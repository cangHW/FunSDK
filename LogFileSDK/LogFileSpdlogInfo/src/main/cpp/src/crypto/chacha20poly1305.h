#ifndef CHACHA20POLY1305_H
#define CHACHA20POLY1305_H

#include <cstdint>
#include <cstddef>
#include <cstring>

namespace crypto {

static inline uint32_t rotl32(uint32_t v, int n) {
    return (v << n) | (v >> (32 - n));
}

static inline uint32_t load32_le(const uint8_t *p) {
    return (uint32_t)p[0] | ((uint32_t)p[1] << 8) |
           ((uint32_t)p[2] << 16) | ((uint32_t)p[3] << 24);
}

static inline void store32_le(uint8_t *p, uint32_t v) {
    p[0] = (uint8_t)v;
    p[1] = (uint8_t)(v >> 8);
    p[2] = (uint8_t)(v >> 16);
    p[3] = (uint8_t)(v >> 24);
}

static inline uint64_t load64_le(const uint8_t *p) {
    return (uint64_t)p[0] | ((uint64_t)p[1] << 8) |
           ((uint64_t)p[2] << 16) | ((uint64_t)p[3] << 24) |
           ((uint64_t)p[4] << 32) | ((uint64_t)p[5] << 40) |
           ((uint64_t)p[6] << 48) | ((uint64_t)p[7] << 56);
}

static inline void store64_le(uint8_t *p, uint64_t v) {
    for (int i = 0; i < 8; i++) {
        p[i] = (uint8_t)(v >> (i * 8));
    }
}

#define QR(a, b, c, d) \
    a += b; d ^= a; d = rotl32(d, 16); \
    c += d; b ^= c; b = rotl32(b, 12); \
    a += b; d ^= a; d = rotl32(d, 8);  \
    c += d; b ^= c; b = rotl32(b, 7);

static void chacha20_block(uint32_t out[16], const uint32_t in[16]) {
    uint32_t x[16];
    memcpy(x, in, 64);

    for (int i = 0; i < 10; i++) {
        QR(x[0], x[4], x[8],  x[12])
        QR(x[1], x[5], x[9],  x[13])
        QR(x[2], x[6], x[10], x[14])
        QR(x[3], x[7], x[11], x[15])
        QR(x[0], x[5], x[10], x[15])
        QR(x[1], x[6], x[11], x[12])
        QR(x[2], x[7], x[8],  x[13])
        QR(x[3], x[4], x[9],  x[14])
    }

    for (int i = 0; i < 16; i++) {
        out[i] = x[i] + in[i];
    }
}

#undef QR

static void chacha20_encrypt(uint8_t *out, const uint8_t *in, size_t len,
                             const uint8_t key[32], const uint8_t nonce[12],
                             uint32_t counter) {
    uint32_t state[16];
    state[0] = 0x61707865;
    state[1] = 0x3320646e;
    state[2] = 0x79622d32;
    state[3] = 0x6b206574;
    for (int i = 0; i < 8; i++) {
        state[4 + i] = load32_le(key + i * 4);
    }
    state[12] = counter;
    state[13] = load32_le(nonce);
    state[14] = load32_le(nonce + 4);
    state[15] = load32_le(nonce + 8);

    uint32_t block[16];
    size_t off = 0;

    while (off < len) {
        chacha20_block(block, state);
        state[12]++;

        size_t chunk = len - off;
        if (chunk > 64) chunk = 64;

        const uint8_t *keystream = (const uint8_t *)block;
        for (size_t i = 0; i < chunk; i++) {
            out[off + i] = in[off + i] ^ keystream[i];
        }
        off += chunk;
    }
}

struct Poly1305 {
    uint32_t r[5];
    uint32_t h[5];
    uint32_t pad[4];
    uint8_t buf[16];
    size_t buf_len;

    void init(const uint8_t key[32]) {
        r[0] = (load32_le(key + 0)) & 0x3ffffff;
        r[1] = (load32_le(key + 3) >> 2) & 0x3ffff03;
        r[2] = (load32_le(key + 6) >> 4) & 0x3ffc0ff;
        r[3] = (load32_le(key + 9) >> 6) & 0x3f03fff;
        r[4] = (load32_le(key + 12) >> 8) & 0x00fffff;

        h[0] = h[1] = h[2] = h[3] = h[4] = 0;

        pad[0] = load32_le(key + 16);
        pad[1] = load32_le(key + 20);
        pad[2] = load32_le(key + 24);
        pad[3] = load32_le(key + 28);

        buf_len = 0;
    }

