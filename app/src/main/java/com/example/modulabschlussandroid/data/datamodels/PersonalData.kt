package com.example.modulabschlussandroid.data.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

/* Dies ist der Konstruktor der Klasse SecretData und er erhält Parameter, welche beim Erstellen von SecretData-Objekten übergeben werden. */
@Entity
data class PersonalData(
    @PrimaryKey(autoGenerate = false)
    var userName: String = "Mustermann",
    var email: String = "mustermann@web.de",
    var password: String = "1234",

    var loggedIn: Boolean = false
)
