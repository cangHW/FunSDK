package com.proxy.service.widget.info.dialog.window.info

/**
 * @author: cangHX
 * @data: 2025/11/27 11:12
 * @desc:
 */
class DialogConfig {

    /**
     * 是否存在全局焦点, 用户控制是否响应 back 键、弹窗外是否可点击等
     * */
    var focusable: Boolean? = false

    /**
     * 屏幕方向, 全局弹窗生效, 如果是页面内弹窗, 则横竖屏效果自动跟随页面的横竖屏
     * */
    var screenOrientation: ScreenOrientation = ScreenOrientation.ORIENTATION_DEFAULT

    /**
     * 对齐方式
     * */
    var gravity: DialogGravity? = null

    /**
     * 水平间距, 具体是左间距还是右间距由 [gravity] 控制, 单位: px
     * */
    var marginVertical: Int? = null

    /**
     * 垂直间距, 具体是上间距还是下间距由 [gravity] 控制, 单位: px
     * */
    var marginHorizontal: Int? = null

    /**
     * 宽度, 参考: ViewGroup.LayoutParams.MATCH_PARENT、ViewGroup.LayoutParams.WRAP_CONTENT、固定尺寸
     * */
    var width: Int? = null

    /**
     * 高度, 参考: ViewGroup.LayoutParams.MATCH_PARENT、ViewGroup.LayoutParams.WRAP_CONTENT、固定尺寸
     * */
    var height: Int? = null

    override fun toString(): String {
        return "DialogConfig(focusable=$focusable, screenOrientation=${screenOrientation.name}, gravity=${gravity?.name}, marginVertical=$marginVertical, marginHorizontal=$marginHorizontal, width=$width, height=$height)"
    }

}