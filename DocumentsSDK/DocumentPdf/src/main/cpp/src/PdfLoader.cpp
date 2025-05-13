// JNI头文件
#include <jni.h>

#include <mutex>
#include <unistd.h>
#include <sys/stat.h>

#include "h/pdflog.h"
#include "h/pdfdoc.h"
#include "h/pdfutils.h"

std::mutex gLibraryMutex;
static int sLibraryReferenceCount = 0;

// 初始化函数
static void initLibraryIfNeed() {
    std::lock_guard<std::mutex> lock(gLibraryMutex);
    if (sLibraryReferenceCount == 0) {
        FPDF_InitLibrary();
    }
    sLibraryReferenceCount++;
}

// 销毁函数
static void destroyLibraryIfNeed() {
    std::lock_guard<std::mutex> lock(gLibraryMutex);
    sLibraryReferenceCount--;
    if (sLibraryReferenceCount == 0) {
        FPDF_DestroyLibrary();
    }
}

DocumentFile::DocumentFile() : fileFd(-1), pdfDocument(nullptr) {
    initLibraryIfNeed();
}

DocumentFile::~DocumentFile() {
    if (pdfDocument != nullptr) {
        FPDF_CloseDocument(pdfDocument);
    }
    if (fileFd >= 0) {
        close(fileFd);
    }
    destroyLibraryIfNeed();
}


static jlong loadPageInternal(JNIEnv *env, DocumentFile *doc, int pageIndex) {
    try {
        if (doc == nullptr) throw "Get page document null";

        FPDF_DOCUMENT pdfDoc = doc->pdfDocument;
        if (pdfDoc != nullptr) {
            FPDF_PAGE page = FPDF_LoadPage(pdfDoc, pageIndex);
            if (page == nullptr) {
                throw "Loaded page is null";
            }
            return reinterpret_cast<jlong>(page);
        } else {
            throw "Get page pdf document null";
        }

    } catch (const char *msg) {
        LOGE("%s", msg);
        jniThrowException(env, "java/lang/IllegalStateException",
                          "cannot load page");
        return -1;
    }
}

static void closePageInternal(jlong pagePtr) {
    FPDF_ClosePage(reinterpret_cast<FPDF_PAGE>(pagePtr));
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




/*** *** *** *** *** *** *** 文档加载 *** *** *** *** *** *** ***/

// 通过文件地址加载文档
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

// 通过文件描述符加载文档
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

// 通过文件 byte 数组加载文档
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

// 释放文档
extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeCloseDocument(
        JNIEnv *env, jobject thiz, jlong documentPtr) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(documentPtr);
    delete doc;
}



/*** *** *** *** *** *** *** 文档页面加载 *** *** *** *** *** *** ***/

// 加载文档页面
extern "C" JNIEXPORT jlong JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeLoadPage(
        JNIEnv *env, jobject thiz, jlong docPtr, jint pageIndex) {
    DocumentFile *doc = reinterpret_cast<DocumentFile *>(docPtr);
    return loadPageInternal(env, doc, (int) pageIndex);
}

// 批量加载文档页面
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

// 释放文档页面
extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeClosePage(
        JNIEnv *env, jobject thiz, jlong pagePtr) {
    closePageInternal(pagePtr);
}

// 批量释放文档页面
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






