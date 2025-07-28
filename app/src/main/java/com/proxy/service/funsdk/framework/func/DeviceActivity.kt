package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.convert.CsStorageUnit
import com.proxy.service.core.framework.system.device.CsDeviceUtils
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkDeviceBinding

/**
 * @author: cangHX
 * @data: 2024/11/11 14:18
 * @desc:
 */
class DeviceActivity : BaseActivity<ActivityFrameworkDeviceBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, DeviceActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val unit = CsStorageUnit.B_UNIT_1000

    override fun getViewBinding(inflater: LayoutInflater): ActivityFrameworkDeviceBinding {
        return ActivityFrameworkDeviceBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.device_mode -> {
                binding?.content?.addData(
                    "设备信息",
                    "DeviceMode = ${CsDeviceUtils.getDeviceModel()}"
                )
            }

            R.id.device_brand -> {
                binding?.content?.addData(
                    "设备信息",
                    "品牌 = ${CsDeviceUtils.getDeviceBrand()}"
                )
            }

            R.id.device_type -> {
                binding?.content?.addData(
                    "设备信息",
                    "厂商 = ${CsDeviceUtils.getDeviceType()}"
                )
            }

            R.id.rom_type -> {
                binding?.content?.addData(
                    "设备信息",
                    "系统 = ${CsDeviceUtils.getRomType()}"
                )
            }

            R.id.device_memory -> {
                val builder = StringBuilder("\n")
                builder.append("TotalMemory = ")
                    .append(unit.toGbLong(CsDeviceUtils.getTotalMemory()))
                    .append("Gb\n")
                builder.append("AvailMemory = ")
                    .append(unit.toGbLong(CsDeviceUtils.getAvailMemory()))
                    .append("Gb\n")

                binding?.content?.addData("设备信息", "内存信息 $builder")
            }

            R.id.device_storage -> {
                val builder = StringBuilder("\n")
                builder.append("DeviceTotalStorage = ")
                    .append(unit.toGbLong(CsDeviceUtils.getDeviceTotalStorage()))
                    .append("Gb\n")
                builder.append("DeviceAvailStorage = ")
                    .append(unit.toGbLong(CsDeviceUtils.getDeviceAvailStorage()))
                    .append("Gb\n")
                builder.append("ExternalTotalStorage = ")
                    .append(unit.toGbLong(CsDeviceUtils.getExternalTotalStorage()))
                    .append("Gb\n")
                builder.append("ExternalAvailStorage = ")
                    .append(unit.toGbLong(CsDeviceUtils.getExternalAvailStorage()))
                    .append("Gb\n")

                binding?.content?.addData("设备信息", "存储信息 $builder")
            }
        }
    }

}