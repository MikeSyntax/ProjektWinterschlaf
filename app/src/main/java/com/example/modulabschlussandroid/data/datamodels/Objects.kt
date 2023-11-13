package com.example.modulabschlussandroid.data.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//Objekte in der Datenbank mit Änderung des Namens der Datenbank
@Entity
data class Objects(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    //Bilder 1-5
    var image1Resource: Int = 0,        //Default
    var image2Resource: Int = 0,        //Default
    var image3Resource: Int = 0,        //Default
    val image4Resource: Int = 0,        //Default
    val image5Resource: Int = 0,        //Default


    //Stadt
    var city: String = "",
    var doneCityChoice: Boolean = false,
    //Überschrift/Titel
    var objectdescription: String = "",
    var doneObjectDescriptionChoice: Boolean = false,
    //Preis
    var price: Double = 0.0,
    var donePriceChoice: Boolean = false,
    //Beschreibung
    var description: String = "",
    var doneDescriptionChoice: Boolean = false,


    //@ColumnInfo(name = "zipCode", defaultValue = "0")
    //Postleitzahl
    var zipCode: String = "",
    var doneZipCodeChoice:  Boolean = false,
    //Favorit
    var liked: Boolean = false,


    //Kategorien
    var doneCategoryChoice: Boolean = false,                //Kategorie bestätigt
    var garage: Boolean = false,                            //Garage
    var parkingSpot: Boolean = false,                       //Parkplatz
    var parkingSpace: Boolean = false,                      //Stellplatz
    var camperParkingInside: Boolean = false,               //Wohnmobil Stellplatz innen
    var camperParkingOutside: Boolean = false,              //Wohnmobil Stellplatz außen
    var underGroundParking: Boolean = false,                //Tiefgarage
    var storage: Boolean = false,                           //Lager
    var storageHall: Boolean = false,                       //Lagerhalle
    var storageRoom: Boolean = false,                       //Lagerraum
    var storageBox: Boolean = false,                        //Lagerbox
    var container: Boolean = false,                         //Container
    var carport: Boolean = false,                           //Carport
    var basement: Boolean = false,                          //Keller
    var openSpace: Boolean = false,                         //Freifläche
    var barn: Boolean = false,                              //Scheune
    var yard: Boolean = false,                              //Hof

)