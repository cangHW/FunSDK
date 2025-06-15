package com.proxy.service.document.pdf.base.bean

class PageSize(val widthPixel: Int, val heightPixel: Int) {

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (this === obj) {
            return true
        }
        if (obj is PageSize) {
            return widthPixel == obj.widthPixel && heightPixel == obj.heightPixel
        }
        return false
    }

    override fun toString(): String {
        return widthPixel.toString() + "x" + heightPixel
    }

    override fun hashCode(): Int {
        return heightPixel xor ((widthPixel shl (Integer.SIZE / 2)) or (widthPixel ushr (Integer.SIZE / 2)))
    }
}
