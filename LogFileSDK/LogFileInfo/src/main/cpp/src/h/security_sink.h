#pragma once

#include "../../spdlog/include/spdlog/spdlog.h"
#include "../../spdlog/include/spdlog/sinks/base_sink.h"
#include "../../spdlog/include/spdlog/sinks/sink.h"
#include "../../spdlog/include/spdlog/formatter.h"

#include <vector>
#include <mutex>
#include <string>
#include <iostream>

// LZ4 头文件
#include "lz4.h"

// 压缩提供程序接口
class ICompressionProvider {
public:
    virtual ~ICompressionProvider() = default;

    virtual std::vector<uint8_t> compress(const std::vector<uint8_t> &data) = 0;

    virtual std::vector<uint8_t> decompress(const std::vector<uint8_t> &data) = 0;
};

// 加密提供程序接口
class ICryptoProvider {
public:
    virtual ~ICryptoProvider() = default;

    virtual std::vector<uint8_t> encrypt(const std::vector<uint8_t> &data) = 0;

    virtual std::vector<uint8_t> decrypt(const std::vector<uint8_t> &data) = 0;
};

// 空处理格式化器
class empty_formatter : public spdlog::formatter {
public:
    void format(
            const spdlog::details::log_msg &msg,
            spdlog::memory_buf_t &dest
    ) override {
        // 输出原始消息内容
        dest.append(
                msg.payload.data(),
                msg.payload.data() + msg.payload.size()
        );
    }

    std::unique_ptr<spdlog::formatter> clone() const override {
        return spdlog::details::make_unique<empty_formatter>();
    }
};

// LZ4 高性能压缩实现
class LZ4CompressionProvider : public ICompressionProvider {
public:
    std::vector<uint8_t> compress(const std::vector<uint8_t> &data) override {
        if (data.empty()) {
            return data;
        }

        // 计算压缩缓冲区的最大大小
        const int src_size = static_cast<int>(data.size());
        const int max_dst_size = LZ4_compressBound(src_size);

        std::vector<uint8_t> compressed(max_dst_size + sizeof(int)); // 前4字节存储原始大小

        // 在压缩数据前存储原始大小，用于解压缩
        *reinterpret_cast<int *>(compressed.data()) = src_size;

        // 执行 LZ4 压缩
        const int compressed_size = LZ4_compress_default(
                reinterpret_cast<const char *>(data.data()),
                reinterpret_cast<char *>(compressed.data() + sizeof(int)),
                src_size,
                max_dst_size
        );

        if (compressed_size <= 0) {
            // 压缩失败，返回原始数据
            return data;
        }

        // 调整 vector 大小到实际压缩后的大小
        compressed.resize(compressed_size + sizeof(int));

        return compressed;
    }

    std::vector<uint8_t> decompress(const std::vector<uint8_t> &data) override {
        if (data.size() < sizeof(int)) {
            return data; // 数据太小，无法包含大小信息
        }

        // 读取原始大小
        const int original_size = *reinterpret_cast<const int *>(data.data());

        if (original_size <= 0 || original_size > 10 * 1024 * 1024) { // 限制最大10MB
            return data; // 无效的大小
        }

        std::vector<uint8_t> decompressed(original_size);

        // 执行 LZ4 解压缩
        const int decompressed_size = LZ4_decompress_safe(
                reinterpret_cast<const char *>(data.data() + sizeof(int)),
                reinterpret_cast<char *>(decompressed.data()),
                static_cast<int>(data.size() - sizeof(int)),
                original_size
        );

        if (decompressed_size != original_size) {
            // 解压缩失败，返回原始数据
            return data;
        }

        return decompressed;
    }
};

// 📦 保留模拟压缩实现作为备用
class GzipCompressionProvider : public ICompressionProvider {
public:
    std::vector<uint8_t> compress(const std::vector<uint8_t> &data) override {
        // 模拟压缩：添加标记
        std::string compressed_marker = "[GZIP_COMPRESSED] ";
        std::vector<uint8_t> result;
        result.insert(result.end(), compressed_marker.begin(), compressed_marker.end());
        result.insert(result.end(), data.begin(), data.end());
        return result;
    }

    std::vector<uint8_t> decompress(const std::vector<uint8_t> &data) override {
        // 模拟解压缩
        return data;
    }
};

// 模拟加密实现
class AESCryptoProvider : public ICryptoProvider {
private:
    std::string key_;

public:
    explicit AESCryptoProvider(const std::string &key) : key_(key) {}

    std::vector<uint8_t> encrypt(const std::vector<uint8_t> &data) override {
        // 模拟加密：添加标记和密钥信息
        std::string encrypted_marker = "[AES_ENCRYPTED:" + key_ + "] ";
        std::vector<uint8_t> result;
        result.insert(result.end(), encrypted_marker.begin(), encrypted_marker.end());
        result.insert(result.end(), data.begin(), data.end());
        return result;
    }

    std::vector<uint8_t> decrypt(const std::vector<uint8_t> &data) override {
        // 模拟解密
        return data;
    }
};


namespace spdlog {
    namespace sinks {

        // 增强Sink - 提供压缩、加密能力
        template<typename Mutex>
        class enhanced_decorator_security_sink final : public spdlog::sinks::base_sink<Mutex> {
        public:
            enhanced_decorator_security_sink(
                    std::shared_ptr<sink> underlying_sink,
                    std::unique_ptr<ICompressionProvider> compression,
                    std::unique_ptr<ICryptoProvider> encryption,
                    size_t buffer_size
            ) : underlying_sink_(std::move(underlying_sink)),
                compressor_(std::move(compression)),
                crypto_(std::move(encryption)),
                buffer_size_(buffer_size) {

                buffer_.reserve(buffer_size_);
            }

