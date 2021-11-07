package com.moonlightsplitter.rsbedinfo.api

import com.moonlightsplitter.rsbedinfo.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("get-provinces")
    fun getProvinces(): Call<ModelProvince>

    @GET("get-cities")
    fun getCities(@Query("provinceid") provinceid: String): Call<ModelCity>

    @GET("get-hospitals")
    fun getHospitals(@Query("provinceid") provinceid: String,
                    @Query("cityid") cityid: String,
                    @Query("type") type: String): Call<ModelHospital>

    @GET("get-bed-detail")
    fun getBedDetail(@Query("hospitalid") hospitalid: String,
                    @Query("type") type: String): Call<ModelBedDetail>

    @GET("get-hospital-map")
    fun getHospitalMaps(@Query("hospitalid") hospitalid: String): Call<ModelMaps>
}