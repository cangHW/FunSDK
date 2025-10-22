#pragma once

#include "../../spdlog/include/spdlog/spdlog.h"
#include "../../spdlog/include/spdlog/sinks/base_sink.h"
#include "../../spdlog/include/spdlog/sinks/sink.h"
#include "../../spdlog/include/spdlog/formatter.h"

#include <vector>
#include <mutex>
#include <string>
#include <iostream>

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

// 🚀 高性能无EOL格式化器
class original_formatter : public spdlog::formatter {
public:
    void format(const spdlog::details::log_msg &msg,
                spdlog::memory_buf_t &dest) override {
        // 只输出原始消息内容，不添加任何EOL
        dest.append(
                msg.payload.data(),
                msg.payload.data() + msg.payload.size()
        );
    }

    std::unique_ptr<spdlog::formatter> clone() const override {
        return spdlog::details::make_unique<original_formatter>();
    }
};

// 模拟压缩实现
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

        // 🎯 装饰器模式的增强Sink - 关键实现！
        template<typename Mutex>
        class enhanced_decorator_security_sink final : public spdlog::sinks::base_sink<Mutex> {
        public:
            // 构造函数：接受原始sink作为参数
            enhanced_decorator_security_sink(
                    std::shared_ptr<sink> underlying_sink,
                    std::unique_ptr<ICompressionProvider> compressor = nullptr,
                    std::unique_ptr<ICryptoProvider> crypto = nullptr,
                    size_t buffer_size = 8192)
                    : underlying_sink_(std::move(underlying_sink)),
                      compressor_(std::move(compressor)),
                      crypto_(std::move(crypto)),
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
                    details::log_msg formatted_msg(msg.time, msg.source, msg.logger_name, msg.level,
                                                   string_view_t(formatted.data(),
                                                                 formatted.size()));
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

                // 🔸 步骤1: 压缩数据
                if (compressor_) {
                    processed_data = compressor_->compress(processed_data);
                    std::cout << "   📦 压缩完成: " << buffer_.size()
                              << " → " << processed_data.size() << " 字节" << std::endl;
                }

                // 🔸 步骤2: 加密数据
                if (crypto_) {
                    processed_data = crypto_->encrypt(processed_data);
                    std::cout << "   🔐 加密完成: 数据已加密" << std::endl;
                }

                // 🔸 步骤3: 创建新的log_msg并传递给底层sink
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

    std::shared_ptr<sinks::sink> create_security_sink(
            std::shared_ptr<spdlog::logger> release_logger,
            bool isSync,
            bool enable_compression,
            const std::string &encryption_key,
            size_t buffer_size
    ) {
        // 1. 获取原始logger的sink
        if (!release_logger || release_logger->sinks().empty()) {
            return nullptr;
        }
        auto original_sink = release_logger->sinks()[0];

        original_sink->set_formatter(
                spdlog::details::make_unique<original_formatter>());

        // 2. 准备压缩和加密提供程序
        std::unique_ptr<ICompressionProvider> compressor = nullptr;
        std::unique_ptr<ICryptoProvider> crypto = nullptr;

        if (enable_compression) {
            compressor.reset(new GzipCompressionProvider());
        }

        if (!encryption_key.empty()) {
            crypto.reset(new AESCryptoProvider(encryption_key));
        }

         if (isSync) {
             return std::make_shared<sinks::enhanced_decorator_security_sink_st>(
                     original_sink,
                     std::move(compressor),
                     std::move(crypto),
                     buffer_size);
         } else {
             return std::make_shared<sinks::enhanced_decorator_security_sink_mt>(
                     original_sink,
                     std::move(compressor),
                     std::move(crypto),
                     buffer_size);
         }
    }

    template<typename Factory = spdlog::synchronous_factory>
    inline std::shared_ptr<logger> enhanced_decorator_security_logger_st(
            const std::string &logger_name,
            std::shared_ptr<spdlog::logger> release_logger,
            bool enable_compression = false,
            const std::string &encryption_key = "",
            size_t buffer_size = 8192) {
        auto enhanced_sink = create_security_sink(release_logger, true, enable_compression,
                                                  encryption_key, buffer_size);
        return std::make_shared<spdlog::logger>(logger_name, enhanced_sink);
    }

    template<typename Factory = spdlog::synchronous_factory>
    inline std::shared_ptr<logger> enhanced_decorator_security_logger_mt(
            const std::string &logger_name,
            std::shared_ptr<spdlog::logger> release_logger,
            bool enable_compression = false,
            const std::string &encryption_key = "",
            size_t buffer_size = 8192) {
        auto enhanced_sink = create_security_sink(release_logger, false, enable_compression,
                                                  encryption_key, buffer_size);
        return std::make_shared<spdlog::logger>(logger_name, enhanced_sink);
    }

} // namespace spdlog

