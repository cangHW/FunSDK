#include <jni.h>
#include "h/LogFileDecompress.h"

namespace fs = std::__fs::filesystem;

// JNI接口实现
extern "C" {

// 解压缩日志文件的JNI接口
JNIEXPORT jboolean JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileDecompress_decompressLogFile(
        JNIEnv *env,
        jobject /* this */,
        jstring input_file_path,
        jstring output_file_path,
        jstring encryption_key
) {
    const char *input_path = env->GetStringUTFChars(input_file_path, nullptr);
    const char *output_path = env->GetStringUTFChars(output_file_path, nullptr);
    const char *enc_key = env->GetStringUTFChars(encryption_key, nullptr);

    LogFileDecompressor decompressor(enc_key ? enc_key : "");
    bool result = decompressor.decompressLogFile(input_path, output_path);

    env->ReleaseStringUTFChars(input_file_path, input_path);
    env->ReleaseStringUTFChars(output_file_path, output_path);
    env->ReleaseStringUTFChars(encryption_key, enc_key);

    return result;
}

// 🔧 新增：解压缩指定目录下所有日志文件的JNI接口
JNIEXPORT jboolean JNICALL
Java_com_proxy_service_logfile_info_manager_LogFileDecompress_decompressLogDirectory(
        JNIEnv *env,
        jobject /* this */,
        jstring input_dir_path,
        jstring output_dir_path,
        jstring encryption_key
) {
    const char *input_dir = env->GetStringUTFChars(input_dir_path, nullptr);
    const char *output_dir = env->GetStringUTFChars(output_dir_path, nullptr);
    const char *enc_key = env->GetStringUTFChars(encryption_key, nullptr);

    LogFileDecompressor decompressor(enc_key ? enc_key : "");

    bool result = true;

    for (const auto &entry: fs::directory_iterator(input_dir)) {
        if (entry.is_regular_file()) {
            std::string input_path = entry.path();

            std::string output_path = output_dir;
            std::string name = entry.path().filename();
            LOGI("文件: %s", name.c_str());
            output_path = output_path + "/" + "decompress_" + name;

            if (decompressor.decompressLogFile(input_path, output_path)) {
                LOGI("文件解压缩成功: %s", input_path.c_str());
            } else {
                LOGE("文件解压缩失败: %s", input_path.c_str());
                result = false;
            }
        }
    }

    env->ReleaseStringUTFChars(input_dir_path, input_dir);
    env->ReleaseStringUTFChars(output_dir_path, output_dir);
    env->ReleaseStringUTFChars(encryption_key, enc_key);

    return result;
}
}
