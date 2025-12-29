#ifndef CHACHA20_H
#define CHACHA20_H

#include <stdint.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

#define CHACHA20_KEY_SIZE 32
#define CHACHA20_NONCE_SIZE 12
#define CHACHA20_BLOCK_SIZE 64

/**
 * ChaCha20 加密/解密函数
 * 
 * @param key 32字节密钥
 * @param nonce 12字节随机数
 * @param counter 计数器（通常从0开始）
 * @param input 输入数据
 * @param input_len 输入数据长度
 * @param output 输出缓冲区（必须与输入长度相同）
 * @return 0表示成功，-1表示失败
 */
int chacha20_encrypt(
    const uint8_t key[CHACHA20_KEY_SIZE],
    const uint8_t nonce[CHACHA20_NONCE_SIZE],
    uint32_t counter,
    const uint8_t *input,
    size_t input_len,
    uint8_t *output
);

/**
 * ChaCha20 解密函数（与加密相同）
 */
#define chacha20_decrypt chacha20_encrypt

/**
 * 生成随机 nonce
 * 
 * @param nonce 输出缓冲区（12字节）
 * @return 0表示成功，-1表示失败
 */
int chacha20_generate_nonce(uint8_t nonce[CHACHA20_NONCE_SIZE]);

/**
 * 从密钥派生 nonce（用于确定性加密）
 * 
 * @param key 32字节密钥
 * @param data 用于派生 nonce 的数据
 * @param data_len 数据长度
 * @param nonce 输出缓冲区（12字节）
 * @return 0表示成功，-1表示失败
 */
int chacha20_derive_nonce(
    const uint8_t key[CHACHA20_KEY_SIZE],
    const uint8_t *data,
    size_t data_len,
    uint8_t nonce[CHACHA20_NONCE_SIZE]
);

#ifdef __cplusplus
}
#endif

#endif // CHACHA20_H
