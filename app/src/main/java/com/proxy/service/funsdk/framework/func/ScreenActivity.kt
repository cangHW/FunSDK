package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsBarUtils
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/10/18 18:25
 * @desc:
 */
class ScreenActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ScreenActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework_screen)

//        CsBarUtils.setStatusBarTransparent(this)
//        CsBarUtils.setNavigationBarTransparent(this)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.screen_width -> {
                Toast.makeText(this, "屏幕宽度：${CsScreenUtils.getScreenWidth()}", Toast.LENGTH_SHORT).show()
            }

            R.id.screen_height -> {
                Toast.makeText(this, "屏幕高度：${CsScreenUtils.getScreenHeight()}", Toast.LENGTH_SHORT).show()
            }

            R.id.screen_real_size -> {
                val info = CsScreenUtils.getScreenRealInfo().toString()
                Toast.makeText(this, "屏幕真实信息：$info", Toast.LENGTH_SHORT).show()
                CsLogger.d(info)
            }

            R.id.status_bar_height -> {
                Toast.makeText(this, "状态栏高度：${CsScreenUtils.getStatusBarHeight()}", Toast.LENGTH_SHORT).show()
            }

            R.id.navigation_bar_height -> {
                Toast.makeText(this, "导航栏高度：${CsScreenUtils.getNavigationBarHeight()}", Toast.LENGTH_SHORT).show()
            }

            R.id.action_bar_height -> {
                Toast.makeText(this, "标题栏高度：${CsScreenUtils.getActionBarHeight()}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}