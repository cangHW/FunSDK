#pragma once

#include "SecurityFactory.h"
#include <fstream>
#include <memory>
#include <vector>
#include <string>
#include <algorithm>
#include <unordered_map>
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "LogFileDecompressor", __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, "LogFileDecompressor", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "LogFileDecompressor", __VA_ARGS__)

// 日志文件解压缩器
class LogFileDecompressor {
public:
    LogFileDecompressor(const std::string &encryption_key) : key_(encryption_key) {

    }

    // 流式处理，避免大文件内存占用
    bool decompressLogFile(const std::string &input_file, const std::string &output_file) {
        std::ifstream input(input_file, std::ios::binary);
        if (!input.is_open()) {
            LOGE("Failed to open input file: %s", input_file.c_str());
            return false;
        }

        std::ofstream output(output_file, std::ios::binary);
        if (!output.is_open()) {
            LOGE("Failed to open output file: %s", output_file.c_str());
            input.close();
            return false;
        }

        // 流式处理缓冲区
        constexpr size_t BUFFER_SIZE = 65536; // 64KB
        std::vector<uint8_t> buffer;
        buffer.reserve(BUFFER_SIZE);

        size_t total_input_bytes = 0;
        size_t total_output_bytes = 0;
        bool success = true;

        while (input.good() && success) {
            // 读取一块数据
            char temp_buffer[BUFFER_SIZE];
            input.read(temp_buffer, BUFFER_SIZE);
            const std::streamsize bytes_read = input.gcount();

            if (bytes_read <= 0) {
                break;
            }

            total_input_bytes += bytes_read;

            // 将新数据添加到缓冲区
            buffer.insert(buffer.end(), temp_buffer, temp_buffer + bytes_read);

            // 流式处理缓冲区中的数据
            size_t processed_bytes = processBufferStream(buffer, output, total_output_bytes);

            if (processed_bytes == 0) {
                // 没有处理任何数据，可能的原因：
                // 1. 数据不完整，需要更多数据
                // 2. 缓冲区中有无效数据
                // 3. 缓冲区过大，可能数据损坏

                if (buffer.size() >= BUFFER_SIZE * 4) { // 增加阈值，给更多机会
                    LOGW("Buffer overflow, possible corrupted data (size: %zu)", buffer.size());
                    success = false;
                    break;
                }

                // 如果缓冲区已经很大但没有处理任何数据，可能是数据损坏
                if (buffer.size() >= BUFFER_SIZE * 2) {
                    // 尝试跳过一些字节，寻找下一个有效块
                    size_t skip_bytes = std::min(buffer.size() / 4, static_cast<size_t>(1024));
                    buffer.erase(buffer.begin(), buffer.begin() + skip_bytes);
                    LOGW("Skipped %zu bytes to find next valid block", skip_bytes);
                }
            }
        }

        // 处理剩余的缓冲区数据
        if (success && !buffer.empty()) {
            size_t remaining_processed = processBufferStream(buffer, output, total_output_bytes);
            if (remaining_processed == 0 && !buffer.empty()) {
                LOGW("Warning: %zu bytes of data could not be processed", buffer.size());
            }
        }

        input.close();
        output.close();

        if (success) {
            LOGI("Decompression completed: %zu -> %zu bytes (ratio: %.2f%%)",
                 total_input_bytes,
                 total_output_bytes,
                 total_input_bytes > 0 ? (total_output_bytes * 100.0 / total_input_bytes) : 0.0);
        } else {
            LOGE("Decompression failed");
        }

        return success;
    }

private:
    std::string key_;
    std::unordered_map<uint32_t, std::unique_ptr<spdlog::security::ICompressionProvider>> compressionProviderMap;
    std::unordered_map<uint32_t, std::unique_ptr<spdlog::security::ICryptoProvider>> cryptoProviderMap;

    // 获取压缩提供程序
    spdlog::security::ICompressionProvider *getCompressionProvider(uint32_t algorithm) {
        auto it = compressionProviderMap.find(algorithm);
        if (it != compressionProviderMap.end()) {
            return it->second.get();
        }

        auto provider = spdlog::create_compression_provider(algorithm);
        if (provider) {
            compressionProviderMap[algorithm] = std::move(provider);
            return compressionProviderMap[algorithm].get();
        }
        return nullptr;
    }

    // 获取加密提供程序
    spdlog::security::ICryptoProvider *getCryptoProvider(uint32_t encryption) {
        auto it = cryptoProviderMap.find(encryption);
        if (it != cryptoProviderMap.end()) {
            return it->second.get();
        }

        auto provider = spdlog::create_encryption_provider(encryption, key_);
        if (provider) {
            cryptoProviderMap[encryption] = std::move(provider);
            return cryptoProviderMap[encryption].get();
        }
        return nullptr;
    }

