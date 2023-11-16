package com.example.modulabschlussandroid.repositorys

import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.modulabschlussandroid.data.datamodels.Advertisement
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RepositoryFirebase(

) {

    private var firebaseAuth = FirebaseAuth.getInstance()

    private var database = FirebaseDatabase.getInstance()

    private var fireStoreDatabase = FirebaseFirestore.getInstance()

//Firebase Firestore================================================================================

    //LiveData für den aktuellen user und alle Daten über den User
    private var _currentUser: MutableLiveData<PersonalData> = MutableLiveData()
    val currentUser: LiveData<PersonalData>
        get() = _currentUser

    //Funktion um den aktuellen User upzudaten und die Daten aus dem Firestore zu holen
    fun updateCurrentUserFromFirestore(uId: String) {
        fireStoreDatabase = Firebase.firestore
        Log.d("Firebase Repo Store", "$uId User Id ready for input FireStore")
        fireStoreDatabase.collection("user").document(uId)
            .get()
            .addOnSuccessListener { thisUser ->
                Log.d("Firebase Repo Store", "SuccesListener FireStore done")
                _currentUser.value = PersonalData(
                    thisUser.id,
                    thisUser.data?.get("cityName").toString(),
                    thisUser.data?.get("countInsertedItems").toString().toInt(),
                    thisUser.data?.get("itemsDone").toString().toInt(),
                    thisUser.data?.get("name").toString(),
                    thisUser.data?.get("preName").toString(),
                    thisUser.data?.get("registered").toString(),
                    thisUser.data?.get("streetName").toString(),
                    thisUser.data?.get("streetNumber").toString(),
                    thisUser.data?.get("telNumber").toString(),
                    thisUser.data?.get("userName").toString(),
                    thisUser.data?.get("zipCode").toString()
                )
                // Log.e("Repo", " User Data set failed in firestore")
            }.addOnFailureListener { Log.e("Firebase Repo Store", "$it") }
    }
//Firebase Authentication===========================================================================

//den aktuellen User zurückgeben


    //Live Data des aktuellen Users
    private var _uId: MutableLiveData<String> = MutableLiveData()
    val uId: LiveData<String>
        get() = _uId

    //Update des aktuellen Users
    fun showCurrentUserId(): String {
        val user = firebaseAuth.currentUser?.uid.toString()
        Log.d("Firebase Repo Auth", "$user User-Id in firebase Auth")
        _uId.value = user
        Log.d("Firebase Repo Auth", "$currentUser User-Id in firebase Auth")
        return uId.value.toString()
    }

    //Ausloggen des aktuellen Users
    fun signOutUser() {
        //Ausloggen
        firebaseAuth.signOut()
    }

//Firebase Database=================================================================================

    //Das erstellte Objekt soll an die Firebase Datenbank gesendet werden
    fun saveItemToDatabase(advertisement: Advertisement) {

        //und eine Reference setzten in der Kategorie myObjects
        val ref = database.getReference("objectsOnline")
        Log.d("Firebase Repo Data", "Reference $ref")

        //Hier wird jedesmal wenn es aufgerufen wird eine Id gesetzt
        val objectId = ref.push().key
        Log.d("Firebase Repo Data", "objectId $objectId")

        //hier wird in der Database das Objekt gesetzt bzw. erschaffen und noch ein CompleteListener zum überprüfen
        ref.child(objectId!!).setValue(advertisement)
            //Erfolgreich???
            .addOnSuccessListener {
                Log.d("Firebase Repo Data", "Data inserted successfully")
                //Fehler???
            }.addOnFailureListener {
                Log.e("Firebase Repo Data", "inserted failed $it")
            }
    }

    //NEU
    fun readDatabase(): Advertisement {
        //Counter für die Anzeigen welche der User gerade online hat
        var count: Int = 0
        //und eine Reference setzten in der Kategorie objectsOnline
        val ref = database.getReference("objectsOnline")
        Log.d("Firebase Repo Data", "Reference $ref")
//TODO TODO TODO Wieviele Anzeigen dieses Users muss noch gemacht werden
//Abfrage wieviele Anzeigen im Moment online sind
        val result = ref.get().addOnSuccessListener {
            Log.d("Firebase Repo Data", "Anzahl Kinder ${it.childrenCount}")
            it.childrenCount
        }
        // count = result.toString().toInt()
        ref.get().addOnSuccessListener {
            Log.d("Firebase Repo Data", "Success $it")
                    for (snapshot in it.children) {
                        Log.d("Firebase Repo Data Schleife", "Alle id´s ${snapshot.child("objectId").value}")
                        Advertisement(snapshot)
                    }
        }.addOnFailureListener {
            Log.d("Firebase Repo Data", "Error $it")
        }
        return Advertisement()
    }
}
//==================================================================================================
