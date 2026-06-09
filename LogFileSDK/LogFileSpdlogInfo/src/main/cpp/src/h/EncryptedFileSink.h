#ifndef ENCRYPTED_FILE_SINK_H
#define ENCRYPTED_FILE_SINK_H

#include <cstdio>
#include <cstdint>
#include <cstring>
#include <string>
#include <vector>
#include <mutex>
#include <chrono>
#include <random>
#include "spdlog/sinks/base_sink.h"
#include "spdlog/details/file_helper.h"
#include "../crypto/chacha20poly1305.h"

namespace sinks {

static const uint32_t ELOG_MAGIC = 0x454C4F47;
static const uint16_t ELOG_VERSION = 0x0001;
static const uint16_t ELOG_FLAG_ENCRYPTED = 0x0001;
static const size_t ELOG_HEADER_SIZE = 20;

struct ElogHeader {
    uint32_t magic;
    uint16_t version;
    uint16_t flags;
    uint32_t file_id;
    uint8_t reserved[8];
};

static uint32_t generate_file_id() {
    std::random_device rd;
    return rd();
}

template<typename Mutex>
class encrypted_file_sink : public spdlog::sinks::base_sink<Mutex> {
public:
    encrypted_file_sink(const std::string &filename,
                        const uint8_t *key, size_t key_len)
            : filename_(filename), counter_(0), current_size_(0) {
        encrypted_ = (key != nullptr && key_len == crypto::CHACHA20POLY1305_KEY_SIZE);
        if (encrypted_) {
            memcpy(key_, key, 32);
        } else {
            memset(key_, 0, 32);
        }
        open_file(filename_);
    }

    ~encrypted_file_sink() override {
        close_file();
    }

    size_t current_size() const { return current_size_; }

protected:
    void sink_it_(const spdlog::details::log_msg &msg) override {
        spdlog::memory_buf_t formatted;
        this->formatter_->format(msg, formatted);

        if (!encrypted_) {
            fwrite(formatted.data(), 1, formatted.size(), fd_);
            current_size_ += formatted.size();
            return;
        }

        size_t plain_len = formatted.size();
        std::vector<uint8_t> ciphertext(plain_len);
        uint8_t tag[16];

        uint8_t nonce[12] = {0};
        memcpy(nonce, &file_id_, 4);
        memcpy(nonce + 4, &counter_, 8);
        counter_++;

        crypto::chacha20poly1305_encrypt(
                ciphertext.data(), tag,
                (const uint8_t *) formatted.data(), plain_len,
                nullptr, 0,
                nonce, key_);

        uint32_t record_len = 12 + (uint32_t) plain_len + 16;
        fwrite(&record_len, 4, 1, fd_);
        fwrite(nonce, 12, 1, fd_);
        fwrite(ciphertext.data(), plain_len, 1, fd_);
        fwrite(tag, 16, 1, fd_);
        current_size_ += 4 + record_len;
    }

    void flush_() override {
        if (fd_) fflush(fd_);
    }

    void open_file(const std::string &path) {
        fd_ = fopen(path.c_str(), "ab");
        if (!fd_) return;

        long pos = ftell(fd_);
        if (pos == 0) {
            if (encrypted_) {
                write_header();
            }
        } else {
            current_size_ = (size_t) pos;
            if (encrypted_) {
                recover_state(path);
            }
        }
    }

    void recover_state(const std::string &path) {
        FILE *rf = fopen(path.c_str(), "rb");
        if (!rf) return;

        ElogHeader hdr = {};
        if (fread(&hdr, 1, ELOG_HEADER_SIZE, rf) != ELOG_HEADER_SIZE ||
            hdr.magic != ELOG_MAGIC) {
            fclose(rf);
            return;
        }
        file_id_ = hdr.file_id;

        uint64_t count = 0;
        while (true) {
            uint32_t record_len;
            if (fread(&record_len, 4, 1, rf) != 1) break;
            if (fseek(rf, record_len, SEEK_CUR) != 0) break;
            count++;
        }
        counter_ = count;
        fclose(rf);
    }

    void close_file() {
        if (fd_) {
            fflush(fd_);
            fclose(fd_);
            fd_ = nullptr;
        }
    }

    void write_header() {
        file_id_ = generate_file_id();
        counter_ = 0;

        ElogHeader hdr = {};
        hdr.magic = ELOG_MAGIC;
        hdr.version = ELOG_VERSION;
        hdr.flags = encrypted_ ? ELOG_FLAG_ENCRYPTED : 0;
        hdr.file_id = file_id_;
        memset(hdr.reserved, 0, 8);

        fwrite(&hdr, 1, ELOG_HEADER_SIZE, fd_);
        current_size_ = ELOG_HEADER_SIZE;
    }

