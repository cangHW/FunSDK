#pragma once

#include <vector>
#include <string>
#include <cstdint>
#include <memory>

// LZ4 头文件
#include "lz4.h"

// 安全块 常量
class SecurityBlockConstant {
public:
    // 固定魔数标识
    static constexpr uint32_t MAGIC = 0x4C5A3420;

    // 压缩
    static constexpr uint32_t COMPRESSION_NONE = 0;
    static constexpr uint32_t COMPRESSION_LZ4 = 1;
    static constexpr uint32_t COMPRESSION_GZIP = 2;

    //加密
    static constexpr uint32_t ENCRYPTION_NONE = 0;
    static constexpr uint32_t ENCRYPTION_AES = 1;
};

// 安全块头结构定义
struct SecurityBlockHeader {
    uint32_t magic;           // 魔数标识, 标记该数据是由当前库所处理
    uint32_t compressed_size; // 压缩后大小
    uint32_t original_size;   // 原始大小
    uint32_t algorithm;       // 压缩算法
    uint32_t encryption;      // 加密类型
    uint32_t reserved;        // 保留字段
} __attribute__((packed));

namespace spdlog {
    namespace security {
        // 压缩提供程序接口
        class ICompressionProvider {
        public:
            virtual ~ICompressionProvider() = default;

            // 压缩标记
            virtual uint32_t getAlgorithm() = 0;

            // 压缩
            virtual std::vector<uint8_t> compress(const std::vector<uint8_t> &data) = 0;

            // 解压缩
            virtual std::vector<uint8_t>
            decompress(const std::vector<uint8_t> &data, uint32_t original_size) = 0;
        };

        // 加密提供程序接口
        class ICryptoProvider {
        public:
            virtual ~ICryptoProvider() = default;

            // 加密标记
            virtual uint32_t getEncryption() = 0;

            // 加密
            virtual std::vector<uint8_t> encrypt(const std::vector<uint8_t> &data) = 0;

            // 解密
            virtual std::vector<uint8_t> decrypt(const std::vector<uint8_t> &data) = 0;
        };
    }
}

// LZ4 压缩
class LZ4CompressionProvider : public spdlog::security::ICompressionProvider {
public:
    uint32_t getAlgorithm() override {
        return SecurityBlockConstant::COMPRESSION_LZ4;
    }

    std::vector<uint8_t> compress(const std::vector<uint8_t> &data) override {
        if (data.empty()) {
            return data;
        }

        const int src_size = static_cast<int>(data.size());
        const int max_dst_size = LZ4_compressBound(src_size);

        std::vector<uint8_t> compressed(max_dst_size);

        const int compressed_size = LZ4_compress_default(
                reinterpret_cast<const char *>(data.data()),
                reinterpret_cast<char *>(compressed.data()),
                src_size,
                max_dst_size
        );

        if (compressed_size <= 0) {
            // 压缩失败，返回原始数据
            return data;
        }

        // 调整 vector 大小到实际压缩后的大小
        compressed.resize(compressed_size);

        return compressed;
    }

    std::vector<uint8_t>
    decompress(const std::vector<uint8_t> &data, uint32_t original_size) override {
        if (data.empty() || original_size == 0) {
            return data;
        }

        // 限制最大 300MB
        if (original_size > 300 * 1024 * 1024) {
            // 原始大小过大，返回压缩数据
            return data;
        }

        std::vector<uint8_t> decompressed(original_size);

        const int decompressed_size = LZ4_decompress_safe(
                reinterpret_cast<const char *>(data.data()),
                reinterpret_cast<char *>(decompressed.data()),
                static_cast<int>(data.size()),
                static_cast<int>(original_size)
        );

        if (decompressed_size != static_cast<int>(original_size)) {
            // 解压缩失败，返回原始压缩数据
            return data;
        }

        return decompressed;
    }
};

// 保留模拟压缩实现作为备用
class GzipCompressionProvider : public spdlog::security::ICompressionProvider {
public:

    uint32_t getAlgorithm() override {
        return SecurityBlockConstant::COMPRESSION_GZIP;
    }

    std::vector<uint8_t> compress(const std::vector<uint8_t> &data) override {
        // 模拟压缩：添加标记
        std::string compressed_marker = "[GZIP_COMPRESSED] ";
        std::vector<uint8_t> result;
        result.insert(result.end(), compressed_marker.begin(), compressed_marker.end());
        result.insert(result.end(), data.begin(), data.end());
        return result;
    }

    std::vector<uint8_t>
    decompress(const std::vector<uint8_t> &data, uint32_t original_size) override {
        // 模拟解压缩
        return data;
    }
};

// 模拟加密实现
class AESCryptoProvider : public spdlog::security::ICryptoProvider {
private:
    std::string key_;

public:
    explicit AESCryptoProvider(const std::string &key) : key_(key) {}

    uint32_t getEncryption() override {
        return SecurityBlockConstant::ENCRYPTION_AES;
    }

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

