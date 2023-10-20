package com.example.modulabschlussandroid.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

//Objekte in der Datenbank mit Änderung des Namens der Datenbank
@Entity(tableName = "your_rental_place")
data class Objects(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val image1Resource: Int,
    val image2Resource: Int,
    val image3Resource: Int,
    val image4Resource: Int,
    val image5Resource: Int,

    val city: String,
    val objectdescription: String,
    val price: Double,
    val description: String,
    val liked: Boolean = false

)