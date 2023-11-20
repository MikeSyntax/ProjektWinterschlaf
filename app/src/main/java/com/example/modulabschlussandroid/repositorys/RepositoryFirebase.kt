package com.example.modulabschlussandroid.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.modulabschlussandroid.data.datamodels.Advertisement
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import okhttp3.internal.cache.DiskLruCache

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

    //Live Data des aktuellen Users Id
    private var _uId: MutableLiveData<String> = MutableLiveData()
    val uId: MutableLiveData<String>
        get() = _uId

    //Update der aktuellen Users Id
    fun showCurrentUserId(){
        val user = firebaseAuth.currentUser?.uid
        _uId.value = user.toString()
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
       // Log.d("Firebase Repo Data", "Reference $ref")

        //Hier wird jedesmal wenn es aufgerufen wird eine Id gesetzt
        val objectId = ref.push().key
       // Log.d("Firebase Repo Data", "objectId $objectId")

        //hier wird in der Database das Objekt gesetzt bzw. erschaffen und noch ein CompleteListener zum überprüfen
        ref.child(objectId!!).setValue(advertisement)
            //Erfolgreich???
            .addOnSuccessListener {
              //  Log.d("Firebase Repo Data", "Data inserted successfully")
                //Fehler???
            }.addOnFailureListener {
             //   Log.e("Firebase Repo Data", "inserted failed $it")
            }
    }

//Abfrage in der Firebase Database wieviele Anzeigen im Moment online sind==========================

    //LiveData für den aktuellen user und alle Daten über den User
    private var _countAdvertises: MutableLiveData<String> = MutableLiveData()
    val countAdvertises: LiveData<String>
        get() = _countAdvertises

    //Funktion zur Abfrage der aktuellen UserAnzeigen
    fun countAdvertises() {
        //und eine Reference setzten in der Kategorie objectsOnline
        val ref = database.getReference("objectsOnline")
        ref.get().addOnSuccessListener {
                    _countAdvertises.postValue("Inserate online ${it.childrenCount}")
        }
    }

//Abfrage in der Firebase Database und erstellen eines Advertisments aller Anzeigen online==========

    //Live Data für die ausgelesenenen Advertisments
    private var _allAdvertises: MutableLiveData<List<Advertisement>> = MutableLiveData()
    val allAdvertises: LiveData<List<Advertisement>>
        get () = _allAdvertises


    fun readDatabase() {
        //Leere Liste für die Advertises
         val advertise: MutableList<Advertisement> =  mutableListOf()
        //und eine Reference setzten in der Kategorie objectsOnline
        val ref = database.getReference("objectsOnline")
        //Log.d("Firebase Repo Data", "Reference $ref")
        //bekomme einen SuccessListener für jeden ....
        ref.get().addOnSuccessListener {
            //Log.d("Firebase Repo Data", "Success $it")
            //For Schleife also für jedes children in der Datenbank...
            for (snapshot in it.children) {
                //Log.d("Firebase Repo Data Schleife", "Alle id´s ${snapshot.child("objectId").value}")
               //Füge dieses ausgelesene der advertise Lise hinzu
                advertise.add(Advertisement(snapshot))
                //filtere die Liste nach der UserId und füge die in Übereinstimmung der neuen Liste hinzu
                val filteredAds = advertise.filter {myAds ->
                    //vergleiche die Ids
                    myAds.userId == uId.value
                }
                //die gefilterte Liste mit dem Live Data setzen
                _allAdvertises.value = filteredAds
                Log.d("Firebase Repo Data Schleife", "Alle id´s ${allAdvertises.value}")
            }
        }.addOnFailureListener {
            //Log.d("Firebase Repo Data", "Error $it")
        }
    }
}
//==================================================================================================
