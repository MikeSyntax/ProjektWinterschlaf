package com.example.modulabschlussandroid.data.remote

import com.example.modulabschlussandroid.data.datamodels.apicall.distance.DistanceMatrix
import com.example.modulabschlussandroid.data.datamodels.apicall.geo.Geo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//erster Teil der Api AnforderungsAdresse
const val BASE_URL3 = "https://api.distancematrix.ai/maps/api/distancematrix/"

//das ist der geforderte Api Token
const val API_KEY3 = "qgQ99hz5t2QHFUsoRlsatZMcI9ZZxlgvMEHaGdw7L7byRBC99xVC7GuAvUVlev11"

//Falls eine Api Token gebraucht wird, muss ein Client gesetzt werden
private val client = OkHttpClient
    //der Builder nimmt den Token und erlaubt uns die Anfrage zu stellen
    .Builder()
    //Eine Kette an Anfragen die wir stellen wollen
    .addInterceptor { chain ->
        //Eine Anfrage an den Builder
        val newRequest: Request = chain.request().newBuilder()
            //Header hinzufügen einer Authorisation um mit dem Token daraut zugreifen
            .addHeader("Authorization", "Bearer $API_KEY3")
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
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL3)
    .build()


interface DistanceApiService {
    @GET("json")
    suspend fun getDistance(
        @Query("origins") origins: String,
        @Query("destinations") destinations: String,
        @Query("key") apiKey: String = API_KEY3
    ): DistanceMatrix
}

object DistanceApiObject {
    val retrofitService3: DistanceApiService by lazy {
        retroFit.create(DistanceApiService::class.java)
    }
}

