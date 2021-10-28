package com.moonlightsplitter.rsbedinfo.api

import com.moonlightsplitter.rsbedinfo.models.ModelProvince
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("get-provinces")
    fun getProvinces(): Call<ModelProvince>
}