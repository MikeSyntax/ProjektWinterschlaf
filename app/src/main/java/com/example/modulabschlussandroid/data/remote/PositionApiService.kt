package com.example.modulabschlussandroid.data.remote

import com.example.modulabschlussandroid.data.datamodels.Geo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://geocoding-api.open-meteo.com/v1/"


//TODO Der Code muss sich ändern aber wie
const val CITY_NAME = "Karlsruhe"


private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retroFit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface GeoCoderApiService{
    @GET("search?name=$CITY_NAME&count=1&language=de&format=json")
    suspend fun getGeoCode(): Geo
}

object GeoCoderApiObject{
    val retrofitService: GeoCoderApiService by lazy { retroFit.create(GeoCoderApiService::class.java) }
}