            ~enhanced_decorator_security_sink() {
                flush_buffer();
            }

        protected:
            void sink_it_(const details::log_msg &msg) override {
                // 装饰器接管格式化工作，确保格式化一致性
                memory_buf_t formatted;
                base_sink<Mutex>::formatter_->format(msg, formatted);

                if (!needs_processing()) {
                    details::log_msg formatted_msg(
                            msg.time,
                            msg.source,
                            msg.logger_name,
                            msg.level,
                            string_view_t(formatted.data(), formatted.size())
                    );
                    underlying_sink_->log(formatted_msg);
                    return;
                }

                std::string log_line = std::string(formatted.data(), formatted.size());
                buffer_.insert(buffer_.end(), log_line.begin(), log_line.end());

                if (buffer_.size() >= buffer_size_) {
                    flush_buffer();
                }
            }

            void flush_() override {
                flush_buffer();
                if (underlying_sink_) {
                    underlying_sink_->flush();
                }
            }

        private:
            std::shared_ptr<sink> underlying_sink_;
            std::unique_ptr<ICompressionProvider> compressor_;
            std::unique_ptr<ICryptoProvider> crypto_;
            std::vector<uint8_t> buffer_;
            size_t buffer_size_;

            bool needs_processing() const {
                return compressor_ || crypto_;
            }

            void flush_buffer() {
                if (buffer_.empty() || !underlying_sink_) {
                    return;
                }

                std::vector<uint8_t> processed_data = buffer_;

                // 压缩数据
                if (compressor_) {
                    processed_data = compressor_->compress(processed_data);
                }

                // 加密数据
                if (crypto_) {
                    processed_data = crypto_->encrypt(processed_data);
                }

                // 创建新的log_msg并传递给底层sink
                details::log_msg processed_msg;
                processed_msg.payload = std::string(processed_data.begin(), processed_data.end());

                underlying_sink_->log(processed_msg);

                // 清空缓冲区
                buffer_.clear();
            }
        };

        // 类型别名
        using enhanced_decorator_security_sink_mt = enhanced_decorator_security_sink<std::mutex>;
        using enhanced_decorator_security_sink_st = enhanced_decorator_security_sink<spdlog::details::null_mutex>;

    } // namespace sinks

    // 前向声明
    class logger;

} // namespace spdlog

namespace spdlog {

    // 压缩算法工厂函数
    inline std::unique_ptr<ICompressionProvider> create_compression_provider(
            const std::string &compressionMode
    ) {
        if (compressionMode == "lz4") {
            return spdlog::details::make_unique<LZ4CompressionProvider>();
        } else if (compressionMode == "gzip") {
            return spdlog::details::make_unique<GzipCompressionProvider>();
        }
        // 默认返回 nullptr（不压缩）
        return nullptr;
    }

    // 加密算法工厂函数
    inline std::unique_ptr<ICryptoProvider> create_encryption_provider(
            const std::string &encryptionMode,
            const std::string &encryptionKey
    ) {
        if (encryptionMode == "chacha20") {
            return spdlog::details::make_unique<AESCryptoProvider>(encryptionKey);
        }
        // 默认返回 nullptr（不加密）
        return nullptr;
    }

    inline std::shared_ptr<sinks::sink> create_security_sink(
            std::shared_ptr<spdlog::logger> release_logger,
            bool isSync,
            const std::string &compressionMode,
            const std::string &encryptionMode,
            const std::string &encryptionKey,
            size_t buffer_size
    ) {
        if (!release_logger || release_logger->sinks().empty()) {
            return nullptr;
        }
        auto original_sink = release_logger->sinks()[0];

        original_sink->set_formatter(
                spdlog::details::make_unique<empty_formatter>()
        );

        std::unique_ptr<ICompressionProvider> compression = create_compression_provider(
                compressionMode
        );
        std::unique_ptr<ICryptoProvider> encryption = create_encryption_provider(
                encryptionMode,
                encryptionKey
        );

        if (isSync) {
            return std::make_shared<sinks::enhanced_decorator_security_sink_st>(
                    original_sink,
                    std::move(compression),
                    std::move(encryption),
                    buffer_size
            );
        } else {
            return std::make_shared<sinks::enhanced_decorator_security_sink_mt>(
                    original_sink,
                    std::move(compression),
                    std::move(encryption),
                    buffer_size
            );
        }
    }

    template<typename Factory = spdlog::synchronous_factory>
    inline std::shared_ptr<logger> enhanced_decorator_security_logger_st(
            const std::string &logger_name,
            std::shared_ptr<spdlog::logger> release_logger,
            const std::string &compressionMode,
            const std::string &encryptionMode,
            const std::string &encryptionKey,
            size_t buffer_size = 8192
    ) {
        auto enhanced_sink = create_security_sink(
                release_logger,
                true,
                compressionMode,
                encryptionMode,
                encryptionKey,
                buffer_size
        );
        return std::make_shared<spdlog::logger>(logger_name, enhanced_sink);
    }

    template<typename Factory = spdlog::synchronous_factory>
    inline std::shared_ptr<logger> enhanced_decorator_security_logger_mt(
            const std::string &logger_name,
            std::shared_ptr<spdlog::logger> release_logger,
            const std::string &compressionMode,
            const std::string &encryptionMode,
            const std::string &encryptionKey,
            size_t buffer_size = 8192
    ) {
        auto enhanced_sink = create_security_sink(
                release_logger,
                false,
                compressionMode,
                encryptionMode,
                encryptionKey,
                buffer_size
        );
        return std::make_shared<spdlog::logger>(logger_name, enhanced_sink);
    }

} // namespace spdlog

