package com.proxy.service.funsdk.apihttp

import com.proxy.service.apihttp.base.annotation.CsRetryWithDelay
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author: cangHX
 * @data: 2024/6/19 11:06
 * @desc:
 */
interface Api {

    @CsRetryWithDelay
    @GET("mock/3509/ebook/v1/bookrack/list_del")
    fun request():Call<ApiResponse>

}