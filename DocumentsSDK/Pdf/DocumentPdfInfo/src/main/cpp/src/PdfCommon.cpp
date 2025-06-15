
#include <jni.h>

#include "fpdfview.h"


// 文档坐标映射为屏幕坐标
extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativePageCoordsToDevice(
        JNIEnv *env, jobject /*thiz*/, jlong pagePtr, jint startX, jint startY, jint sizeX,
        jint sizeY, jint rotate, jdouble pageX, jdouble pageY) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    int deviceX, deviceY;

    FPDF_PageToDevice(page, startX, startY, sizeX, sizeY, rotate, pageX, pageY, &deviceX, &deviceY);

    jclass clazz = env->FindClass("android/graphics/Point");
    jmethodID constructorID = env->GetMethodID(clazz, "<init>", "(II)V");
    return env->NewObject(clazz, constructorID, deviceX, deviceY);
}



