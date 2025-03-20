package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.convert.CsStorageUnit
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.device.CsDeviceUtils
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/11/11 14:18
 * @desc:
 */
class DeviceActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, DeviceActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.device_mode -> {
                CsLogger.d("DeviceMode = ${CsDeviceUtils.getDeviceModel()}")
            }

            R.id.device_storage -> {
                CsLogger.d(
                    "" +
                            "DeviceTotalStorage = ${
                                CsStorageUnit.B_UNIT_1000.toGbLong(
                                    CsDeviceUtils.getDeviceTotalStorage()
                                )
                            }, " +
                            "DeviceAvailStorage = ${
                                CsStorageUnit.B_UNIT_1000.toGbLong(
                                    CsDeviceUtils.getDeviceAvailStorage()
                                )
                            }, " +
                            "ExternalTotalStorage = ${
                                CsStorageUnit.B_UNIT_1000.toGbLong(
                                    CsDeviceUtils.getExternalTotalStorage()
                                )
                            }, " +
                            "ExternalAvailStorage = ${
                                CsStorageUnit.B_UNIT_1000.toGbLong(
                                    CsDeviceUtils.getExternalAvailStorage()
                                )
                            }, "
                )
            }
        }
    }

}