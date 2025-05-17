
#include <jni.h>
#include <mutex>

#include "h/pdfdoc.h"

#include "fpdfview.h"
#include "fpdf_doc.h"
#include "h/pdfutils.h"

#include <vector>


template<class string_type>
inline typename string_type::value_type *WriteInto(string_type *str, size_t length_with_null) {
    str->reserve(length_with_null);
    str->resize(length_with_null - 1);
    return &((*str)[0]);
}




/*** *** *** *** *** *** *** 文档基础信息 *** *** *** *** *** *** ***/

// 获取文档页面数量
extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageCount(
        JNIEnv *env, jobject thiz, jlong documentPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(documentPtr);
    return (jint) FPDF_GetPageCount(doc->pdfDocument);
}

// 获取文档说明信息
extern "C" JNIEXPORT jstring JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetDocumentMetaText(
        JNIEnv *env, jobject /*thiz*/, jlong docPtr, jstring tag) {
    const char *ctag = env->GetStringUTFChars(tag, nullptr);
    if (ctag == nullptr) {
        return env->NewStringUTF("");
    }
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);

    size_t bufferLen = FPDF_GetMetaText(doc->pdfDocument, ctag, nullptr, 0);
    if (bufferLen <= 2) {
        return env->NewStringUTF("");
    }
    std::wstring text;
    FPDF_GetMetaText(doc->pdfDocument, ctag, WriteInto(&text, bufferLen + 1), bufferLen);
    env->ReleaseStringUTFChars(tag, ctag);
    return env->NewString((jchar *) text.c_str(), bufferLen / 2 - 1);
}



/*** *** *** *** *** *** *** 文档目录 *** *** *** *** *** *** ***/

// 获取文档目录的第一个子目录指针
extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetFirstChildBookmark(
        JNIEnv *env, jobject /*thiz*/, jlong docPtr, jobject bookmarkPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    FPDF_BOOKMARK parent;
    if (bookmarkPtr == NULL) {
        parent = NULL;
    } else {
        jclass longClass = env->GetObjectClass(bookmarkPtr);
        jmethodID longValueMethod = env->GetMethodID(longClass, "longValue", "()J");

        jlong ptr = env->CallLongMethod(bookmarkPtr, longValueMethod);
        parent = reinterpret_cast<FPDF_BOOKMARK>(ptr);
    }
    FPDF_BOOKMARK bookmark = FPDFBookmark_GetFirstChild(doc->pdfDocument, parent);
    if (bookmark == NULL) {
        return NULL;
    }
    return NewLong(env, reinterpret_cast<jlong>(bookmark));
}

// 获取文档目录的下一个目录指针
extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetNextChildBookmark(
        JNIEnv *env, jobject /*thiz*/, jlong docPtr, jlong bookmarkPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    FPDF_BOOKMARK parent = reinterpret_cast<FPDF_BOOKMARK>(bookmarkPtr);
    FPDF_BOOKMARK bookmark = FPDFBookmark_GetNextSibling(doc->pdfDocument, parent);
    if (bookmark == NULL) {
        return NULL;
    }
    return NewLong(env, reinterpret_cast<jlong>(bookmark));
}

// 获取文档目录标题
extern "C" JNIEXPORT jstring JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetBookmarkTitle(
        JNIEnv *env, jobject /*thiz*/, jlong bookmarkPtr) {
    FPDF_BOOKMARK bookmark = reinterpret_cast<FPDF_BOOKMARK>(bookmarkPtr);
    size_t bufferLen = FPDFBookmark_GetTitle(bookmark, NULL, 0);
    if (bufferLen <= 2) {
        return env->NewStringUTF("");
    }
    std::wstring title;
    FPDFBookmark_GetTitle(bookmark, WriteInto(&title, bufferLen + 1), bufferLen);
    return env->NewString((jchar *) title.c_str(), bufferLen / 2 - 1);
}

// 获取文档目录对应的页码
extern "C" JNIEXPORT jlong JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetBookmarkDestIndex(
        JNIEnv *env, jobject /*thiz*/, jlong docPtr, jlong bookmarkPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    FPDF_BOOKMARK bookmark = reinterpret_cast<FPDF_BOOKMARK>(bookmarkPtr);

    FPDF_DEST dest = FPDFBookmark_GetDest(doc->pdfDocument, bookmark);
    if (dest == NULL) {
        return -1;
    }
    return (jlong) FPDFDest_GetPageIndex(doc->pdfDocument, dest);
}



