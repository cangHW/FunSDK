#include "../spdlog/include/spdlog/spdlog.h"
#include "h/EncryptedFileSink.h"
#include "h/LogFileInit.h"

static const uint8_t *parse_key(const std::string &hexKey, uint8_t out[32]) {
    if (hexKey.empty()) return nullptr;
    if (hexKey.size() != 64) return nullptr;
    for (int i = 0; i < 32; i++) {
        char hi = hexKey[i * 2];
        char lo = hexKey[i * 2 + 1];
        auto hex_val = [](char c) -> int {
            if (c >= '0' && c <= '9') return c - '0';
            if (c >= 'a' && c <= 'f') return c - 'a' + 10;
            if (c >= 'A' && c <= 'F') return c - 'A' + 10;
            return -1;
        };
        int h = hex_val(hi);
        int l = hex_val(lo);
        if (h < 0 || l < 0) return nullptr;
        out[i] = (uint8_t)((h << 4) | l);
    }
    return out;
}

std::shared_ptr<spdlog::logger> rotating_logger(
        jboolean &isSync,
        const std::string &path,
        jlong &maxFileSize,
        jint &maxFiles,
        const std::string &encryptionKey
) {
    uint8_t key_bytes[32];
    const uint8_t *key = parse_key(encryptionKey, key_bytes);

    std::shared_ptr<spdlog::sinks::sink> sink;
    if (isSync) {
        sink = std::make_shared<sinks::encrypted_rotating_sink_st>(
                path, (size_t) maxFileSize, (size_t) maxFiles, key, key ? 32 : 0);
    } else {
        sink = std::make_shared<sinks::encrypted_rotating_sink_mt>(
                path, (size_t) maxFileSize, (size_t) maxFiles, key, key ? 32 : 0);
    }
    return std::make_shared<spdlog::logger>("rotating_logger", sink);
}

std::shared_ptr<spdlog::logger> daily_logger(
        jboolean &isSync,
        const std::string &path,
        jint &hour,
        jint &minute,
        const std::string &encryptionKey
) {
    uint8_t key_bytes[32];
    const uint8_t *key = parse_key(encryptionKey, key_bytes);

    std::shared_ptr<spdlog::sinks::sink> sink;
    if (isSync) {
        sink = std::make_shared<sinks::encrypted_daily_sink_st>(
                path, (int) hour, (int) minute, key, key ? 32 : 0);
    } else {
        sink = std::make_shared<sinks::encrypted_daily_sink_mt>(
                path, (int) hour, (int) minute, key, key ? 32 : 0);
    }
    return std::make_shared<spdlog::logger>("daily_logger", sink);
}
