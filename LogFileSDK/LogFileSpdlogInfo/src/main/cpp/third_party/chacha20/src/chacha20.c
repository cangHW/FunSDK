#include "chacha20.h"
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <fcntl.h>

// ChaCha20 内部状态
typedef struct {
    uint32_t state[16];
} chacha20_ctx_t;

// 左旋转函数
static inline uint32_t rotl32(uint32_t x, int n) {
    return (x << n) | (x >> (32 - n));
}

// ChaCha20 四分之一轮函数
static void chacha20_quarter_round(uint32_t *a, uint32_t *b, uint32_t *c, uint32_t *d) {
    *a += *b; *d ^= *a; *d = rotl32(*d, 16);
    *c += *d; *b ^= *c; *b = rotl32(*b, 12);
    *a += *b; *d ^= *a; *d = rotl32(*d, 8);
    *c += *d; *b ^= *c; *b = rotl32(*b, 7);
}

// ChaCha20 核心函数
static void chacha20_core(chacha20_ctx_t *ctx, uint8_t *output) {
    uint32_t *state = ctx->state;
    uint32_t working_state[16];
    
    // 复制状态到工作数组
    memcpy(working_state, state, sizeof(working_state));
    
    // 20轮 ChaCha20
    for (int i = 0; i < 10; i++) {
        // 列轮
        chacha20_quarter_round(&working_state[0], &working_state[4], &working_state[8],  &working_state[12]);
        chacha20_quarter_round(&working_state[1], &working_state[5], &working_state[9],  &working_state[13]);
        chacha20_quarter_round(&working_state[2], &working_state[6], &working_state[10], &working_state[14]);
        chacha20_quarter_round(&working_state[3], &working_state[7], &working_state[11], &working_state[15]);
        
        // 对角轮
        chacha20_quarter_round(&working_state[0], &working_state[5], &working_state[10], &working_state[15]);
        chacha20_quarter_round(&working_state[1], &working_state[6], &working_state[11], &working_state[12]);
        chacha20_quarter_round(&working_state[2], &working_state[7], &working_state[8],  &working_state[13]);
        chacha20_quarter_round(&working_state[3], &working_state[4], &working_state[9],  &working_state[14]);
    }
    
    // 添加原始状态
    for (int i = 0; i < 16; i++) {
        working_state[i] += state[i];
    }
    
    // 转换为字节数组
    for (int i = 0; i < 16; i++) {
        uint32_t word = working_state[i];
        output[i * 4 + 0] = (uint8_t)(word >> 0);
        output[i * 4 + 1] = (uint8_t)(word >> 8);
        output[i * 4 + 2] = (uint8_t)(word >> 16);
        output[i * 4 + 3] = (uint8_t)(word >> 24);
    }
}

// 初始化 ChaCha20 状态
static void chacha20_init(chacha20_ctx_t *ctx, 
                          const uint8_t key[CHACHA20_KEY_SIZE],
                          const uint8_t nonce[CHACHA20_NONCE_SIZE],
                          uint32_t counter) {
    // 常量
    ctx->state[0] = 0x61707865;
    ctx->state[1] = 0x3320646e;
    ctx->state[2] = 0x79622d32;
    ctx->state[3] = 0x6b206574;
    
    // 密钥
    for (int i = 0; i < 8; i++) {
        ctx->state[4 + i] = ((uint32_t)key[i * 4 + 0] << 0)  |
                           ((uint32_t)key[i * 4 + 1] << 8)  |
                           ((uint32_t)key[i * 4 + 2] << 16) |
                           ((uint32_t)key[i * 4 + 3] << 24);
    }
    
    // 计数器
    ctx->state[12] = counter;
    
    // 随机数
    for (int i = 0; i < 3; i++) {
        ctx->state[13 + i] = ((uint32_t)nonce[i * 4 + 0] << 0)  |
                            ((uint32_t)nonce[i * 4 + 1] << 8)  |
                            ((uint32_t)nonce[i * 4 + 2] << 16) |
                            ((uint32_t)nonce[i * 4 + 3] << 24);
    }
}

