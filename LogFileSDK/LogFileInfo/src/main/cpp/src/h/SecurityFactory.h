#pragma once

#include "SecurityProvider.h"
#include <memory>
#include <string>

namespace spdlog {

    // 压缩算法工厂函数
    inline std::unique_ptr<spdlog::security::ICompressionProvider> create_compression_provider(
            uint32_t compressionMode
    ) {
        std::unique_ptr<spdlog::security::ICompressionProvider> provider = nullptr;
        if (compressionMode == SecurityBlockConstant::COMPRESSION_LZ4) {
            provider.reset(new LZ4CompressionProvider());
        }
        return provider;
    }

    // 加密算法工厂函数
    inline std::unique_ptr<spdlog::security::ICryptoProvider> create_encryption_provider(
            uint32_t encryptionMode,
            const std::string &encryptionKey
    ) {
        std::unique_ptr<spdlog::security::ICryptoProvider> provider = nullptr;
        if (encryptionKey.empty()) {
            return provider;
        }

        if (encryptionMode == SecurityBlockConstant::ENCRYPTION_CHACHA20) {
            provider.reset(new ChaCha20CryptoProvider(encryptionKey));
        }
        return provider;
    }
}