// JNI头文件
#include <jni.h>

#include <unistd.h>
#include <sys/stat.h>

#include "fpdfview.h"
#include "fpdf_doc.h"
#include "h/pdflog.h"
#include "h/pdfmanager.h"
#include "h/pdfutils.h"
#include "h/pdfrgb.h"

#include <android/native_window_jni.h>


template<class string_type>
inline typename string_type::value_type *WriteInto(string_type *str, size_t length_with_null) {
    str->reserve(length_with_null);
    str->resize(length_with_null - 1);
    return &((*str)[0]);
}

// 文件读取回调
static int
getBlock(void *param, unsigned long position, unsigned char *outBuffer, unsigned long size) {
    const int fd = reinterpret_cast<intptr_t>(param);
    const int readCount = pread(fd, outBuffer, size, position);
    return readCount >= 0 ? 1 : 0;
}

jobject createOpenError(JNIEnv *env, long code){
    jclass clazz = env->FindClass("com/proxy/service/document/pdf/core/OpenResult");
    jmethodID constructorID = env->GetMethodID(clazz, "<init>", "(JJ)V");
    jobject result = env->NewObject(clazz, constructorID, static_cast<jlong>(-1), static_cast<jlong>(code));
    env->DeleteLocalRef(clazz);
    return result;
}

jobject createOpenResult(JNIEnv *env,FPDF_DOCUMENT document, DocumentFile *docFile){
    jclass clazz = env->FindClass("com/proxy/service/document/pdf/core/OpenResult");
    jmethodID constructorID = env->GetMethodID(clazz, "<init>", "(JJ)V");

    if (!document) {
        delete docFile;
        jlong errorNum = static_cast<jlong>(FPDF_GetLastError());
        jobject result = env->NewObject(clazz, constructorID, static_cast<jlong>(-1), errorNum);
        env->DeleteLocalRef(clazz);
        return result;
    }

    docFile->pdfDocument = document;
    jobject result = env->NewObject(clazz, constructorID, reinterpret_cast<jlong>(docFile), static_cast<jlong>(0));
    env->DeleteLocalRef(clazz);
    return result;
}

