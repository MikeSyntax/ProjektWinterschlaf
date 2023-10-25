package com.example.modulabschlussandroid.data.datamodels



data class Geo(
    val generationtime_ms: Double,
    val results: List<Result>
)

//Hier mussten ? dahinter, da manchmal keine Daten angegeben sind
data class Result(
    val latitude: Double?,
    val longitude: Double?,
    val name: String?
)
