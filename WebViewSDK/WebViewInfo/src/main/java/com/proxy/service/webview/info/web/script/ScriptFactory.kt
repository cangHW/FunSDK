package com.proxy.service.webview.info.web.script

import android.content.Context
import android.webkit.JavascriptInterface
import com.proxy.service.core.framework.context.CsContextManager
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.webview.info.config.Config
import com.proxy.service.webview.info.config.JavascriptInterfaceInfo
import net.bytebuddy.ByteBuddy
import net.bytebuddy.android.AndroidClassLoadingStrategy
import net.bytebuddy.description.annotation.AnnotationDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.implementation.MethodDelegation
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Modifier

/**
 * @author: cangHX
 * @data: 2024/8/1 16:15
 * @desc:
 */
object ScriptFactory {

    private const val TAG = "${Config.LOG_TAG_START}Script"

    fun getJavaScript(list: ArrayList<JavascriptInterfaceInfo>): Any? {
        if (list.size ==0){
            return null
        }
        if (list.size == 1){
            return list[0]
        }
        val annotation = AnnotationDescription.Builder
            .ofType(JavascriptInterface::class.java)
            .build()

        val byteBuddy = ByteBuddy()
            .subclass(Any::class.java)
            .name("com.tal.sc.JavaScriptBridge")

        var methodDefinition: DynamicType.Builder.MethodDefinition<Any>? = null

        list.forEach { info ->
            if (info.isReady()) {
                info.getMethods().forEach { method ->
                    methodDefinition = (methodDefinition ?: byteBuddy)
                        .defineMethod(method.name, Void.TYPE, Modifier.PUBLIC)
                        .withParameters(method.parameterTypes.toMutableList())
                        .intercept(MethodDelegation.to(MethodInterceptor(method, info.getObj())))
                        .annotateMethod(annotation)
                }
            }
        }

        val dynamicType = if (methodDefinition != null) {
            methodDefinition!!.make()
        } else {
            byteBuddy.make()
        }

        if (Config.isDebug) {
            try {
                writeToFile(dynamicType.bytes)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }

        val generatedClass: Class<*> =
            dynamicType.load(
                CsContextManager.getApplication().classLoader,
                AndroidClassLoadingStrategy.Wrapping(CsContextManager.getApplication().cacheDir)
            ).loaded
        return generatedClass.getDeclaredConstructor().newInstance()
    }

    @Throws(IOException::class)
    private fun writeToFile(data: ByteArray) {
        var fos: FileOutputStream? = null
        try {
            fos = CsContextManager.getApplication()
                .openFileOutput("JavaScriptBridge.class", Context.MODE_PRIVATE)
            fos.write(data)
        } finally {
            fos?.close()
        }
    }

}