    // 流式处理缓冲区数据，返回已处理的字节数
    size_t processBufferStream(
            std::vector<uint8_t> &buffer,
            std::ofstream &output,
            size_t &total_output_bytes
    ) {
        size_t processed_bytes = 0;
        size_t offset = 0;
        const size_t data_size = buffer.size();
        const uint8_t *data_ptr = buffer.data();

        while (offset < data_size) {
            // 检查是否有足够的空间读取头部
            if (offset + sizeof(SecurityBlockHeader) > data_size) {
                // 剩余数据不足以构成完整的头部，保留在缓冲区中
                break;
            }

            const SecurityBlockHeader *header = reinterpret_cast<const SecurityBlockHeader *>(
                    data_ptr + offset
            );

            // 检查是否为有效的压缩块
            if (header->magic == SecurityBlockConstant::MAGIC) {
                // 验证块的完整性和合理性
                int validation_result = validateBlockHeader(header, data_size - offset);

                if (validation_result == 0) {
                    // 数据不完整，需要更多数据，停止处理当前缓冲区
                    break;
                } else if (validation_result == -1) {
                    // 块无效，跳过这个字节
                    offset++;
                    processed_bytes++;
                    continue;
                }

                // validation_result == 1，块有效
                const size_t block_total_size =
                        sizeof(SecurityBlockHeader) + header->compressed_size;

                // 解压缩块
                std::vector<uint8_t> block_data = decompressBlock(data_ptr + offset,
                                                                  block_total_size);
                if (!block_data.empty()) {
                    // 写入输出文件
                    output.write(
                            reinterpret_cast<const char *>(block_data.data()),
                            block_data.size()
                    );

                    if (output.good()) {
                        total_output_bytes += block_data.size();
                        offset += block_total_size;
                        processed_bytes += block_total_size;
                    } else {
                        LOGE("Failed to write decompressed block at offset %zu", offset);
                        break;
                    }
                } else {
                    // 解压失败，跳过这个块
                    LOGE("Failed to decompress block at offset %zu", offset);
                    offset += block_total_size;
                    processed_bytes += block_total_size;
                }
            } else {
                // 非压缩数据，直接写入
                output.put(data_ptr[offset]);
                if (output.good()) {
                    total_output_bytes++;
                    offset++;
                    processed_bytes++;
                } else {
                    LOGE("Failed to write data at offset %zu", offset);
                    break;
                }
            }
        }

        // 移除已处理的数据
        if (processed_bytes > 0) {
            buffer.erase(buffer.begin(), buffer.begin() + processed_bytes);
        }

        return processed_bytes;
    }

    // 验证块头部的有效性（避免重复验证）
    // 返回值：-1=无效块，0=数据不完整需要更多数据，1=有效块
    int validateBlockHeader(const SecurityBlockHeader *header, size_t available_bytes) {
        // 检查魔数
        if (header->magic != SecurityBlockConstant::MAGIC) {
            return -1; // 无效块
        }

        // 检查块大小合理性
        if (header->compressed_size == 0 || header->original_size == 0 ||
            header->compressed_size > 100 * 1024 * 1024 || // 100MB 限制
            header->original_size > 300 * 1024 * 1024) {   // 300MB 限制
            LOGW(
                    "Invalid block size (compressed: %u, original: %u)",
                    header->compressed_size,
                    header->original_size
            );
            return -1; // 无效块
        }

        // 检查是否有足够的数据
        const size_t block_total_size = sizeof(SecurityBlockHeader) + header->compressed_size;
        if (block_total_size > available_bytes) {
            return 0; // 数据不完整，需要更多数据
        }

        return 1; // 有效块
    }

    // 解压缩单个块（从原始指针，已验证的块）
    std::vector<uint8_t> decompressBlock(const uint8_t *block_data, size_t block_size) {
        if (block_size < sizeof(SecurityBlockHeader)) {
            LOGW("Block too small: %zu bytes", block_size);
            return {};
        }

        const SecurityBlockHeader *header = reinterpret_cast<const SecurityBlockHeader *>(block_data);

        // 计算负载偏移量
        const size_t payload_offset = sizeof(SecurityBlockHeader);
        const size_t payload_size = header->compressed_size;

        if (payload_offset + payload_size > block_size) {
            LOGW("Incomplete block payload");
            return {};
        }

        // 优化：直接使用指针构造，避免不必要的拷贝
        std::vector<uint8_t> payload(
                block_data + payload_offset,
                block_data + payload_offset + payload_size
        );

        // 解密处理
        if (header->encryption != SecurityBlockConstant::ENCRYPTION_NONE) {
            auto crypto_provider = getCryptoProvider(header->encryption);
            if (!crypto_provider) {
                LOGE("No crypto provider available for encryption type: %u", header->encryption);
                return {};
            }

            payload = crypto_provider->decrypt(payload);
            if (payload.empty()) {
                LOGE("Decryption failed");
                return {};
            }
        }

        // 根据算法类型解压缩
        std::vector<uint8_t> decompressed_data;
        // 解压缩处理
        if (header->algorithm != SecurityBlockConstant::COMPRESSION_NONE) {
            auto compression_provider = getCompressionProvider(header->algorithm);
            if (!compression_provider) {
                LOGE("No compression provider available for algorithm: %u", header->algorithm);
                return {};
            }

            decompressed_data = compression_provider->decompress(payload, header->original_size);
            // 验证解压结果
            if (decompressed_data.empty()) {
                LOGE("Decompression returned empty data");
                return {};
            }
        } else {
            // 无压缩，直接使用原始数据
            decompressed_data = payload;
        }

        if (decompressed_data.size() != header->original_size) {
            LOGW("Decompression size mismatch: expected=%u, actual=%zu",
                 header->original_size, decompressed_data.size());
            // 根据策略决定是返回还是继续（这里选择继续）
        }

        return decompressed_data;
    }
};
