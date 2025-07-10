package com.proxy.service.funsdk.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.proxy.service.widget.info.statepage.CsStatePageManager
import com.proxy.service.widget.info.statepage.config.IStatePageController
import java.lang.reflect.ParameterizedType

/**
 * @author: cangHX
 * @data: 2025/6/17 11:48
 * @desc:
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    protected var binding: T? = null
    protected var statePage: IStatePageController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateLayout()

        binding?.let {
            statePage = CsStatePageManager.inflate(it.root)
            setContentView(statePage?.getRootView())
        }

        initView()
    }

    @Suppress("UNCHECKED_CAST")
    private fun inflateLayout(): T {
        val clazz: Class<T> = getGenericClass()
        val method = clazz.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java
        )
        return method.invoke(null, LayoutInflater.from(this)) as T
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getGenericClass(): Class<T> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }

    protected open fun initView() {}

    abstract fun onClick(view: View)
}