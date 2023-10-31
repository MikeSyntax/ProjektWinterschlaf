package com.example.modulabschlussandroid.data.datamodels.apicall.distance

import com.example.modulabschlussandroid.data.datamodels.apicall.distance.Distance
import com.example.modulabschlussandroid.data.datamodels.apicall.distance.Duration

data class Element(
    val destination: String?,
    val distance: Distance?,
    val duration: Duration?,
    val origin: String?,
    val status: String?
)
