package com.example.modulabschlussandroid.data.datamodels.apicall.distance

data class DistanceMatrix(
    val destination_addresses: List<String>?,
    val origin_addresses: List<String>?,
    val rows: List<Row>?,
    val status: String?,
)
