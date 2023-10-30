package com.example.modulabschlussandroid.data.remote

import com.example.modulabschlussandroid.data.datamodels.apicall.Geo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

//erster Hauptteil der Api Call Adresse, der zweite steht im Interface
const val BASE_URL = "https://geocoding-api.open-meteo.com/v1/"

//Umwandlung der Kotlin Anfrage in Json Format
private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Umwandlung der zurückgegebenen Json-Datei in Kotlin Code
private val retroFit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

//Start des GeoCode Abfrage mit Übergabe der Stadt als Parameter, mit diesem retrofit2 Paket ist die individuelle Übergabe der Stadt zur Suche möglich,
//da sonst nur Konstanten vom Interface geduldet worden, war dieser kleine Umweg nötig, mit @Path hat es leider nicht funktioniert
interface GeoCoderApiService {
    @GET("search")
    // retrofit2 ein Paket, das zu der Retrofit-Bibliothek gehört, die normalerweise für die Erstellung von HTTP-Anfragen in Android-Anwendungen verwendet wird.
    // Das @retrofit2.http.Query ist eine Annotation aus diesem Paket und wird verwendet, um Abfrageparameter für HTTP-Anfragen zu deklarieren.
    suspend fun getGeoCode(@retrofit2.http.Query("name") city: String, @retrofit2.http.Query("count") count: Int, @retrofit2.http.Query("language") language: String, @retrofit2.http.Query("format") format: String): Geo
}

//Erstellen eines Objektes
object GeoCoderApiObject{
    val retrofitService: GeoCoderApiService by lazy { retroFit.create(GeoCoderApiService::class.java) }
}
