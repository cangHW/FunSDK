

#include <jni.h>
#include <mutex>
#include "../include/fpdfview.h"
#include "../include/fpdf_text.h"
#include "h/pdfutils.h"
#include "h/pdflog.h"


/*** *** *** *** *** *** *** 文档内文字的处理 *** *** *** *** *** *** ***/

// 加载页面内全部字符信息
extern "C" JNIEXPORT jlong JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeLoadTextPage(
        JNIEnv *env, jobject thiz, jlong pagePtr) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);

    FPDF_TEXTPAGE textPage = FPDFText_LoadPage(page);
    if (textPage == nullptr) {
        throw "Loaded page is null";
    }

    return reinterpret_cast<jlong>(textPage);
}

// 清理加载的页面内字符信息
extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeCloseTextPage(
        JNIEnv *env, jobject thiz, jlong textPagePtr) {
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);
    FPDFText_ClosePage(page);
}

// 获取当前页面内的全部字符数量
extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeGetCharsCount(
        JNIEnv *env, jobject thiz, jlong textPagePtr) {
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);
    return (jint) FPDFText_CountChars(page);
}

// 获取当前页面内对应位置的字符
extern "C" JNIEXPORT jstring JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeGetTextByIndex(
        JNIEnv *env, jobject thiz, jlong textPagePtr, jint textIndex) {
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);

    unsigned int unicode = FPDFText_GetUnicode(page, (int) textIndex);
    if (unicode == 0) {
        return env->NewStringUTF("");
    }
    return UnicodeToJString(unicode);
}

// 获取当前页面内对应位置字符的字体大小
extern "C" JNIEXPORT jdouble JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeGetFontSizeByIndex(
        JNIEnv *env, jobject thiz, jlong textPagePtr, jint textIndex) {
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);
    return (jint) FPDFText_GetFontSize(page, (int) textIndex);
}

// 获取当前页面内对应位置字符的坐标区域
extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeGetTextBox(
        JNIEnv *env,
        jobject thiz,
        jlong textPagePtr,
        jint textIndex,
        jdoubleArray left,
        jdoubleArray top,
        jdoubleArray right,
        jdoubleArray bottom
) {
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);

    jdouble *leftArray = env->GetDoubleArrayElements(left, NULL);
    jdouble *rightArray = env->GetDoubleArrayElements(right, NULL);
    jdouble *bottomArray = env->GetDoubleArrayElements(bottom, NULL);
    jdouble *topArray = env->GetDoubleArrayElements(top, NULL);

    FPDFText_GetCharBox(page, (int) textIndex, leftArray, rightArray, bottomArray, topArray);

    env->ReleaseDoubleArrayElements(left, leftArray, 0);
    env->ReleaseDoubleArrayElements(right, rightArray, 0);
    env->ReleaseDoubleArrayElements(bottom, bottomArray, 0);
    env->ReleaseDoubleArrayElements(top, topArray, 0);
}

// 根据位置获取文字索引
extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeGetTextIndexAtPos(
        JNIEnv *env,
        jobject thiz,
        jlong textPagePtr,
        jdouble touchX,
        jdouble touchY,
        jdouble xTolerance,
        jdouble yTolerance
) {
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);
    return (jint) FPDFText_GetCharIndexAtPos(page, touchX, touchY, xTolerance, yTolerance);
}

// 获取指定位置与长度的字符串
extern "C" JNIEXPORT jstring JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeGetText(
        JNIEnv *env,
        jobject thiz,
        jlong textPagePtr,
        jint startIndex,
        jint count
) {
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);

    unsigned short *buffer = (unsigned short *) malloc((count + 1) * sizeof(unsigned short));
    if (!buffer) {
        LOGE("Failed to allocate memory for buffer.\n");
        return env->NewStringUTF("");
    }

    int extracted_count = FPDFText_GetText(page, startIndex, count, buffer);
    if (extracted_count <= 0) {
        LOGE("Failed to extract text.\n");
        free(buffer);
        return env->NewStringUTF("");
    }

    jstring result = UnicodeToJString(env, buffer, count);
    free(buffer);
    return result;
}

// 获取当前页面内对应字符串的坐标区域
extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeGetTextRect(
        JNIEnv *env,
        jobject thiz,
        jlong textPagePtr,
        jint startIndex,
        jint count,
        jdoubleArray left,
        jdoubleArray top,
        jdoubleArray right,
        jdoubleArray bottom
) {
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);

    int rect_index = FPDFText_CountRects(page, startIndex, count);
    if (rect_index <= 0) {
        LOGE("No rectangles found for the specified range.\n");
        return;
    }

    jdouble *leftArray = env->GetDoubleArrayElements(left, NULL);
    jdouble *topArray = env->GetDoubleArrayElements(top, NULL);
    jdouble *rightArray = env->GetDoubleArrayElements(right, NULL);
    jdouble *bottomArray = env->GetDoubleArrayElements(bottom, NULL);

    if (!leftArray || !topArray || !rightArray || !bottomArray) {
        LOGE("Failed to get Java array elements.\n");
        return;
    }

    FPDFText_GetRect(page, rect_index, leftArray, topArray, rightArray, bottomArray);

    env->ReleaseDoubleArrayElements(left, leftArray, 0);
    env->ReleaseDoubleArrayElements(right, rightArray, 0);
    env->ReleaseDoubleArrayElements(bottom, bottomArray, 0);
    env->ReleaseDoubleArrayElements(top, topArray, 0);
}

