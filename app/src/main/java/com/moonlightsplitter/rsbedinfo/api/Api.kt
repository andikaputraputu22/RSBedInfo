package com.moonlightsplitter.rsbedinfo.api

import com.moonlightsplitter.rsbedinfo.models.ModelCity
import com.moonlightsplitter.rsbedinfo.models.ModelProvince
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("get-provinces")
    fun getProvinces(): Call<ModelProvince>

    @GET("get-cities")
    fun getCities(@Query("provinceid") provinceid: String): Call<ModelCity>
}