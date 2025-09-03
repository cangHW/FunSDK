
#include <jni.h>
#include <string>
#include "h/pdflog.h"
#include "fpdfview.h"

jobject NewLong(JNIEnv *env, jlong value) {
    jclass cls = env->FindClass("java/lang/Long");
    jmethodID methodID = env->GetMethodID(cls, "<init>", "(J)V");
    return env->NewObject(cls, methodID, value);
}

jobject NewInteger(JNIEnv *env, jint value) {
    jclass cls = env->FindClass("java/lang/Integer");
    jmethodID methodID = env->GetMethodID(cls, "<init>", "(I)V");
    return env->NewObject(cls, methodID, value);
}

// JNI异常处理
void ThrowJavaException(JNIEnv *env, const char *className, const char *message) {
    jclass exClass = env->FindClass(className);
    if (exClass) {
        env->ThrowNew(exClass, message);
    }
}

int jniThrowException(JNIEnv *env, const char *className, const char *message) {
    jclass exClass = env->FindClass(className);
    if (exClass == nullptr) {
        LOGE("Unable to find exception class %s", className);
        return -1;
    }

    if (env->ThrowNew(exClass, message) != JNI_OK) {
        LOGE("Failed throwing '%s' '%s'", className, message);
        return -1;
    }

    return 0;
}

int jniThrowExceptionFmt(JNIEnv *env, const char *className, const char *fmt, ...) {
    va_list args;
    va_start(args, fmt);
    char msgBuf[512];
    vsnprintf(msgBuf, sizeof(msgBuf), fmt, args);
    return jniThrowException(env, className, msgBuf);
    va_end(args);
}

char *getErrorDescription(const long error) {
    char *description = nullptr;
    switch (error) {
        case FPDF_ERR_SUCCESS:
            asprintf(&description, "No error.");
            break;
        case FPDF_ERR_FILE:
            asprintf(&description, "File not found or could not be opened.");
            break;
        case FPDF_ERR_FORMAT:
            asprintf(&description, "File not in PDF format or corrupted.");
            break;
        case FPDF_ERR_PASSWORD:
            asprintf(&description, "Incorrect password.");
            break;
        case FPDF_ERR_SECURITY:
            asprintf(&description, "Unsupported security scheme.");
            break;
        case FPDF_ERR_PAGE:
            asprintf(&description, "Page not found or content error.");
            break;
        default:
            asprintf(&description, "Unknown error.");
    }

    return description;
}

jstring UnicodeToJString(JNIEnv *env, unsigned int unicode) {
    std::string utf8;
    if (unicode <= 0x7F) {
        // 1 字节 UTF-8
        utf8 += static_cast<char>(unicode);
    } else if (unicode <= 0x7FF) {
        // 2 字节 UTF-8
        utf8 += static_cast<char>(0xC0 | ((unicode >> 6) & 0x1F));
        utf8 += static_cast<char>(0x80 | (unicode & 0x3F));
    } else if (unicode <= 0xFFFF) {
        // 3 字节 UTF-8
        utf8 += static_cast<char>(0xE0 | ((unicode >> 12) & 0x0F));
        utf8 += static_cast<char>(0x80 | ((unicode >> 6) & 0x3F));
        utf8 += static_cast<char>(0x80 | (unicode & 0x3F));
    } else if (unicode <= 0x10FFFF) {
        // 4 字节 UTF-8
        utf8 += static_cast<char>(0xF0 | ((unicode >> 18) & 0x07));
        utf8 += static_cast<char>(0x80 | ((unicode >> 12) & 0x3F));
        utf8 += static_cast<char>(0x80 | ((unicode >> 6) & 0x3F));
        utf8 += static_cast<char>(0x80 | (unicode & 0x3F));
    }
    return env->NewStringUTF(utf8.c_str());
}

jstring UnicodeToJString(JNIEnv *env, unsigned short *buffer, int count) {
    if (!buffer || count <= 0) {
        LOGE("Invalid buffer or count.\n");
        return env->NewStringUTF("");
    }

    char *utf8Buffer = (char *) malloc((count * 3 + 1) * sizeof(char));
    if (!utf8Buffer) {
        LOGE("Failed to allocate memory for UTF-8 buffer.\n");
        free(buffer);
        return env->NewStringUTF("");
    }

    int utf8Index = 0;
    for (int i = 0; i < count; ++i) {
        unsigned short unicodeChar = buffer[i];
        if (unicodeChar <= 0x7F) {
            utf8Buffer[utf8Index++] = (char) unicodeChar;
        } else if (unicodeChar <= 0x7FF) {
            utf8Buffer[utf8Index++] = (char) (0xC0 | ((unicodeChar >> 6) & 0x1F));
            utf8Buffer[utf8Index++] = (char) (0x80 | (unicodeChar & 0x3F));
        } else if (unicodeChar <= 0xFFFF) {
            utf8Buffer[utf8Index++] = (char) (0xE0 | ((unicodeChar >> 12) & 0x0F));
            utf8Buffer[utf8Index++] = (char) (0x80 | ((unicodeChar >> 6) & 0x3F));
            utf8Buffer[utf8Index++] = (char) (0x80 | (unicodeChar & 0x3F));
        } else {
            utf8Buffer[utf8Index++] = (char) (0xF0 | ((unicodeChar >> 18) & 0x07));
            utf8Buffer[utf8Index++] = (char) (0x80 | ((unicodeChar >> 12) & 0x3F));
            utf8Buffer[utf8Index++] = (char) (0x80 | ((unicodeChar >> 6) & 0x3F));
            utf8Buffer[utf8Index++] = (char) (0x80 | (unicodeChar & 0x3F));
        }
    }
    utf8Buffer[utf8Index] = '\0';

    jstring result = env->NewStringUTF(utf8Buffer);
    free(utf8Buffer);
    return result;
}

