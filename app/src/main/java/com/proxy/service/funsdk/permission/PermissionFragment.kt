package com.proxy.service.funsdk.permission

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/12/9 17:37
 * @desc:
 */
class PermissionFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_permission, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CsPermission.createRequest()
            ?.addPermission(Manifest.permission.CAMERA)
            ?.start()
    }

}