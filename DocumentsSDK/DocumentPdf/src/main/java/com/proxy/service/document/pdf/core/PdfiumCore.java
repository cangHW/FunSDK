//package com.proxy.service.document.pdf.core;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Point;
//import android.graphics.RectF;
//import android.os.ParcelFileDescriptor;
//import android.util.Log;
//import android.view.Surface;
//
//import com.proxy.service.document.base.pdf.info.MetaData;
//import com.proxy.service.document.base.pdf.info.PageSize;
//
//import java.io.FileDescriptor;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PdfiumCore {
//    private static final String TAG = PdfiumCore.class.getName();
//    private static final Class FD_CLASS = FileDescriptor.class;
//    private static final String FD_FIELD_NAME = "descriptor";
//
//    static {
//        try {
//            System.loadLibrary("c++_shared");
//            System.loadLibrary("modpng");
//            System.loadLibrary("modft2");
//            System.loadLibrary("modpdfium");
//            System.loadLibrary("jniPdfium");
//        } catch (UnsatisfiedLinkError e) {
//            Log.e(TAG, "Native libraries failed to load - " + e);
//        }
//    }
//
//    private native long nativeOpenDocument(int fd, String password);
//
//    private native long nativeOpenMemDocument(byte[] data, String password);
//
//    /**
//     * 关闭对应文档
//     *
//     * @param doc_hand 文档指针
//     */
//    public native void nativeCloseDocument(long doc_hand);
//
//    /**
//     * 获取对应文档内的页面数量
//     *
//     * @param doc_hand 文档指针
//     */
//    public native int nativeGetPageCount(long doc_hand);
//
//    /**
//     * 获取对应文档内对应页面的指针
//     *
//     * @param doc_hand  文档指针
//     * @param pageIndex 页面位置
//     */
//    public native long nativeLoadPage(long doc_hand, int pageIndex);
//
//    private native long[] nativeLoadPages(long docPtr, int fromIndex, int toIndex);
//
//    private native void nativeClosePage(long pagePtr);
//
//    private native void nativeClosePages(long[] pagesPtr);
//
//    private native int nativeGetPageWidthPixel(long pagePtr, int dpi);
//
//    private native int nativeGetPageHeightPixel(long pagePtr, int dpi);
//
//    private native int nativeGetPageWidthPoint(long pagePtr);
//
//    private native int nativeGetPageHeightPoint(long pagePtr);
//
//    //private native long nativeGetNativeWindow(Surface surface);
//    //private native void nativeRenderPage(long pagePtr, long nativeWindowPtr);
//    private native void nativeRenderPage(
//            long pagePtr,
//            Surface surface,
//            int dpi,
//            int startX,
//            int startY,
//            int drawSizeHor,
//            int drawSizeVer,
//            boolean renderAnnot
//    );
//
//    private native void nativeRenderPageBitmap(
//            long pagePtr,
//            Bitmap bitmap,
//            int dpi,
//            int startX,
//            int startY,
//            int drawSizeHor,
//            int drawSizeVer,
//            boolean renderAnnot
//    );
//
//    /**
//     * 获取对应文档说明信息
//     *
//     * @param doc_hand 文档指针
//     * @param tag      标签
//     */
//    public native String nativeGetDocumentMetaText(long doc_hand, String tag);
//
//    /**
//     * 获取对应文档目录的第一个子目录指针
//     *
//     * @param doc_hand       文档指针
//     * @param catalogue_hand 文档目录指针
//     */
//    public native Long nativeGetFirstChildBookmark(long doc_hand, Long catalogue_hand);
//
//    /**
//     * 获取对应文档目录的下一个目录指针
//     *
//     * @param doc_hand       文档指针
//     * @param catalogue_hand 文档目录指针
//     */
//    public native Long nativeGetSiblingBookmark(long doc_hand, long catalogue_hand);
//
//    /**
//     * 获取对应文档目录标题
//     *
//     * @param catalogue_hand 文档目录指针
//     */
//    public native String nativeGetBookmarkTitle(long catalogue_hand);
//
//    /**
//     * 获取文档目录对应的页码
//     *
//     * @param doc_hand       文档指针
//     * @param catalogue_hand 文档目录指针
//     */
//    public native long nativeGetBookmarkDestIndex(long doc_hand, long catalogue_hand);
//
//    private native PageSize nativeGetPageSizeByIndex(long docPtr, int pageIndex, int dpi);
//
//    private native long[] nativeGetPageLinks(long pagePtr);
//
//    private native Integer nativeGetDestPageIndex(long docPtr, long linkPtr);
//
//    private native String nativeGetLinkURI(long docPtr, long linkPtr);
//
//    private native RectF nativeGetLinkRectF(long linkPtr);
//
//    /**
//     * 页面坐标映射为屏幕坐标
//     *
//     * @param pagePtr 页面指针
//     * @param startX  显示区域在设备坐标中的左侧像素位置
//     * @param startY  显示区域在设备坐标中的顶部像素位置
//     * @param sizeX   显示区域的宽度(单位：像素)
//     * @param sizeY   显示区域的高度(单位：像素)
//     * @param rotate  页面方向：0（正常）, 1（顺时针旋转90度）, 2（顺时针旋转180度）, 3（顺时针旋转270度）
//     * @param pageX   页面坐标中的X值
//     * @param pageY   页面坐标中的Y值
//     * @return 返回映射坐标
//     */
//    private native Point nativePageCoordsToDevice(
//            long pagePtr,
//            int startX,
//            int startY,
//            int sizeX,
//            int sizeY,
//            int rotate,
//            double pageX,
//            double pageY
//    );
//
//    /* synchronize native methods */
//    private static final Object lock = new Object();
//    private static Field mFdField = null;
//    private int mCurrentDpi;
//
//    public static int getNumFd(ParcelFileDescriptor fdObj) {
//        try {
//            if (mFdField == null) {
//                mFdField = FD_CLASS.getDeclaredField(FD_FIELD_NAME);
//                mFdField.setAccessible(true);
//            }
//
//            return mFdField.getInt(fdObj.getFileDescriptor());
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//            return -1;
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }
//
//
//    /**
//     * Context needed to get screen density
//     */
////    public PdfiumCore(Context ctx) {
////        mCurrentDpi = ctx.getResources().getDisplayMetrics().densityDpi;
//////        Log.d(TAG, "Starting PdfiumAndroid " + BuildConfig.VERSION_NAME);
////    }
//
//    /**
//     * Create new document from file
//     */
//    public PdfDocument newDocument(ParcelFileDescriptor fd) throws IOException {
//        return newDocument(fd, null);
//    }
//
//    /**
//     * Create new document from file with password
//     */
//    public PdfDocument newDocument(ParcelFileDescriptor fd, String password) throws IOException {
//        PdfDocument document = new PdfDocument();
//        document.parcelFileDescriptor = fd;
//        synchronized (lock) {
//            document.mNativeDocPtr = nativeOpenDocument(getNumFd(fd), password);
//        }
//
//        return document;
//    }
//
//    /**
//     * Create new document from bytearray
//     */
//    public PdfDocument newDocument(byte[] data) throws IOException {
//        return newDocument(data, null);
//    }
//
//    /**
//     * Create new document from bytearray with password
//     */
//    public PdfDocument newDocument(byte[] data, String password) throws IOException {
//        PdfDocument document = new PdfDocument();
//        synchronized (lock) {
//            document.mNativeDocPtr = nativeOpenMemDocument(data, password);
//        }
//        return document;
//    }
//
//    /**
//     * Open page and store native pointer in {@link PdfDocument}
//     */
//    public long openPage(PdfDocument doc, int pageIndex) {
//        long pagePtr;
//        synchronized (lock) {
//            pagePtr = nativeLoadPage(doc.mNativeDocPtr, pageIndex);
//            doc.mNativePagesPtr.put(pageIndex, pagePtr);
//            return pagePtr;
//        }
//
//    }
//
//    /**
//     * Open range of pages and store native pointers in {@link PdfDocument}
//     */
//    public long[] openPage(PdfDocument doc, int fromIndex, int toIndex) {
//        long[] pagesPtr;
//        synchronized (lock) {
//            pagesPtr = nativeLoadPages(doc.mNativeDocPtr, fromIndex, toIndex);
//            int pageIndex = fromIndex;
//            for (long page : pagesPtr) {
//                if (pageIndex > toIndex) break;
//                doc.mNativePagesPtr.put(pageIndex, page);
//                pageIndex++;
//            }
//
//            return pagesPtr;
//        }
//    }
//
//    /**
//     * Render page fragment on {@link Surface}.<br>
//     * Page must be opened before rendering.
//     */
//    public void renderPage(PdfDocument doc, Surface surface, int pageIndex,
//                           int startX, int startY, int drawSizeX, int drawSizeY) {
//        renderPage(doc, surface, pageIndex, startX, startY, drawSizeX, drawSizeY, false);
//    }
//
//    /**
//     * Render page fragment on {@link Surface}. This method allows to render annotations.<br>
//     * Page must be opened before rendering.
//     */
//    public void renderPage(PdfDocument doc, Surface surface, int pageIndex,
//                           int startX, int startY, int drawSizeX, int drawSizeY,
//                           boolean renderAnnot) {
//        synchronized (lock) {
//            try {
//                //nativeRenderPage(doc.mNativePagesPtr.get(pageIndex), surface, mCurrentDpi);
//                nativeRenderPage(doc.mNativePagesPtr.get(pageIndex), surface, mCurrentDpi,
//                        startX, startY, drawSizeX, drawSizeY, renderAnnot);
//            } catch (NullPointerException e) {
//                Log.e(TAG, "mContext may be null");
//                e.printStackTrace();
//            } catch (Exception e) {
//                Log.e(TAG, "Exception throw from native");
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Render page fragment on {@link Bitmap}.<br>
//     * Page must be opened before rendering.
//     * <p>
//     * Supported bitmap configurations:
//     * <ul>
//     * <li>ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
//     * <li>RGB_565 - little worse quality, twice less memory usage
//     * </ul>
//     */
//    public void renderPageBitmap(PdfDocument doc, Bitmap bitmap, int pageIndex,
//                                 int startX, int startY, int drawSizeX, int drawSizeY) {
//        renderPageBitmap(doc, bitmap, pageIndex, startX, startY, drawSizeX, drawSizeY, false);
//    }
//
//    /**
//     * Render page fragment on {@link Bitmap}. This method allows to render annotations.<br>
//     * Page must be opened before rendering.
//     * <p>
//     * For more info see {@link PdfiumCore#renderPageBitmap(PdfDocument, Bitmap, int, int, int, int, int)}
//     */
//    public void renderPageBitmap(PdfDocument doc, Bitmap bitmap, int pageIndex,
//                                 int startX, int startY, int drawSizeX, int drawSizeY,
//                                 boolean renderAnnot) {
//        synchronized (lock) {
//            try {
//                nativeRenderPageBitmap(doc.mNativePagesPtr.get(pageIndex), bitmap, mCurrentDpi,
//                        startX, startY, drawSizeX, drawSizeY, renderAnnot);
//            } catch (NullPointerException e) {
//                Log.e(TAG, "mContext may be null");
//                e.printStackTrace();
//            } catch (Exception e) {
//                Log.e(TAG, "Exception throw from native");
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * Map page coordinates to device screen coordinates
//     *
//     * @param doc       pdf document
//     * @param pageIndex index of page
//     * @param startX    left pixel position of the display area in device coordinates
//     * @param startY    top pixel position of the display area in device coordinates
//     * @param sizeX     horizontal size (in pixels) for displaying the page
//     * @param sizeY     vertical size (in pixels) for displaying the page
//     * @param rotate    page orientation: 0 (normal), 1 (rotated 90 degrees clockwise),
//     *                  2 (rotated 180 degrees), 3 (rotated 90 degrees counter-clockwise)
//     * @param pageX     X value in page coordinates
//     * @param pageY     Y value in page coordinate
//     * @return mapped coordinates
//     */
//    public Point mapPageCoordsToDevice(PdfDocument doc, int pageIndex, int startX, int startY, int sizeX,
//                                       int sizeY, int rotate, double pageX, double pageY) {
//        long pagePtr = doc.mNativePagesPtr.get(pageIndex);
//        return nativePageCoordsToDevice(pagePtr, startX, startY, sizeX, sizeY, rotate, pageX, pageY);
//    }
//
//    /**
//     * @return mapped coordinates
//     * @see PdfiumCore#mapPageCoordsToDevice(PdfDocument, int, int, int, int, int, int, double, double)
//     */
//    public RectF mapRectToDevice(PdfDocument doc, int pageIndex, int startX, int startY, int sizeX,
//                                 int sizeY, int rotate, RectF coords) {
//
//        Point leftTop = mapPageCoordsToDevice(doc, pageIndex, startX, startY, sizeX, sizeY, rotate,
//                coords.left, coords.top);
//        Point rightBottom = mapPageCoordsToDevice(doc, pageIndex, startX, startY, sizeX, sizeY, rotate,
//                coords.right, coords.bottom);
//        return new RectF(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y);
//    }
//}
