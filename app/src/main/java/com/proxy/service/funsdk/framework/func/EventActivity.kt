package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.message.broadcast.CsBroadcastManager
import com.proxy.service.core.framework.app.message.broadcast.MessageReceiverListener
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/10/29 11:37
 * @desc:
 */
class EventActivity: AppCompatActivity(), MessageReceiverListener {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, EventActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
    }

//    override fun onResume() {
//        super.onResume()
//        Toast.makeText(this, "111", Toast.LENGTH_SHORT).show()
//    }

    fun onClick(view:View){
        when(view.id){
            R.id.set_callback->{
                CsBroadcastManager.addWeakReceiverListener(this)
            }
            R.id.send_event_msg->{
                val bundle = Bundle()
                bundle.putString("ss", "sss")
                CsBroadcastManager.sendBroadcast("com.proxy.service.funsdk2", null, bundle)
            }
        }
    }

    override fun onReceive(context: Context, data: Uri?, extras: Bundle?) {
        Toast.makeText(context, "$extras", Toast.LENGTH_SHORT).show()
    }

}