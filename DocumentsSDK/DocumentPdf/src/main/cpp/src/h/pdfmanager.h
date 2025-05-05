
#include <mutex>
#include "pdfutils.h"


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

// 文档类
class DocumentFile {
private:
    int fileFd;

public:
    FPDF_DOCUMENT pdfDocument;

    DocumentFile() : fileFd(-1), pdfDocument(nullptr) {
        initLibraryIfNeed();
    }

    ~DocumentFile() {
        if (pdfDocument != nullptr) {
            FPDF_CloseDocument(pdfDocument);
        }
        if (fileFd >= 0) {
            close(fileFd);
        }
        destroyLibraryIfNeed();
    }
};

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

