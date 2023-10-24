package com.example.modulabschlussandroid.data.remote

import com.example.modulabschlussandroid.data.datamodels.GeoCoderApi
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET

const val BASE_URL = "http://api.openweathermap.org/geo/1.0/"

const val API_TOKEN = "334e22b2ed48492fbbc52db74902a8e7"

//Variable der Postleitzahl
val zipCode: Objects
    get() {
        zipCode.zipCode
        return zipCode
    }

const val zipCodeObject = "zipCode"


val client = OkHttpClient.Builder().addInterceptor {
    chain ->
    val request = chain.request().newBuilder()
        .addHeader("Authorization", "Bearer")
        .build()
    chain.proceed(request)
} .build()


private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retroFit: Retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface GeoCoderApiService{
    @GET("zip?zip=$zipCodeObject,DE&appid=$API_TOKEN")
    suspend fun getGeoCode():GeoCoderApi

}

object GeoCoderApi{
    val retrofitService: GeoCoderApiService by lazy { retroFit.create(GeoCoderApiService::class.java) }
}