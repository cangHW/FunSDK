

#include <jni.h>

#include <mutex>
#include "h/pdflog.h"
#include "h/pdfdoc.h"
#include "h/pdfrgb.h"

#include <android/native_window_jni.h>




/*** *** *** *** *** *** *** 整体渲染 *** *** *** *** *** *** ***/

// 渲染到 Surface
extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeRenderPageToSurface(
        JNIEnv *env, jobject /*thiz*/, jlong pagePtr, jobject objSurface,
        jint dpi, jint startX, jint startY,
        jint drawSizeHor, jint drawSizeVer,
        jboolean renderAnnot, jlong viewBgColor, jlong pageBgColor) {
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
        ANativeWindow_release(nativeWindow);
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
        FPDFBitmap_FillRect(pdfBitmap, 0, 0, canvasHorSize, canvasVerSize, viewBgColor); //窗口背景色 White
    }

    int baseHorSize = (canvasHorSize < drawSizeHor) ? canvasHorSize : drawSizeHor;
    int baseVerSize = (canvasVerSize < drawSizeVer) ? canvasVerSize : drawSizeVer;
    int baseX = (startX < 0) ? 0 : startX;
    int baseY = (startY < 0) ? 0 : startY;
    int flags = FPDF_REVERSE_BYTE_ORDER | FPDF_LCD_TEXT;

    if (renderAnnot) {
        flags |= FPDF_ANNOT;
    }

    FPDFBitmap_FillRect(pdfBitmap, baseX, baseY, baseHorSize, baseVerSize, pageBgColor); //bitmap 背景色 White

    FPDF_RenderPageBitmap(
            pdfBitmap, page,
            startX, startY,
            drawSizeHor, drawSizeVer,
            0, flags
    );


    ANativeWindow_unlockAndPost(nativeWindow);
    ANativeWindow_release(nativeWindow);

    FPDFBitmap_Destroy(pdfBitmap);
}

// 渲染到 Bitmap
extern "C" JNIEXPORT void JNICALL
Java_com_proxy_service_document_pdf_core_PdfiumCore_nativeRenderPageToBitmap(
        JNIEnv *env, jobject /*thiz*/, jlong pagePtr, jobject bitmap,
        jint dpi, jint startX, jint startY,
        jint drawSizeHor, jint drawSizeVer,
        jboolean renderAnnot, jlong viewBgColor, jlong pageBgColor) {
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
    int flags = FPDF_REVERSE_BYTE_ORDER | FPDF_LCD_TEXT;

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



/*** *** *** *** *** *** *** 渐进式渲染 *** *** *** *** *** *** ***/