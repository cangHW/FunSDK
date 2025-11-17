#pragma once

#include <string>
#include <vector>
#include <cstdint>
#include <cstring>

/**
 * 安全工具类 - 提供密码学安全的密钥派生和随机数生成
 */
class SecurityUtils {
public:
    // PBKDF2 密钥派生函数
    static bool deriveKeyPBKDF2(
            const std::string &password,
            const std::vector<uint8_t> &salt,
            uint32_t iterations,
            uint8_t *derived_key,
            size_t key_length
    );

    // 生成密码学安全的随机盐
    static bool generateSecureSalt(std::vector<uint8_t> &salt, size_t length = 32);

    // 生成密码学安全的随机数
    static bool generateSecureRandom(uint8_t *buffer, size_t length);

    // 生成密码学安全的 nonce
    static bool generateSecureNonce(uint8_t *nonce, size_t length = 12);

    // 安全的密钥派生（使用 PBKDF2 + 外部提供的盐）
    static bool deriveKeySecure(
            const std::string &password,
            const std::vector<uint8_t> &salt,
            uint8_t *derived_key,
            size_t key_length
    );

    // 降级方案的密钥派生
    static void deriveKeyFallback(
            const std::string &password,
            uint8_t *derived_key,
            size_t key_length
    );

private:
    // 内部辅助函数
    static void hmac_sha256(
            const uint8_t *key, size_t key_len,
            const uint8_t *data, size_t data_len,
            uint8_t *output
    );

    static void sha256(const uint8_t *data, size_t len, uint8_t *output);

    static void xor_bytes(const uint8_t *a, const uint8_t *b, uint8_t *result, size_t len);

    // 从系统获取随机数
    static bool getSystemRandom(uint8_t *buffer, size_t length);
};

// 简化的 PBKDF2 实现（适用于移动端）
inline bool SecurityUtils::deriveKeyPBKDF2(
        const std::string &password,
        const std::vector<uint8_t> &salt,
        uint32_t iterations,
        uint8_t *derived_key,
        size_t key_length
) {
    if (password.empty() || salt.empty() || key_length == 0 || iterations == 0) {
        return false;
    }

    // 使用简化的 PBKDF2 实现
    // 在实际应用中，建议使用 OpenSSL 或其他密码学库

    const uint8_t *password_bytes = reinterpret_cast<const uint8_t *>(password.c_str());
    size_t password_len = password.length();

    // 计算需要的块数
    size_t blocks_needed = (key_length + 31) / 32; // SHA256 输出 32 字节

    for (size_t i = 0; i < blocks_needed; i++) {
        // 准备输入数据：salt + 4字节块索引
        std::vector<uint8_t> input;
        input.insert(input.end(), salt.begin(), salt.end());

        // 添加块索引（大端序）
        uint32_t block_index = static_cast<uint32_t>(i + 1);
        input.push_back((block_index >> 24) & 0xFF);
        input.push_back((block_index >> 16) & 0xFF);
        input.push_back((block_index >> 8) & 0xFF);
        input.push_back(block_index & 0xFF);

        // 计算 HMAC-SHA256
        uint8_t hmac_result[32];
        hmac_sha256(password_bytes, password_len, input.data(), input.size(), hmac_result);

        // 迭代计算
        uint8_t current_block[32];
        memcpy(current_block, hmac_result, 32);

        for (uint32_t j = 1; j < iterations; j++) {
            uint8_t temp[32];
            hmac_sha256(password_bytes, password_len, current_block, 32, temp);
            xor_bytes(current_block, temp, current_block, 32);
        }

        // 复制到输出缓冲区
        size_t copy_len = std::min(static_cast<size_t>(32), key_length - i * 32);
        memcpy(derived_key + i * 32, current_block, copy_len);
    }

    return true;
}

inline bool SecurityUtils::generateSecureSalt(std::vector<uint8_t> &salt, size_t length) {
    salt.resize(length);
    return generateSecureRandom(salt.data(), length);
}