    void process_block(const uint8_t *msg, uint32_t hibit) {
        uint32_t r0 = r[0], r1 = r[1], r2 = r[2], r3 = r[3], r4 = r[4];
        uint32_t s1 = r1 * 5, s2 = r2 * 5, s3 = r3 * 5, s4 = r4 * 5;
        uint32_t h0 = h[0], h1 = h[1], h2 = h[2], h3 = h[3], h4 = h[4];

        h0 += (load32_le(msg + 0)) & 0x3ffffff;
        h1 += (load32_le(msg + 3) >> 2) & 0x3ffffff;
        h2 += (load32_le(msg + 6) >> 4) & 0x3ffffff;
        h3 += (load32_le(msg + 9) >> 6) & 0x3ffffff;
        h4 += (load32_le(msg + 12) >> 8) | hibit;

        uint64_t d0 = (uint64_t)h0*r0 + (uint64_t)h1*s4 + (uint64_t)h2*s3 + (uint64_t)h3*s2 + (uint64_t)h4*s1;
        uint64_t d1 = (uint64_t)h0*r1 + (uint64_t)h1*r0 + (uint64_t)h2*s4 + (uint64_t)h3*s3 + (uint64_t)h4*s2;
        uint64_t d2 = (uint64_t)h0*r2 + (uint64_t)h1*r1 + (uint64_t)h2*r0 + (uint64_t)h3*s4 + (uint64_t)h4*s3;
        uint64_t d3 = (uint64_t)h0*r3 + (uint64_t)h1*r2 + (uint64_t)h2*r1 + (uint64_t)h3*r0 + (uint64_t)h4*s4;
        uint64_t d4 = (uint64_t)h0*r4 + (uint64_t)h1*r3 + (uint64_t)h2*r2 + (uint64_t)h3*r1 + (uint64_t)h4*r0;

        uint32_t c;
        c = (uint32_t)(d0 >> 26); h0 = (uint32_t)d0 & 0x3ffffff; d1 += c;
        c = (uint32_t)(d1 >> 26); h1 = (uint32_t)d1 & 0x3ffffff; d2 += c;
        c = (uint32_t)(d2 >> 26); h2 = (uint32_t)d2 & 0x3ffffff; d3 += c;
        c = (uint32_t)(d3 >> 26); h3 = (uint32_t)d3 & 0x3ffffff; d4 += c;
        c = (uint32_t)(d4 >> 26); h4 = (uint32_t)d4 & 0x3ffffff; h0 += c * 5;
        c = h0 >> 26; h0 &= 0x3ffffff; h1 += c;

        h[0] = h0; h[1] = h1; h[2] = h2; h[3] = h3; h[4] = h4;
    }

    void update(const uint8_t *msg, size_t len) {
        if (buf_len > 0) {
            size_t want = 16 - buf_len;
            if (len < want) {
                memcpy(buf + buf_len, msg, len);
                buf_len += len;
                return;
            }
            memcpy(buf + buf_len, msg, want);
            process_block(buf, 1 << 24);
            msg += want;
            len -= want;
            buf_len = 0;
        }

        while (len >= 16) {
            process_block(msg, 1 << 24);
            msg += 16;
            len -= 16;
        }

        if (len > 0) {
            memcpy(buf, msg, len);
            buf_len = len;
        }
    }

