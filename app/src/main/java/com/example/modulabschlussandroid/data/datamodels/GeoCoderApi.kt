package com.example.modulabschlussandroid.data.datamodels

data class GeoCoderApi(
    val country: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val zip: String
)