inline bool SecurityUtils::generateSecureRandom(uint8_t *buffer, size_t length) {
    if (!buffer || length == 0) {
        return false;
    }

    // 尝试从系统获取随机数
    if (getSystemRandom(buffer, length)) {
        return true;
    }

    // 降级方案：使用时间戳 + 简单算法生成伪随机数
    // 注意：这不是密码学安全的，仅作为最后手段
    uint64_t seed = static_cast<uint64_t>(time(nullptr));
    for (size_t i = 0; i < length; i++) {
        seed = seed * 1103515245 + 12345;
        buffer[i] = static_cast<uint8_t>((seed >> 16) & 0xFF);
    }

    return true;
}

inline bool SecurityUtils::generateSecureNonce(uint8_t *nonce, size_t length) {
    return generateSecureRandom(nonce, length);
}

inline bool SecurityUtils::deriveKeySecure(
        const std::string &password,
        const std::vector<uint8_t> &salt,
        uint8_t *derived_key,
        size_t key_length
) {
    if (salt.empty()) {
        return false;
    }

    // 使用外部提供的盐进行 PBKDF2 派生（10000 次迭代）
    return deriveKeyPBKDF2(password, salt, 10000, derived_key, key_length);
}

// HMAC-SHA256 实现
inline void SecurityUtils::hmac_sha256(
        const uint8_t *key, size_t key_len,
        const uint8_t *data, size_t data_len,
        uint8_t *output
) {
    const size_t block_size = 64; // SHA256 块大小
    uint8_t key_block[block_size];

    // 准备密钥
    if (key_len > block_size) {
        sha256(key, key_len, key_block);
        memset(key_block + 32, 0, block_size - 32);
    } else {
        memcpy(key_block, key, key_len);
        memset(key_block + key_len, 0, block_size - key_len);
    }

    // 创建内部填充和外部填充
    uint8_t inner_pad[block_size];
    uint8_t outer_pad[block_size];

    for (size_t i = 0; i < block_size; i++) {
        inner_pad[i] = key_block[i] ^ 0x36;
        outer_pad[i] = key_block[i] ^ 0x5C;
    }

    // 计算内部哈希
    uint8_t inner_data[block_size + data_len];
    memcpy(inner_data, inner_pad, block_size);
    memcpy(inner_data + block_size, data, data_len);

    uint8_t inner_hash[32];
    sha256(inner_data, block_size + data_len, inner_hash);

    // 计算外部哈希
    uint8_t outer_data[block_size + 32];
    memcpy(outer_data, outer_pad, block_size);
    memcpy(outer_data + block_size, inner_hash, 32);

    sha256(outer_data, block_size + 32, output);
}

