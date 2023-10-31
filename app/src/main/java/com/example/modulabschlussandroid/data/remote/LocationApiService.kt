package com.example.modulabschlussandroid.data.remote

import com.example.modulabschlussandroid.data.datamodels.apicall.geo.Location
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL2 = "https://api.openweathermap.org/geo/1.0/"

//TODO Der Städtename muss sich ändern aber wie
const val ZIP_CODE = "76477"

const val API_KEY2 = "248358c1a32881e0e7ddbffec8c8658c"

//Falls eine Api Token gebraucht wird, muss ein Client gesetzt werden
private val client1 = OkHttpClient
    //der Builder nimmt den Token und erlaubt uns die Anfrage zu stellen
    .Builder()
    //Eine Kette an Anfragen die wir stellen wollen
    .addInterceptor {chain ->
        //Eine Anfrage an den Builder
        val newRequest: Request = chain.request().newBuilder()
            //Header hinzufügen einer Authorisation um mit dem Token daraut zugreifen
            .addHeader("Authorization","Bearer $API_KEY2")
            //und hier wird alles gebaut
            .build()
        //chain steht für den Client und kann hiermit die neue Anfrage stellen
        chain.proceed(newRequest)
        //und bauen
    }.build()

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retroFit: Retrofit = Retrofit.Builder()
    .client(client1)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL2)
    .build()


interface LocationApiService{
    @GET("zip?zip=$ZIP_CODE,DE&appid=$API_KEY2")
    suspend fun getLocationCode(): Location
}

object LocationApiObject{
    val retrofitService2: LocationApiService by lazy { retroFit.create(LocationApiService::class.java) }
}