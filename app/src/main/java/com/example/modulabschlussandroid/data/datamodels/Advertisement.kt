package com.example.modulabschlussandroid.data.datamodels

import com.google.firebase.firestore.DocumentSnapshot

data class Advertisement(

    var objectId: String? = null,
    var userId: String? = null,
    var zipCode: String? = null,
    var city: String? = null,
    var title: String? = null,
    var description: String? = null,
    var price: String? = null,

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


) {
    constructor(dataSnapshot: DocumentSnapshot) : this() {
        objectId = dataSnapshot["objectId"].toString()
        zipCode = dataSnapshot["zipCode"].toString()
        city = dataSnapshot["city"].toString()
        title = dataSnapshot["title"].toString()
        userId = dataSnapshot["userId"].toString()
        description = dataSnapshot["description"].toString()
        price = dataSnapshot["price"].toString()
    }
}