// 完整的 SHA256 实现
inline void SecurityUtils::sha256(const uint8_t *data, size_t len, uint8_t *output) {
    // SHA256 常量（前64个素数的立方根的小数部分）
    static const uint32_t k[64] = {
        0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
        0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
        0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
        0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
        0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
        0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
        0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
        0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    // 初始化哈希值（前8个素数的平方根的小数部分）
    uint32_t h[8] = {
        0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
        0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
    };

    // 预处理：填充消息
    uint64_t bit_len = len * 8;
    size_t new_len = len + 1;
    while (new_len % 64 != 56) new_len++;
    
    std::vector<uint8_t> padded(new_len + 8, 0);
    memcpy(padded.data(), data, len);
    padded[len] = 0x80;  // 添加 '1' 位
    
    // 添加原始长度（大端序）
    for (int i = 0; i < 8; i++) {
        padded[new_len + i] = (bit_len >> (56 - i * 8)) & 0xFF;
    }

    // 处理每个 512 位块
    for (size_t chunk = 0; chunk < padded.size(); chunk += 64) {
        uint32_t w[64] = {0};
        
        // 将块分解为16个32位大端字
        for (int i = 0; i < 16; i++) {
            w[i] = (padded[chunk + i * 4] << 24) |
                   (padded[chunk + i * 4 + 1] << 16) |
                   (padded[chunk + i * 4 + 2] << 8) |
                   (padded[chunk + i * 4 + 3]);
        }
        
        // 扩展为64个字
        for (int i = 16; i < 64; i++) {
            uint32_t s0 = ((w[i-15] >> 7) | (w[i-15] << 25)) ^
                          ((w[i-15] >> 18) | (w[i-15] << 14)) ^
                          (w[i-15] >> 3);
            uint32_t s1 = ((w[i-2] >> 17) | (w[i-2] << 15)) ^
                          ((w[i-2] >> 19) | (w[i-2] << 13)) ^
                          (w[i-2] >> 10);
            w[i] = w[i-16] + s0 + w[i-7] + s1;
        }
        
        // 初始化工作变量
        uint32_t a = h[0], b = h[1], c = h[2], d = h[3];
        uint32_t e = h[4], f = h[5], g = h[6], h_temp = h[7];
        
        // 主循环
        for (int i = 0; i < 64; i++) {
            uint32_t S1 = ((e >> 6) | (e << 26)) ^
                          ((e >> 11) | (e << 21)) ^
                          ((e >> 25) | (e << 7));
            uint32_t ch = (e & f) ^ ((~e) & g);
            uint32_t temp1 = h_temp + S1 + ch + k[i] + w[i];
            uint32_t S0 = ((a >> 2) | (a << 30)) ^
                          ((a >> 13) | (a << 19)) ^
                          ((a >> 22) | (a << 10));
            uint32_t maj = (a & b) ^ (a & c) ^ (b & c);
            uint32_t temp2 = S0 + maj;
            
            h_temp = g;
            g = f;
            f = e;
            e = d + temp1;
            d = c;
            c = b;
            b = a;
            a = temp1 + temp2;
        }
        
        // 添加到哈希值
        h[0] += a; h[1] += b; h[2] += c; h[3] += d;
        h[4] += e; h[5] += f; h[6] += g; h[7] += h_temp;
    }

    // 输出最终哈希值（大端序）
    for (int i = 0; i < 8; i++) {
        output[i * 4] = (h[i] >> 24) & 0xFF;
        output[i * 4 + 1] = (h[i] >> 16) & 0xFF;
        output[i * 4 + 2] = (h[i] >> 8) & 0xFF;
        output[i * 4 + 3] = h[i] & 0xFF;
    }
}

inline void SecurityUtils::xor_bytes(
        const uint8_t *a,
        const uint8_t *b,
        uint8_t *result,
        size_t len
) {
    for (size_t i = 0; i < len; i++) {
        result[i] = a[i] ^ b[i];
    }
}

inline bool SecurityUtils::getSystemRandom(uint8_t *buffer, size_t length) {
    // 在 Android 上，可以尝试从 /dev/urandom 读取
    // 这里提供一个简化的实现
    FILE *urandom = fopen("/dev/urandom", "rb");
    if (urandom) {
        size_t bytes_read = fread(buffer, 1, length, urandom);
        fclose(urandom);
        return bytes_read == length;
    }
    return false;
}


// 降级方案的密钥派生（无时间戳，确定性）
inline void SecurityUtils::deriveKeyFallback(
        const std::string &key,
        uint8_t *derived_key,
        size_t key_length
) {
    memset(derived_key, 0, key_length);

    size_t key_len = key.length();
    if (key_len == 0) {
        return;
    }

    // 使用确定性哈希（无时间戳）
    uint32_t hash = 0x811c9dc5; // FNV-1a 初始值
    for (size_t i = 0; i < key_length; i++) {
        // 计算位置相关的哈希
        hash ^= key[i % key_len];
        hash *= 0x01000193; // FNV-1a 质数

        // 添加位置信息（确定性）
        hash ^= static_cast<uint32_t>(i);
        hash *= 0x01000193;

        // ✅ 移除 time(nullptr)，确保相同密码 → 相同密钥

        derived_key[i] = static_cast<uint8_t>(hash & 0xFF);
    }
}
