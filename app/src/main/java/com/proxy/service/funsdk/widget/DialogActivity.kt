package com.proxy.service.funsdk.widget

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.system.screen.CsBarUtils
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityWidgetDialogBinding

/**
 * @author: cangHX
 * @data: 2025/12/24 10:09
 * @desc:
 */
class DialogActivity : BaseActivity<ActivityWidgetDialogBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, DialogActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        CsBarUtils.setNavigationBarTransparent(this)
        CsBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityWidgetDialogBinding {
        return ActivityWidgetDialogBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.show_dialog -> {
//                if (!Settings.canDrawOverlays(this)) {
//                    val intent = Intent(
//                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + CsAppUtils.getPackageName())
//                    )
//                    startActivityForResult(intent, 100)
//                }else {
//                    TestDialog(0).show()
//                }

//                for (index in 0..3) {
                    TestDialog(0).show(this)
//                TestDialog(0).show()
//                }
            }
        }
    }
}