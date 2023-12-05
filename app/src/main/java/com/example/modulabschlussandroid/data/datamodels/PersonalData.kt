package com.example.modulabschlussandroid.data.datamodels

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot

/* Dies ist der Konstruktor der Klasse SecretData und er erhält Parameter, welche beim Erstellen von SecretData-Objekten übergeben werden. */
@Entity
data class PersonalData(
    @PrimaryKey(autoGenerate = false)
    var userId: String = "",
    var cityName: String? = null,
    var itemsDone: String? = null,
    var name: String? = null,
    var preName: String? = null,
    var streetName: String? = null,
    var streetNumber: String ? = null,
    var telNumber: String? = null,
    var userName: String? = null,
    var zipCode: String? = null,
    var profileImage: String? = null,
    var userDataComplete: Boolean = false
    )