    std::string filename_;
    FILE *fd_ = nullptr;
    uint8_t key_[32];
    bool encrypted_;
    uint32_t file_id_ = 0;
    uint64_t counter_;
    size_t current_size_;
};

template<typename Mutex>
class encrypted_rotating_sink : public encrypted_file_sink<Mutex> {
public:
    encrypted_rotating_sink(const std::string &base_filename,
                            size_t max_size, size_t max_files,
                            const uint8_t *key, size_t key_len)
            : encrypted_file_sink<Mutex>(base_filename, key, key_len),
              base_filename_(base_filename),
              max_size_(max_size),
              max_files_(max_files) {}

protected:
    void sink_it_(const spdlog::details::log_msg &msg) override {
        encrypted_file_sink<Mutex>::sink_it_(msg);
        if (this->current_size_ >= max_size_) {
            rotate();
        }
    }

private:
    void rotate() {
        this->close_file();

        for (int i = (int) max_files_ - 1; i > 0; --i) {
            std::string src = calc_filename(i - 1);
            std::string dst = calc_filename(i);
            if (i - 1 == 0) src = base_filename_;
            std::remove(dst.c_str());
            std::rename(src.c_str(), dst.c_str());
        }

        this->fd_ = fopen(base_filename_.c_str(), "wb");
        if (this->fd_) {
            if (this->encrypted_) {
                this->write_header();
            } else {
                this->current_size_ = 0;
            }
        }
    }

    std::string calc_filename(int index) const {
        if (index == 0) return base_filename_;
        auto dot_pos = base_filename_.rfind('.');
        if (dot_pos == std::string::npos) {
            return base_filename_ + "." + std::to_string(index);
        }
        return base_filename_.substr(0, dot_pos) + "." +
               std::to_string(index) + base_filename_.substr(dot_pos);
    }

    std::string base_filename_;
    size_t max_size_;
    size_t max_files_;
};

template<typename Mutex>
class encrypted_daily_sink : public encrypted_file_sink<Mutex> {
public:
    encrypted_daily_sink(const std::string &base_filename,
                         int rotation_hour, int rotation_minute,
                         const uint8_t *key, size_t key_len)
            : encrypted_file_sink<Mutex>(daily_filename(base_filename, now_tm()), key, key_len),
              base_filename_(base_filename),
              rotation_hour_(rotation_hour),
              rotation_minute_(rotation_minute) {
        rotation_tp_ = next_rotation_tp();
    }

protected:
    void sink_it_(const spdlog::details::log_msg &msg) override {
        auto time = msg.time;
        if (time >= rotation_tp_) {
            this->close_file();
            std::string new_path = daily_filename(base_filename_, now_tm());
            this->filename_ = new_path;
            this->fd_ = fopen(new_path.c_str(), "wb");
            if (this->fd_) {
                if (this->encrypted_) {
                    this->write_header();
                } else {
                    this->current_size_ = 0;
                }
            }
            rotation_tp_ = next_rotation_tp();
        }
        encrypted_file_sink<Mutex>::sink_it_(msg);
    }

private:
    static std::tm now_tm() {
        auto now = std::chrono::system_clock::now();
        auto time = std::chrono::system_clock::to_time_t(now);
        std::tm tm_val = {};
        localtime_r(&time, &tm_val);
        return tm_val;
    }

    static std::string daily_filename(const std::string &base, const std::tm &tm_val) {
        auto dot_pos = base.rfind('.');
        std::string prefix = (dot_pos == std::string::npos) ? base : base.substr(0, dot_pos);
        std::string suffix = (dot_pos == std::string::npos) ? "" : base.substr(dot_pos);

        char date_buf[16];
        snprintf(date_buf, sizeof(date_buf), "_%04d-%02d-%02d",
                 tm_val.tm_year + 1900, tm_val.tm_mon + 1, tm_val.tm_mday);

        return prefix + date_buf + suffix;
    }

    std::chrono::system_clock::time_point next_rotation_tp() {
        auto now = std::chrono::system_clock::now();
        auto time = std::chrono::system_clock::to_time_t(now);
        std::tm tm_val = {};
        localtime_r(&time, &tm_val);

        tm_val.tm_hour = rotation_hour_;
        tm_val.tm_min = rotation_minute_;
        tm_val.tm_sec = 0;

        auto tp = std::chrono::system_clock::from_time_t(mktime(&tm_val));
        if (tp <= now) {
            tp += std::chrono::hours(24);
        }
        return tp;
    }

    std::string base_filename_;
    int rotation_hour_;
    int rotation_minute_;
    std::chrono::system_clock::time_point rotation_tp_;
};

using encrypted_file_sink_st = encrypted_file_sink<spdlog::details::null_mutex>;
using encrypted_file_sink_mt = encrypted_file_sink<std::mutex>;
using encrypted_rotating_sink_st = encrypted_rotating_sink<spdlog::details::null_mutex>;
using encrypted_rotating_sink_mt = encrypted_rotating_sink<std::mutex>;
using encrypted_daily_sink_st = encrypted_daily_sink<spdlog::details::null_mutex>;
using encrypted_daily_sink_mt = encrypted_daily_sink<std::mutex>;

} // namespace sinks

#endif // ENCRYPTED_FILE_SINK_H
