#pragma once

#include "../../spdlog/include/spdlog/spdlog.h"
#include "../../spdlog/include/spdlog/sinks/base_sink.h"
#include "../../spdlog/include/spdlog/sinks/sink.h"
#include "../../spdlog/include/spdlog/formatter.h"

#include <vector>
#include <mutex>
#include <string>
#include <iostream>
#include <cstring>

#include "SecurityProvider.h"
#include "SecurityFactory.h"

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

namespace spdlog {
    namespace sinks {

        // 增强Sink - 提供压缩、加密能力
        template<typename Mutex>
        class enhanced_decorator_security_sink final : public spdlog::sinks::base_sink<Mutex> {
        public:
            enhanced_decorator_security_sink(
                    std::shared_ptr<sink> underlying_sink,
                    std::unique_ptr<spdlog::security::ICompressionProvider> compression,
                    std::unique_ptr<spdlog::security::ICryptoProvider> encryption,
                    size_t buffer_size
            ) : underlying_sink_(std::move(underlying_sink)),
                compression_(std::move(compression)),
                encryption_(std::move(encryption)),
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
                if (needs_processing()) {
                    flush_buffer();
                }
                if (underlying_sink_) {
                    underlying_sink_->flush();
                }
            }

        private:
            std::shared_ptr<sink> underlying_sink_;
            std::unique_ptr<spdlog::security::ICompressionProvider> compression_;
            std::unique_ptr<spdlog::security::ICryptoProvider> encryption_;
            std::vector<uint8_t> buffer_;
            size_t buffer_size_;

            bool needs_processing() const {
                return compression_ || encryption_;
            }

            void flush_buffer() {
                if (buffer_.empty() || !underlying_sink_) {
                    return;
                }

                std::vector<uint8_t> processed_data = buffer_;
                uint32_t original_size = static_cast<uint32_t>(buffer_.size());

                // 压缩数据
                uint32_t compressionAlgorithm = SecurityBlockConstant::COMPRESSION_NONE;
                if (compression_) {
                    compressionAlgorithm = compression_->getAlgorithm();
                    processed_data = compression_->compress(processed_data);
                }

                // 加密数据
                uint32_t encryptionAlgorithm = SecurityBlockConstant::ENCRYPTION_NONE;
                if (encryption_) {
                    encryptionAlgorithm = encryption_->getEncryption();
                    processed_data = encryption_->encrypt(processed_data);
                }

                // 创建压缩块头
                SecurityBlockHeader header;
                header.magic = SecurityBlockConstant::MAGIC;
                header.compressed_size = static_cast<uint32_t>(processed_data.size());
                header.original_size = original_size;
                header.algorithm = compressionAlgorithm;
                header.encryption = encryptionAlgorithm;
                header.reserved = 0;

                // 组装最终数据：头部 + 压缩数据
                std::vector<uint8_t> final_data;
                final_data.resize(sizeof(header) + processed_data.size());

                std::memcpy(
                        final_data.data(),
                        &header,
                        sizeof(header)
                );
                std::memcpy(
                        final_data.data() + sizeof(header),
                        processed_data.data(),
                        processed_data.size()
                );

                // 创建新的log_msg并传递给底层sink
                details::log_msg processed_msg;
                processed_msg.payload = std::string(final_data.begin(), final_data.end());
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

    inline std::shared_ptr<sinks::sink> create_security_sink(
            std::shared_ptr<spdlog::logger> release_logger,
            bool isSync,
            jint &compressionMode,
            jint &encryptionMode,
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

        std::unique_ptr<spdlog::security::ICompressionProvider> compression = spdlog::create_compression_provider(
                static_cast<uint32_t>(compressionMode)
        );
        std::unique_ptr<spdlog::security::ICryptoProvider> encryption = spdlog::create_encryption_provider(
                static_cast<uint32_t>(encryptionMode),
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
            jint &compressionMode,
            jint &encryptionMode,
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
            jint &compressionMode,
            jint &encryptionMode,
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

