package com.proxy.service.document.base.pdf.info

class PageSize(val width: Int, val height: Int) {

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (this === obj) {
            return true
        }
        if (obj is PageSize) {
            return width == obj.width && height == obj.height
        }
        return false
    }

    override fun toString(): String {
        return width.toString() + "x" + height
    }

    override fun hashCode(): Int {
        return height xor ((width shl (Integer.SIZE / 2)) or (width ushr (Integer.SIZE / 2)))
    }
}