// 获取当前页面坐标区域内的字符串
extern "C" JNIEXPORT jstring JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeGetTextByRect(
        JNIEnv *env,
        jobject thiz,
        jlong textPagePtr,
        jdouble left,
        jdouble top,
        jdouble right,
        jdouble bottom
) {
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);

    int char_count = FPDFText_GetBoundedText(page, left, top, right, bottom, NULL, 0);
    if (char_count <= 0) {
        return env->NewStringUTF("");
    }

    unsigned short *buffer = (unsigned short *) malloc((char_count + 1) * sizeof(unsigned short));
    if (!buffer) {
        LOGE("Failed to allocate memory for buffer.\n");
        return env->NewStringUTF("");
    }

    int final_count = char_count + 1;
    int extracted_count = FPDFText_GetBoundedText(page, left, top, right, bottom, buffer,
                                                  final_count);
    if (char_count <= 0) {
        free(buffer);
        return env->NewStringUTF("");
    }

    jstring result = UnicodeToJString(env, buffer, final_count);
    free(buffer);
    return result;
}

// 获取当前页面坐标区域内的字符串
extern "C" JNIEXPORT jintArray JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeSearchText(
        JNIEnv *env,
        jobject thiz,
        jlong textPagePtr,
        jstring text,
        jint flag,
        jint startIndex
){
    if (text == NULL) {
        return NULL;
    }
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);

    unsigned long flags = 0;
    if (flag == 1){
        flags = 0x00000001;
    } else if (flag == 2){
        flags = 0x00000002;
    } else{
        flags = 0x00000001 | 0x00000002;
    }

    const jchar *nativeText = env->GetStringChars(text, NULL);
    FPDF_SCHHANDLE search_handle = FPDFText_FindStart(
            page,
            reinterpret_cast<const unsigned short *>(nativeText),
            flags,
            startIndex
    );
    env->ReleaseStringChars(text, nativeText);

    if (!search_handle) {
        return NULL;
    }

    int result_count = 0;
    int result_capacity = 10;
    int *results = (int *)malloc(result_capacity * sizeof(int));
    if (!results) {
        FPDFText_FindClose(search_handle);
        return NULL;
    }

    while (FPDFText_FindNext(search_handle)) {
        int index = FPDFText_GetSchResultIndex(search_handle);
        if (result_count >= result_capacity) {
            result_capacity *= 2;
            int *new_results = (int *)realloc(results, result_capacity * sizeof(int));
            if (!new_results) {
                free(results);
                FPDFText_FindClose(search_handle);
                return NULL;
            }
            results = new_results;
        }
        results[result_count++] = index;
    }
    FPDFText_FindClose(search_handle);

    jintArray resultArray = env->NewIntArray(result_count);
    if (resultArray != NULL) {
        env->SetIntArrayRegion(resultArray, 0, result_count, results);
    }
    free(results);
    return resultArray;
}

// 获取页面文字中的链接信息, 和文档的超链接不同
extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_info_core_PdfiumCore_nativeGetWebLinks(
        JNIEnv *env,
        jobject thiz,
        jlong textPagePtr
){
    FPDF_TEXTPAGE page = reinterpret_cast<FPDF_TEXTPAGE>(textPagePtr);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListInit = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jobject resultList = env->NewObject(arrayListClass, arrayListInit);

    FPDF_PAGELINK page_links = FPDFLink_LoadWebLinks(page);
    if (!page_links) {
        LOGE("Failed to load weblinks.\n");
        return resultList;
    }

    int link_count = FPDFLink_CountWebLinks(page_links);
    LOGI("Found %d weblinks:\n", link_count);


    // 获取 TextWebLinkData 类和构造方法
    jclass textWebLinkDataClass = env->FindClass("com/proxy/service/document/pdf/base/bean/TextWebLinkData");
    jmethodID textWebLinkDataInit = env->GetMethodID(
            textWebLinkDataClass,
            "<init>",
            "(Ljava/lang/String;Ljava/util/ArrayList;)V"
    );

    jclass rectFClass = env->FindClass("android/graphics/RectF");
    jmethodID rectFInit = env->GetMethodID(rectFClass, "<init>", "(FFFF)V");

    for (int link_index = 0; link_index < link_count; ++link_index) {
        unsigned short url_buffer[256];
        int url_length = FPDFLink_GetURL(page_links, link_index, url_buffer, 256);

        if (url_length <= 0){
            LOGE("Invalid URL for link %d, url length: %d", link_index, url_length);
            continue;
        }

        jobject boundsList = env->NewObject(arrayListClass, arrayListInit);

        int rect_count = FPDFLink_CountRects(page_links, link_index);
        LOGI("Link %d has %d rectangles.", link_index, rect_count);

        for (int rect_index = 0; rect_index < rect_count; ++rect_index) {
            double left, top, right, bottom;
            FPDFLink_GetRect(page_links, link_index, rect_index, &left, &top, &right, &bottom);

            jobject rectF = env->NewObject(rectFClass, rectFInit, (float)left, (float)top, (float)right, (float)bottom);
            env->CallBooleanMethod(boundsList, arrayListAdd, rectF);
            env->DeleteLocalRef(rectF);
        }

        jstring urlString = UnicodeToJString(env, url_buffer, url_length);
        jobject textWebLinkData = env->NewObject(textWebLinkDataClass, textWebLinkDataInit, urlString, boundsList);
        env->CallBooleanMethod(resultList, arrayListAdd, textWebLinkData);

        env->DeleteLocalRef(boundsList);
        env->DeleteLocalRef(urlString);
        env->DeleteLocalRef(textWebLinkData);
    }

    FPDFLink_CloseWebLinks(page_links);
    return resultList;
}