// JNI方法实现
extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeOpenDocumentByPath(
        JNIEnv *env, jobject thiz, jstring filepath, jstring password){
    DocumentFile *docFile = new DocumentFile();

    const char *cfilepath = nullptr;
    if (filepath != nullptr) {
        cfilepath = env->GetStringUTFChars(filepath, nullptr);
    }

    const char *cpassword = nullptr;
    if (password != nullptr) {
        cpassword = env->GetStringUTFChars(password, nullptr);
    }

    FPDF_DOCUMENT document = FPDF_LoadDocument(cfilepath, cpassword);

    if (cfilepath != nullptr) {
        env->ReleaseStringUTFChars(filepath, cfilepath);
    }

    if (cpassword != nullptr) {
        env->ReleaseStringUTFChars(password, cpassword);
    }

    return createOpenResult(env, document, docFile);
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeOpenDocumentByFd(
        JNIEnv *env, jobject thiz, jint fd, jstring password) {

    struct stat file_state;
    if (fstat(fd, &file_state) < 0) {
        return createOpenError(env, 2);
    }

    size_t fileLength = file_state.st_size;
    if (fileLength <= 0) {
        return createOpenError(env, 2);
    }

    DocumentFile *docFile = new DocumentFile();

    FPDF_FILEACCESS loader;
    loader.m_FileLen = fileLength;
    loader.m_Param = reinterpret_cast<void *>(intptr_t(fd));
    loader.m_GetBlock = &getBlock;

    const char *cpassword = nullptr;
    if (password != nullptr) {
        cpassword = env->GetStringUTFChars(password, nullptr);
    }

    FPDF_DOCUMENT document = FPDF_LoadCustomDocument(&loader, cpassword);

    if (cpassword != nullptr) {
        env->ReleaseStringUTFChars(password, cpassword);
    }

    return createOpenResult(env, document, docFile);
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeOpenDocumentByMem(
        JNIEnv *env, jobject thiz, jbyteArray data, jstring password) {

    DocumentFile *docFile = new DocumentFile();

    const char *cpassword = nullptr;
    if (password != nullptr) {
        cpassword = env->GetStringUTFChars(password, nullptr);
    }

    jbyte *cData = env->GetByteArrayElements(data, nullptr);
    int size = env->GetArrayLength(data);
    jbyte *cDataCopy = new jbyte[size];
    memcpy(cDataCopy, cData, size);

    FPDF_DOCUMENT document = FPDF_LoadMemDocument(
            reinterpret_cast<const void *>(cDataCopy),
            size,
            cpassword
    );

    env->ReleaseByteArrayElements(data, cData, JNI_ABORT);

    if (cpassword != nullptr) {
        env->ReleaseStringUTFChars(password, cpassword);
    }

    return createOpenResult(env, document, docFile);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageCount(
        JNIEnv *env, jobject thiz, jlong documentPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(documentPtr);
    return (jint) FPDF_GetPageCount(doc->pdfDocument);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeCloseDocument(
        JNIEnv *env, jobject thiz, jlong documentPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(documentPtr);
    delete doc;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeLoadPage(
        JNIEnv *env, jobject thiz, jlong docPtr, jint pageIndex) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    return loadPageInternal(env, doc, (int) pageIndex);
}

extern "C" JNIEXPORT jlongArray JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeLoadPages(
        JNIEnv *env, jobject thiz, jlong docPtr, jint fromIndex, jint toIndex) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);

    if (toIndex < fromIndex) return NULL;
    jlong pages[toIndex - fromIndex + 1];

    int i;
    for (i = 0; i <= (toIndex - fromIndex); i++) {
        pages[i] = loadPageInternal(env, doc, (int) (i + fromIndex));
    }

    jlongArray javaPages = env->NewLongArray((jsize) (toIndex - fromIndex + 1));
    env->SetLongArrayRegion(javaPages, 0, (jsize) (toIndex - fromIndex + 1), (const jlong *) pages);
    return javaPages;
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeClosePage(
        JNIEnv *env, jobject thiz, jlong pagePtr) {
    closePageInternal(pagePtr);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeClosePages(
        JNIEnv *env, jobject thiz, jlongArray pagesPtr) {
    int length = (int) (env->GetArrayLength(pagesPtr));
    jlong *pages = env->GetLongArrayElements(pagesPtr, NULL);

    int i;
    for (i = 0; i < length; i++) {
        closePageInternal(pages[i]);
    }

    env->ReleaseLongArrayElements(pagesPtr, pages, JNI_ABORT);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageWidthPixel(
        JNIEnv *env, jobject thiz, jlong pagePtr, jint dpi) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    return (jint) (FPDF_GetPageWidth(page) * dpi / 72);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageHeightPixel(
        JNIEnv *env, jobject thiz, jlong pagePtr, jint dpi) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    return (jint) (FPDF_GetPageHeight(page) * dpi / 72);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageWidthPoint(
        JNIEnv *env, jobject thiz, jlong pagePtr) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    return (jint) FPDF_GetPageWidth(page);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageHeightPoint(
        JNIEnv *env, jobject thiz, jlong pagePtr) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    return (jint) FPDF_GetPageHeight(page);
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetPageSizeByIndex(
        JNIEnv *env, jobject thiz, jlong docPtr, jint pageIndex, jint dpi) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    if (doc == nullptr) {
        LOGE("Document is null");
        jniThrowException(env, "java/lang/IllegalStateException", "Document is null");
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

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeRenderPage(
        JNIEnv *env, jobject /*thiz*/, jlong pagePtr, jobject objSurface,
        jint dpi, jint startX, jint startY,
        jint drawSizeHor, jint drawSizeVer,
        jboolean renderAnnot) {
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, objSurface);
    if (nativeWindow == nullptr) {
        LOGE("native window pointer null");
        return;
    }
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);

    if (page == NULL || nativeWindow == NULL) {
        LOGE("Render page pointers invalid");
        return;
    }

    if (ANativeWindow_getFormat(nativeWindow) != WINDOW_FORMAT_RGBA_8888) {
        LOGD("Set format to RGBA_8888");
        ANativeWindow_setBuffersGeometry(nativeWindow,
                                         ANativeWindow_getWidth(nativeWindow),
                                         ANativeWindow_getHeight(nativeWindow),
                                         WINDOW_FORMAT_RGBA_8888);
    }

    ANativeWindow_Buffer windowBuffer;
    int ret;
    if ((ret = ANativeWindow_lock(nativeWindow, &windowBuffer, nullptr)) != 0) {
        LOGE("Locking native window failed: %s", strerror(ret * -1));
        return;
    }

    int canvasHorSize = windowBuffer.width;
    int canvasVerSize = windowBuffer.height;

    FPDF_BITMAP pdfBitmap = FPDFBitmap_CreateEx(
            canvasHorSize, canvasVerSize,
            FPDFBitmap_BGRA,
            windowBuffer.bits,
            (int) (windowBuffer.stride) * 4
    );

    if (drawSizeHor < canvasHorSize || drawSizeVer < canvasVerSize) {
        FPDFBitmap_FillRect(pdfBitmap, 0, 0, canvasHorSize, canvasVerSize, 0x848484FF); //Gray
    }

    int baseHorSize = (canvasHorSize < drawSizeHor) ? canvasHorSize : drawSizeHor;
    int baseVerSize = (canvasVerSize < drawSizeVer) ? canvasVerSize : drawSizeVer;
    int baseX = (startX < 0) ? 0 : startX;
    int baseY = (startY < 0) ? 0 : startY;
    int flags = FPDF_REVERSE_BYTE_ORDER;

    if (renderAnnot) {
        flags |= FPDF_ANNOT;
    }

    FPDFBitmap_FillRect(pdfBitmap, baseX, baseY, baseHorSize, baseVerSize, 0xFFFFFFFF); //White

    FPDF_RenderPageBitmap(
            pdfBitmap, page,
            startX, startY,
            drawSizeHor, drawSizeVer,
            0, flags
    );


    ANativeWindow_unlockAndPost(nativeWindow);
    ANativeWindow_release(nativeWindow);
}

extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeRenderPageBitmap(
        JNIEnv *env, jobject /*thiz*/, jlong pagePtr, jobject bitmap,
        jint dpi, jint startX, jint startY,
        jint drawSizeHor, jint drawSizeVer,
        jboolean renderAnnot) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);

    if (page == NULL || bitmap == NULL) {
        LOGE("Render page pointers invalid");
        return;
    }

    AndroidBitmapInfo info;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        LOGE("Fetching bitmap info failed: %s", strerror(ret * -1));
        return;
    }

    int canvasHorSize = info.width;
    int canvasVerSize = info.height;

    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888 &&
        info.format != ANDROID_BITMAP_FORMAT_RGB_565) {
        LOGE("Bitmap format must be RGBA_8888 or RGB_565");
        return;
    }

    void *addr;
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &addr)) != 0) {
        LOGE("Locking bitmap failed: %s", strerror(ret * -1));
        return;
    }

    void *tmp;
    int format;
    int sourceStride;
    if (info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
        tmp = malloc(canvasVerSize * canvasHorSize * sizeof(rgb));
        sourceStride = canvasHorSize * sizeof(rgb);
        format = FPDFBitmap_BGR;
    } else {
        tmp = addr;
        sourceStride = info.stride;
        format = FPDFBitmap_BGRA;
    }

    FPDF_BITMAP pdfBitmap = FPDFBitmap_CreateEx(canvasHorSize, canvasVerSize,
                                                format, tmp, sourceStride);

    if (drawSizeHor < canvasHorSize || drawSizeVer < canvasVerSize) {
        FPDFBitmap_FillRect(pdfBitmap, 0, 0, canvasHorSize, canvasVerSize, 0x848484FF); //Gray
    }

    int baseHorSize = (canvasHorSize < drawSizeHor) ? canvasHorSize : (int) drawSizeHor;
    int baseVerSize = (canvasVerSize < drawSizeVer) ? canvasVerSize : (int) drawSizeVer;
    int baseX = (startX < 0) ? 0 : (int) startX;
    int baseY = (startY < 0) ? 0 : (int) startY;
    int flags = FPDF_REVERSE_BYTE_ORDER;

    if (renderAnnot) {
        flags |= FPDF_ANNOT;
    }

    FPDFBitmap_FillRect(pdfBitmap, baseX, baseY, baseHorSize, baseVerSize, 0xFFFFFFFF); //White

    FPDF_RenderPageBitmap(pdfBitmap, page,
                          startX, startY,
                          (int) drawSizeHor, (int) drawSizeVer,
                          0, flags);

    if (info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
        rgbBitmapTo565(tmp, sourceStride, addr, &info);
        free(tmp);
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}

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

extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeGetSiblingBookmark(
        JNIEnv *env, jobject /*thiz*/, jlong docPtr, jlong bookmarkPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    FPDF_BOOKMARK parent = reinterpret_cast<FPDF_BOOKMARK>(bookmarkPtr);
    FPDF_BOOKMARK bookmark = FPDFBookmark_GetNextSibling(doc->pdfDocument, parent);
    if (bookmark == NULL) {
        return NULL;
    }
    return NewLong(env, reinterpret_cast<jlong>(bookmark));
}

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

extern "C" JNIEXPORT jobject JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativePageCoordsToDevice(
        JNIEnv *env, jobject /*thiz*/, jlong pagePtr, jint startX, jint startY, jint sizeX,
        jint sizeY, jint rotate, jdouble pageX, jdouble pageY) {
    FPDF_PAGE page = reinterpret_cast<FPDF_PAGE>(pagePtr);
    int deviceX, deviceY;

    FPDF_PageToDevice(page, startX, startY, sizeX, sizeY, rotate, pageX, pageY, &deviceX, &deviceY);

    jclass clazz = env->FindClass("android/graphics/Point");
    jmethodID constructorID = env->GetMethodID(clazz, "<init>", "(II)V");
    return env->NewObject(clazz, constructorID, deviceX, deviceY);
}