    void finish(uint8_t tag[16]) {
        if (buf_len > 0) {
            uint8_t final_buf[16] = {0};
            memcpy(final_buf, buf, buf_len);
            final_buf[buf_len] = 1;
            process_block(final_buf, 0);
        }

        uint32_t h0 = h[0], h1 = h[1], h2 = h[2], h3 = h[3], h4 = h[4];

        uint32_t c;
        c = h1 >> 26; h1 &= 0x3ffffff; h2 += c;
        c = h2 >> 26; h2 &= 0x3ffffff; h3 += c;
        c = h3 >> 26; h3 &= 0x3ffffff; h4 += c;
        c = h4 >> 26; h4 &= 0x3ffffff; h0 += c * 5;
        c = h0 >> 26; h0 &= 0x3ffffff; h1 += c;

        uint32_t g0 = h0 + 5; c = g0 >> 26; g0 &= 0x3ffffff;
        uint32_t g1 = h1 + c; c = g1 >> 26; g1 &= 0x3ffffff;
        uint32_t g2 = h2 + c; c = g2 >> 26; g2 &= 0x3ffffff;
        uint32_t g3 = h3 + c; c = g3 >> 26; g3 &= 0x3ffffff;
        uint32_t g4 = h4 + c - (1 << 26);

        uint32_t mask = (g4 >> 31) - 1;
        g0 &= mask; g1 &= mask; g2 &= mask; g3 &= mask; g4 &= mask;
        mask = ~mask;
        h0 = (h0 & mask) | g0;
        h1 = (h1 & mask) | g1;
        h2 = (h2 & mask) | g2;
        h3 = (h3 & mask) | g3;
        h4 = (h4 & mask) | g4;

        uint64_t f;
        f = (uint64_t)(h0 | (h1 << 26)) + pad[0];             store32_le(tag, (uint32_t)f);
        f = (uint64_t)((h1 >> 6) | (h2 << 20)) + pad[1] + (f >> 32); store32_le(tag + 4, (uint32_t)f);
        f = (uint64_t)((h2 >> 12) | (h3 << 14)) + pad[2] + (f >> 32); store32_le(tag + 8, (uint32_t)f);
        f = (uint64_t)((h3 >> 18) | (h4 << 8)) + pad[3] + (f >> 32); store32_le(tag + 12, (uint32_t)f);
    }
};

static const size_t CHACHA20POLY1305_KEY_SIZE = 32;
static const size_t CHACHA20POLY1305_NONCE_SIZE = 12;
static const size_t CHACHA20POLY1305_TAG_SIZE = 16;

static void poly1305_pad_len(Poly1305 &poly, size_t len) {
    if (len % 16 != 0) {
        uint8_t zeros[16] = {0};
        poly.update(zeros, 16 - (len % 16));
    }
}

static void chacha20poly1305_encrypt(
        uint8_t *ciphertext,
        uint8_t tag[16],
        const uint8_t *plaintext, size_t plaintext_len,
        const uint8_t *aad, size_t aad_len,
        const uint8_t nonce[12],
        const uint8_t key[32]) {

    // Generate Poly1305 key (first 32 bytes of ChaCha20 block 0)
    uint8_t poly_key[64] = {0};
    chacha20_encrypt(poly_key, poly_key, 64, key, nonce, 0);

    // Encrypt plaintext with counter starting at 1
    chacha20_encrypt(ciphertext, plaintext, plaintext_len, key, nonce, 1);

    // Compute tag: Poly1305(aad || pad || ciphertext || pad || len_aad || len_ct)
    Poly1305 poly;
    poly.init(poly_key);

    if (aad_len > 0) {
        poly.update(aad, aad_len);
        poly1305_pad_len(poly, aad_len);
    }

    poly.update(ciphertext, plaintext_len);
    poly1305_pad_len(poly, plaintext_len);

    uint8_t lens[16];
    store64_le(lens, aad_len);
    store64_le(lens + 8, plaintext_len);
    poly.update(lens, 16);

    poly.finish(tag);
}

static bool chacha20poly1305_decrypt(
        uint8_t *plaintext,
        const uint8_t *ciphertext, size_t ciphertext_len,
        const uint8_t tag[16],
        const uint8_t *aad, size_t aad_len,
        const uint8_t nonce[12],
        const uint8_t key[32]) {

    // Generate Poly1305 key
    uint8_t poly_key[64] = {0};
    chacha20_encrypt(poly_key, poly_key, 64, key, nonce, 0);

    // Verify tag first
    Poly1305 poly;
    poly.init(poly_key);

    if (aad_len > 0) {
        poly.update(aad, aad_len);
        poly1305_pad_len(poly, aad_len);
    }

    poly.update(ciphertext, ciphertext_len);
    poly1305_pad_len(poly, ciphertext_len);

    uint8_t lens[16];
    store64_le(lens, aad_len);
    store64_le(lens + 8, ciphertext_len);
    poly.update(lens, 16);

    uint8_t computed_tag[16];
    poly.finish(computed_tag);

    // Constant-time comparison
    uint8_t diff = 0;
    for (int i = 0; i < 16; i++) {
        diff |= computed_tag[i] ^ tag[i];
    }
    if (diff != 0) return false;

    // Decrypt
    chacha20_encrypt(plaintext, ciphertext, ciphertext_len, key, nonce, 1);
    return true;
}

} // namespace crypto

#endif // CHACHA20POLY1305_H
