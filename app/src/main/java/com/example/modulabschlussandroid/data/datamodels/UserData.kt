package com.example.modulabschlussandroid.data.datamodels

data class UserData(
    var userId: String,
    var cityName: String,
    var countInsertedItems: Int,
    var itemsDone: Int,
    var name: String = "MusterMuster",  //Default
    var preName: String = "Mister",     //Default
    var registered: String,
    var streetName: String,
    var streetNumber: String = "1001",  //Default
    var telNumber: String = "",         //Default
    var userName: String = "Mustermann",//Default
    var zipCode: String,

    var showTelNumber: Boolean = false,
    var showStreet: Boolean = false,
    var loggedIn: Boolean = false,
)