int chacha20_encrypt(const uint8_t key[CHACHA20_KEY_SIZE],
                     const uint8_t nonce[CHACHA20_NONCE_SIZE],
                     uint32_t counter,
                     const uint8_t *input,
                     size_t input_len,
                     uint8_t *output) {
    if (!key || !nonce || !input || !output || input_len == 0) {
        return -1;
    }
    
    chacha20_ctx_t ctx;
    uint8_t keystream[CHACHA20_BLOCK_SIZE];
    size_t remaining = input_len;
    size_t offset = 0;
    
    while (remaining > 0) {
        // 初始化状态
        chacha20_init(&ctx, key, nonce, counter);
        
        // 生成密钥流
        chacha20_core(&ctx, keystream);
        
        // 处理当前块
        size_t block_size = (remaining < CHACHA20_BLOCK_SIZE) ? remaining : CHACHA20_BLOCK_SIZE;
        for (size_t i = 0; i < block_size; i++) {
            output[offset + i] = input[offset + i] ^ keystream[i];
        }
        
        offset += block_size;
        remaining -= block_size;
        counter++;
    }
    
    return 0;
}

int chacha20_generate_nonce(uint8_t nonce[CHACHA20_NONCE_SIZE]) {
    if (!nonce) {
        return -1;
    }
    
    // 尝试从 /dev/urandom 读取密码学安全的随机数
    int urandom_fd = open("/dev/urandom", O_RDONLY);
    if (urandom_fd >= 0) {
        ssize_t bytes_read = read(urandom_fd, nonce, CHACHA20_NONCE_SIZE);
        close(urandom_fd);
        
        if (bytes_read == CHACHA20_NONCE_SIZE) {
            return 0; // 成功从 /dev/urandom 读取
        }
    }
    
    // 降级方案：使用多种熵源生成更安全的随机数
    uint32_t timestamp = (uint32_t)time(NULL);
    uint32_t pid = (uint32_t)getpid();
    uint32_t counter = 0;
    
    // 使用更复杂的随机数生成算法
    for (int i = 0; i < CHACHA20_NONCE_SIZE; i += 4) {
        // 组合多种熵源
        uint32_t entropy = timestamp ^ pid ^ counter;
        
        // 使用线性同余生成器（LCG）增强随机性
        entropy = entropy * 1103515245 + 12345;
        entropy ^= (entropy >> 16);
        entropy *= 0x85ebca6b;
        entropy ^= (entropy >> 13);
        entropy *= 0xc2b2ae35;
        entropy ^= (entropy >> 16);
        
        // 存储到 nonce 中
        int remaining = CHACHA20_NONCE_SIZE - i;
        int bytes_to_write = (remaining >= 4) ? 4 : remaining;
        
        for (int j = 0; j < bytes_to_write; j++) {
            nonce[i + j] = (uint8_t)(entropy >> (j * 8));
        }
        
        counter++;
        timestamp = timestamp * 7 + 13; // 更新时间戳
    }
    
    return 0;
}

int chacha20_derive_nonce(const uint8_t key[CHACHA20_KEY_SIZE],
                          const uint8_t *data,
                          size_t data_len,
                          uint8_t nonce[CHACHA20_NONCE_SIZE]) {
    if (!key || !data || !nonce || data_len == 0) {
        return -1;
    }
    
    // 简单的 HMAC 风格派生
    uint32_t hash = 0;
    for (size_t i = 0; i < data_len; i++) {
        hash = hash * 31 + data[i];
    }
    
    // 使用密钥和哈希值生成 nonce
    for (int i = 0; i < 3; i++) {
        uint32_t value = hash ^ ((uint32_t)key[i * 4 + 0] << 0)  |
                                ((uint32_t)key[i * 4 + 1] << 8)  |
                                ((uint32_t)key[i * 4 + 2] << 16) |
                                ((uint32_t)key[i * 4 + 3] << 24);
        
        nonce[i * 4 + 0] = (uint8_t)(value >> 0);
        nonce[i * 4 + 1] = (uint8_t)(value >> 8);
        nonce[i * 4 + 2] = (uint8_t)(value >> 16);
        nonce[i * 4 + 3] = (uint8_t)(value >> 24);
    }
    
    return 0;
}