/*** *** *** *** *** *** *** 页面尺寸信息 *** *** *** *** *** *** ***/

// 获取文档页面宽度: 像素
extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageWidthPixel(
        JNIEnv *env, jobject thiz, jlong pagePtr, jint dpi) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    return (jint) (FPDF_GetPageWidth(page) * dpi / 72);
}

// 获取文档页面高度: 像素
extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageHeightPixel(
        JNIEnv *env, jobject thiz, jlong pagePtr, jint dpi) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    return (jint) (FPDF_GetPageHeight(page) * dpi / 72);
}

// 获取文档页面宽度: 点
extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageWidthPoint(
        JNIEnv *env, jobject thiz, jlong pagePtr) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    return (jint) FPDF_GetPageWidth(page);
}

// 获取文档页面高度: 点
extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageHeightPoint(
        JNIEnv *env, jobject thiz, jlong pagePtr) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    return (jint) FPDF_GetPageHeight(page);
}

// 获取文档页面宽高
extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageSizeByIndex(
        JNIEnv *env, jobject thiz, jlong docPtr, jint pageIndex, jint dpi) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    if (doc == nullptr) {
        return nullptr;
    }

    double width, height;
    int result = FPDF_GetPageSizeByIndex(doc->pdfDocument, pageIndex, &width, &height);

    if (result == 0) {
        width = 0;
        height = 0;
    }

    jint widthInt = (jint) (width * dpi / 72);
    jint heightInt = (jint) (height * dpi / 72);

    jclass clazz = env->FindClass("com/proxy/service/document/base/pdf/info/PageSize");
    jmethodID constructorID = env->GetMethodID(clazz, "<init>", "(II)V");
    return env->NewObject(clazz, constructorID, widthInt, heightInt);
}



/*** *** *** *** *** *** *** 页面超链信息 *** *** *** *** *** *** ***/

// 获取文档对应页面的全部超链接信息
extern "C" JNIEXPORT jlongArray JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageLinks(
        JNIEnv *env, jobject /*thiz*/, jlong pagePtr) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    int pos = 0;
    std::vector<jlong> links;
    FPDF_LINK link;
    while (FPDFLink_Enumerate(page, &pos, &link)) {
        links.push_back(reinterpret_cast<jlong>(link));
    }

    jlongArray result = env->NewLongArray(links.size());
    env->SetLongArrayRegion(result, 0, links.size(), &links[0]);
    return result;
}

// 获取超链接对应的目标页码
extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetDestPageIndex(
        JNIEnv *env, jobject /*thiz*/, jlong docPtr, jlong linkPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    FPDF_LINK link = reinterpret_cast<FPDF_LINK>(linkPtr);
    FPDF_DEST dest = FPDFLink_GetDest(doc->pdfDocument, link);
    if (dest == NULL) {
        return NULL;
    }
    unsigned long index = FPDFDest_GetPageIndex(doc->pdfDocument, dest);
    return NewInteger(env, (jint) index);
}

// 获取超链接对应的目标 uri
extern "C" JNIEXPORT jstring JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetLinkURI(
        JNIEnv *env, jobject /*thiz*/, jlong docPtr, jlong linkPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    FPDF_LINK link = reinterpret_cast<FPDF_LINK>(linkPtr);
    FPDF_ACTION action = FPDFLink_GetAction(link);
    if (action == NULL) {
        return NULL;
    }
    size_t bufferLen = FPDFAction_GetURIPath(doc->pdfDocument, action, NULL, 0);
    if (bufferLen <= 0) {
        return env->NewStringUTF("");
    }
    std::string uri;
    FPDFAction_GetURIPath(doc->pdfDocument, action, WriteInto(&uri, bufferLen), bufferLen);
    return env->NewStringUTF(uri.c_str());
}

// 获取超链接对应的坐标区域
extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetLinkRectF(
        JNIEnv *env, jobject /*thiz*/, jlong linkPtr) {
    FPDF_LINK link = reinterpret_cast<FPDF_LINK>(linkPtr);
    FS_RECTF fsRectF;
    FPDF_BOOL result = FPDFLink_GetAnnotRect(link, &fsRectF);

    if (!result) {
        return NULL;
    }

    jclass clazz = env->FindClass("android/graphics/RectF");
    jmethodID constructorID = env->GetMethodID(clazz, "<init>", "(FFFF)V");
    return env->NewObject(clazz, constructorID, fsRectF.left, fsRectF.top, fsRectF.right,
                          fsRectF.bottom);
}
