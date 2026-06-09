#include <jni.h>
#include <string>
#include <cstdio>
#include <vector>
#include "h/JniCallUtils.h"
#include "crypto/chacha20poly1305.h"
#include "h/EncryptedFileSink.h"

static bool hex_to_bytes(const std::string &hex, uint8_t *out, size_t out_len) {
    if (hex.size() != out_len * 2) return false;
    for (size_t i = 0; i < out_len; i++) {
        char hi = hex[i * 2];
        char lo = hex[i * 2 + 1];
        auto hex_val = [](char c) -> int {
            if (c >= '0' && c <= '9') return c - '0';
            if (c >= 'a' && c <= 'f') return c - 'a' + 10;
            if (c >= 'A' && c <= 'F') return c - 'A' + 10;
            return -1;
        };
        int h = hex_val(hi);
        int l = hex_val(lo);
        if (h < 0 || l < 0) return false;
        out[i] = (uint8_t) ((h << 4) | l);
    }
    return true;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileDecrypt_nativeDecryptFile(
        JNIEnv *env,
        jobject obj,
        jstring inputPath,
        jstring outputPath,
        jstring hexKey
) {
    const char *input = env->GetStringUTFChars(inputPath, nullptr);
    const char *output = env->GetStringUTFChars(outputPath, nullptr);
    const char *keyStr = env->GetStringUTFChars(hexKey, nullptr);

    if (!input || !output || !keyStr) {
        if (input) env->ReleaseStringUTFChars(inputPath, input);
        if (output) env->ReleaseStringUTFChars(outputPath, output);
        if (keyStr) env->ReleaseStringUTFChars(hexKey, keyStr);
        return false;
    }

    std::string keyHex(keyStr);
    uint8_t key[32];
    if (!hex_to_bytes(keyHex, key, 32)) {
        env->ReleaseStringUTFChars(inputPath, input);
        env->ReleaseStringUTFChars(outputPath, output);
        env->ReleaseStringUTFChars(hexKey, keyStr);
        return false;
    }

    FILE *fin = fopen(input, "rb");
    FILE *fout = fopen(output, "wb");

    env->ReleaseStringUTFChars(inputPath, input);
    env->ReleaseStringUTFChars(outputPath, output);
    env->ReleaseStringUTFChars(hexKey, keyStr);

    if (!fin || !fout) {
        if (fin) fclose(fin);
        if (fout) fclose(fout);
        return false;
    }

    // Read header
    sinks::ElogHeader hdr = {};
    if (fread(&hdr, 1, sinks::ELOG_HEADER_SIZE, fin) != sinks::ELOG_HEADER_SIZE ||
        hdr.magic != sinks::ELOG_MAGIC) {
        fclose(fin);
        fclose(fout);
        return false;
    }

    bool encrypted = (hdr.flags & sinks::ELOG_FLAG_ENCRYPTED) != 0;
    if (!encrypted) {
        // Plain text file, copy remaining content
        uint8_t buf[4096];
        size_t n;
        while ((n = fread(buf, 1, sizeof(buf), fin)) > 0) {
            fwrite(buf, 1, n, fout);
        }
        fclose(fin);
        fclose(fout);
        return true;
    }

    // Decrypt line by line
    while (true) {
        uint32_t record_len;
        if (fread(&record_len, 4, 1, fin) != 1) break;

        std::vector<uint8_t> record(record_len);
        if (fread(record.data(), 1, record_len, fin) != record_len) break;

        if (record_len < 12 + 16) continue;

        uint8_t *nonce = record.data();
        size_t ciphertext_len = record_len - 12 - 16;
        uint8_t *ciphertext = record.data() + 12;
        uint8_t *tag = record.data() + 12 + ciphertext_len;

        std::vector<uint8_t> plaintext(ciphertext_len);
        bool ok = crypto::chacha20poly1305_decrypt(
                plaintext.data(),
                ciphertext, ciphertext_len,
                tag,
                nullptr, 0,
                nonce, key);

        if (ok) {
            fwrite(plaintext.data(), 1, ciphertext_len, fout);
        }
    }

    fclose(fin);
    fclose(fout);
    return true;
}
