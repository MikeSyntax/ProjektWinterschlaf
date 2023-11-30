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
    var countInsertedItems: String? = null,
    var itemsDone: String? = "0",
    var name: String? = null,
    var preName: String? = null,
    var registered: String? = null,
    var streetName: String? = null,
    var streetNumber: String ? = null,
    var telNumber: String? = null,
    var userName: String? = null,
    var zipCode: String? = null,
    var profileImage: String? = null,

    var showTelNumber: Boolean = false,
    var showStreet: Boolean = false,
    var loggedIn: Boolean = false,

    ) {

    companion object {
        fun fromFirebase(snapshot: DocumentSnapshot): PersonalData{
            return PersonalData(
                snapshot["userID"] as String,
                snapshot["cityName"] as String?,
                snapshot["countInsertedItems"] as String?,
                snapshot["itemsDone"] as String?,
                snapshot["name"] as String?,
                snapshot["preName"] as String?,
                snapshot["registered"] as String?,
                snapshot["streetName"] as String?,
                snapshot["streetNumber"] as String?,
                snapshot["telNumber"] as String?,
                snapshot["userName"] as String?,
                snapshot["zipCode"] as String?,
                snapshot["profileImage"] as String?,
            )
        }
    }

    fun toFirebase(): Map<String, *>{
        return mapOf(
            "userId" to userId,
            "cityName" to cityName,
            "countInsertedItems" to countInsertedItems,
            "itemsDone" to itemsDone,
            "name" to name,
            "preName" to preName,
            "registered" to registered,
            "streetName" to streetName,
            "streetNumber" to streetNumber,
            "telNumber" to telNumber,
            "userName" to userName,
            "zipCode" to zipCode,
            "profileImage" to profileImage
        )
    }
}