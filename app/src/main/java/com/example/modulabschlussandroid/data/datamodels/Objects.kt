package com.example.modulabschlussandroid.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

//Objekte in der Datenbank mit Änderung des Namens der Datenbank
@Entity
data class Objects(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var image1Resource: Int,
    var image2Resource: Int,
    var image3Resource: Int,
    val image4Resource: Int,
    val image5Resource: Int,

    var city: String,
    var objectdescription: String,
    var price: Double,
    var description: String,
    var liked: Boolean = false

)