#pragma once

#include <vector>
#include <string>
#include <cstdint>
#include <memory>

// 压缩
#include "lz4.h"
#include "zstd.h"

// 加密
#include "SecurityUtils.h"
#include "chacha20.h"


// 安全块 常量
class SecurityBlockConstant {
public:
    // 固定魔数标识
    static constexpr uint32_t MAGIC = 0x4C5A3420;

    // 压缩
    static constexpr uint32_t COMPRESSION_NONE = 0;
    static constexpr uint32_t COMPRESSION_LZ4 = 1;
    static constexpr uint32_t COMPRESSION_ZSTD = 2;

    //加密
    static constexpr uint32_t ENCRYPTION_NONE = 0;
    static constexpr uint32_t ENCRYPTION_CHACHA20 = 1;
};

// 安全块头结构定义
struct SecurityBlockHeader {
    uint32_t magic;           // 魔数标识, 标记该数据是由当前库所处理
    uint32_t compressed_size; // 压缩后大小
    uint32_t original_size;   // 原始大小
    uint32_t algorithm;       // 压缩算法
    uint32_t encryption;      // 加密类型
    uint8_t salt[32];         // 密钥派生盐（加密时随机生成并保存）
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

            // 加密（返回 nonce + 密文）
            virtual std::vector<uint8_t> encrypt(const std::vector<uint8_t> &data) = 0;

            // 解密
            virtual std::vector<uint8_t> decrypt(const std::vector<uint8_t> &data) = 0;
            
            // 获取盐（加密时生成的随机盐）
            virtual std::vector<uint8_t> getSalt() const = 0;
            
            // 设置盐（解密时从 Header 读取）
            virtual void setSalt(const uint8_t *salt, size_t salt_len) = 0;
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
            // 压缩失败，返回空数据
            return {};
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

// ZSTD 压缩提供程序（固定级别 3）
class ZSTDCompressionProvider : public spdlog::security::ICompressionProvider {
public:
    uint32_t getAlgorithm() override {
        return SecurityBlockConstant::COMPRESSION_ZSTD;
    }

    std::vector<uint8_t> compress(const std::vector<uint8_t> &data) override {
        if (data.empty()) {
            return data;
        }

        const size_t src_size = data.size();
        size_t const max_dst_size = ZSTD_compressBound(src_size);
        
        std::vector<uint8_t> compressed(max_dst_size);

        // 固定使用级别 3（平衡速度和压缩率）
        size_t const compressed_size = ZSTD_compress(
            compressed.data(), max_dst_size,
            data.data(), src_size,
            3  // 固定压缩级别
        );

        if (ZSTD_isError(compressed_size)) {
            // 压缩失败，返回空数据
            return {};
        }

        // 调整 vector 大小到实际压缩后的大小
        compressed.resize(compressed_size);

        return compressed;
    }

    std::vector<uint8_t> decompress(
        const std::vector<uint8_t> &data, 
        uint32_t original_size
    ) override {
        if (data.empty() || original_size == 0) {
            return data;
        }

        // 限制最大 300MB（与 LZ4 保持一致）
        if (original_size > 300 * 1024 * 1024) {
            // 原始大小过大，返回压缩数据
            return data;
        }

        std::vector<uint8_t> decompressed(original_size);

        // ZSTD 解压缩
        size_t const decompressed_size = ZSTD_decompress(
            decompressed.data(), original_size,
            data.data(), data.size()
        );

        // 检查解压结果
        if (ZSTD_isError(decompressed_size) || 
            decompressed_size != original_size) {
            // 解压缩失败，返回原始压缩数据
            return data;
        }

        return decompressed;
    }
};

// ChaCha20 加密提供程序
class ChaCha20CryptoProvider : public spdlog::security::ICryptoProvider {
private:
    std::string password_;
    uint8_t derived_key_[CHACHA20_KEY_SIZE];
    std::vector<uint8_t> salt_;  // 保存盐（加密时生成，解密时从外部设置）

public:
    explicit ChaCha20CryptoProvider(const std::string &password) : password_(password), salt_(32) {
        // 构造时不派生密钥，等待设置盐后再派生
    }

    uint32_t getEncryption() override {
        return SecurityBlockConstant::ENCRYPTION_CHACHA20;
    }
    
    std::vector<uint8_t> getSalt() const override {
        return salt_;
    }
    
    void setSalt(const uint8_t *salt, size_t salt_len) override {
        if (salt && salt_len == 32) {
            salt_.assign(salt, salt + salt_len);
            deriveKey();
        }
    }

    std::vector<uint8_t> encrypt(const std::vector<uint8_t> &data) override {
        if (data.empty()) {
            return data;
        }

        // 加密时生成新的随机盐
        if (!SecurityUtils::generateSecureSalt(salt_, 32)) {
            return {};
        }
        
        // 使用生成的盐派生密钥
        deriveKey();

        // 生成密码学安全的 nonce
        uint8_t nonce[CHACHA20_NONCE_SIZE];
        if (!SecurityUtils::generateSecureNonce(nonce, CHACHA20_NONCE_SIZE)) {
            // 如果安全随机数生成失败，使用降级方案
            if (chacha20_generate_nonce(nonce) != 0) {
                return {};
            }
        }

        // 加密数据
        std::vector<uint8_t> encrypted(data.size());
        if (chacha20_encrypt(derived_key_, nonce, 0, data.data(), data.size(), encrypted.data()) != 0) {
            return {};
        }

        // 返回 nonce + 密文
        std::vector<uint8_t> result;
        result.reserve(CHACHA20_NONCE_SIZE + encrypted.size());
        result.insert(result.end(), nonce, nonce + CHACHA20_NONCE_SIZE);
        result.insert(result.end(), encrypted.begin(), encrypted.end());

        return result;
    }

    std::vector<uint8_t> decrypt(const std::vector<uint8_t> &data) override {
        if (data.size() < CHACHA20_NONCE_SIZE) {
            return {};
        }

        // 提取 nonce
        uint8_t nonce[CHACHA20_NONCE_SIZE];
        memcpy(nonce, data.data(), CHACHA20_NONCE_SIZE);

        // 提取加密数据
        std::vector<uint8_t> encrypted_data(data.begin() + CHACHA20_NONCE_SIZE, data.end());

        // 解密数据
        std::vector<uint8_t> decrypted(encrypted_data.size());
        if (chacha20_decrypt(derived_key_, nonce, 0, encrypted_data.data(), encrypted_data.size(), decrypted.data()) != 0) {
            return {};
        }

        return decrypted;
    }

private:
    void deriveKey() {
        // 使用盐派生密钥
        if (!SecurityUtils::deriveKeySecure(password_, salt_, derived_key_, CHACHA20_KEY_SIZE)) {
            // 如果安全派生失败，使用降级方案
            SecurityUtils::deriveKeyFallback(password_, derived_key_, CHACHA20_KEY_SIZE);
        }
    }
};

