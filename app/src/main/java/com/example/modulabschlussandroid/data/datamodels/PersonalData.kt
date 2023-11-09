package com.example.modulabschlussandroid.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

/* Dies ist der Konstruktor der Klasse SecretData und er erhält Parameter, welche beim Erstellen von SecretData-Objekten übergeben werden. */
@Entity
data class PersonalData(
    @PrimaryKey(autoGenerate = false)
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

    //Zeit von Firebase
    //var time: Timestamp

    //var email: String = "mustermann@web.de",
    //var password: String = "1234",
)
