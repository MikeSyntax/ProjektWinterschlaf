package com.example.modulabschlussandroid.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.modulabschlussandroid.data.datamodels.Advertisement
import com.example.modulabschlussandroid.data.datamodels.apicall.geo.Geo
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.data.datamodels.apicall.distance.DistanceMatrix
import com.example.modulabschlussandroid.data.exampledata.ObjectsExampleData
import com.example.modulabschlussandroid.data.local.ObjectDatabase
import com.example.modulabschlussandroid.data.remote.DistanceApiObject
import com.example.modulabschlussandroid.data.remote.GeoCoderApiObject
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

//Repository Pattern nur eine Datenquelle für die ganze App um immer die gleichen Daten zu haben====
//Das Repository ist das Modell von MVVM
class RepositoryObjects(

    //Verbindung zur Datenbank
    private val database: ObjectDatabase,

    //Verbindung zum Objekt in der PositionApiService
    private val api: GeoCoderApiObject,
    //private val api2: LocationApiObject,

    //Verbindung zum Objekt in der DistanceApiService
    private val apiDistance: DistanceApiObject,

    ) {

//LiveData ObjectList===============================================================================

    //Alle Objekte in der Datenbank als LiveData anzeigen
    val objectList: LiveData<List<Objects>> = database.objectDao.showALL()

//Geliked Objects===================================================================================

    //Alle favorisierten Objekte in der Datenbank als LiveData anzeigen
    val likedObjects: LiveData<List<Objects>> = database.objectDao.showALLLikedObjects()

//Postleitzahlensuche Objekte als LiveData==========================================================

    //Nullable LiveData der _zipObjects
    private var _zipObjects: MutableLiveData<List<Objects>?> = MutableLiveData()
    val zipObjects: LiveData<List<Objects>?>
        get() = _zipObjects

//Firebase Firestore Userdaten (Name Adresse Benutzer usw.==========================================

    //Verbindung zum Firebase Repository
    private val firebaseRepository = RepositoryFirebase()

    val countAdvertises = firebaseRepository.countAdvertises
    fun countAdvertises(){
        firebaseRepository.countAdvertises()
    }

    //LiveData des aktuellen Users
    var currentUser = firebaseRepository.currentUser

    //Funktion um den aktuellen User upzudaten und die Daten aus dem Firestore zu holen
    fun updateCurrentUserFromFirestore() {
       firebaseRepository.updateCurrentUserFromFirestore()
    }

    fun newUserDataFirstSignIn(personalData: PersonalData){
        firebaseRepository.newUserDataFirstSignIn(personalData)
    }

//Firebase Authentication User ID (integer) ========================================================

    //Live Data des aktuellen Users dessen Id
    var uId = firebaseRepository.uId

    //Funktion um den aktuellen User anhand seiner Id zu identifizieren aus der Authentication
    fun showCurrentUserId(){
       firebaseRepository.showCurrentUserId()
        Log.d("Repo Objects", "User Id ${uId.value}")

    }

    //Ausloggen des aktuellen Users
    fun signOutUser(){
        //Ausloggen
       firebaseRepository.signOutUser()
    }

//Firebase Database ================================================================================



    //Inserieren eines neuen Advertisments (Objekt)
    fun saveItemToDatabase(advertisement: Advertisement) {
        firebaseRepository.saveItemToDatabase(advertisement)
    }

    //Auslesen der Datenbank von Firebase
    fun readDatabase(){
        firebaseRepository.readDatabase()
    }

    val allAdvertises = firebaseRepository.allAdvertises

//Funktion für die Schnellsuche über Postleitzahl===================================================

    //Anzeige aller Objekte mit einer bestimmten Postleitzahl
    fun getZipCodeObject(zip: String) {
        try {
            //Die Objekt Liste wird nach Zahlen gefilter, welche über die Suche eingegeben werden
            val zipResults = objectList.value?.filter {
                it.zipCode.contains(zip)
            }
            if (zipResults == null) {
                _zipObjects.value = emptyList()
            } else {
                _zipObjects.value = zipResults
            }
            //Log.d("success Repo", "$zip input Text - ${_zipObjects.value} zipObjects - $zipResults zipObjects")
        } catch (e: Exception) {
           // Log.e("Repository", "getZipCodeObject failed")
            _zipObjects.value = emptyList()
        }
    }

//GEOdata===========================================================================================

    //LiveData der GeoDaten Abfrage über einen API Call
    private val _geoResult: MutableLiveData<Geo> = MutableLiveData()
    val geoResult: LiveData<Geo>
        get() = _geoResult

    //API Call starten
    suspend fun getGeoResult(city: String) {
        try {
            val geo = api.retrofitService.getGeoCode(city, 1, "de", "json")
            _geoResult.value = geo
            //   _geoResult.value = api2.retrofitService2.getLocationCode()
        } catch (e: Exception) {
            //Log.e("Repository", "$e - getGeoResult API Call failed")
        }
    }
//DistanceApiData===================================================================================

    //LiveData der DistanceData Abfrage über einen Api Call
    private val _distanceData: MutableLiveData<DistanceMatrix> = MutableLiveData()
    val distanceData: LiveData<DistanceMatrix>
        get() = _distanceData

    suspend fun getDistanceData(origins: String, destinations: String) {
        try {
            val data = apiDistance.retrofitService3.getDistance(origins, destinations)
            //Log.d("success Repository", "$destinations für Ziel, $origins für Start")
            _distanceData.value = data
        } catch (e: Exception) {
           // Log.e("Repository", "$e - getDistance API Call failed")
        }
    }

//Datenbank anlegen=================================================================================

    //Falls die Datenbank noch leer ist, einmal bitte alle Objekte hineinladen
    suspend fun loadAllObjects() {
        val data = ObjectsExampleData
        try {
            if (database.objectDao.countObjects() == 0) {
                database.objectDao.insertObject(data.object1)
                database.objectDao.insertObject(data.object2)
                database.objectDao.insertObject(data.object3)
                database.objectDao.insertObject(data.object4)
                database.objectDao.insertObject(data.object5)
                database.objectDao.insertObject(data.object6)
                database.objectDao.insertObject(data.object7)
                database.objectDao.insertObject(data.object8)
                database.objectDao.insertObject(data.object9)
                database.objectDao.insertObject(data.object10)
                database.objectDao.insertObject(data.object11)
            }
        } catch (e: Exception) {
           // Log.e("Repository", "$e loadAllObjects failed")
        }
    }

    //Update eines Objektes mit Änderungen
    suspend fun updateObject(objects: Objects) {
        try {
            database.objectDao.updateObject(objects)
        } catch (e: Exception) {
           // Log.e("Repository", "updateObject failed")
        }
    }

    //Einfügen eines Objektes
    suspend fun insertObject(objects: Objects) {
        try {
            database.objectDao.insertObject(objects)
        } catch (e: Exception) {
          //  Log.e("Repository", "insertObject failed")
        }
    }


    //Löschen eines einzigen Objektes mit Kennung der id
    suspend fun deleteById(id: Long) {
        try {
            database.objectDao.deleteById(id)
        } catch (e: Exception) {
           // Log.e("Repository", "deleteById failed")
        }
    }

    //Löschen aller Objekte
    suspend fun deleteAll() {
        try {
            database.objectDao.deleteALL()
        } catch (e: Exception) {
          //  Log.e("Repository", "deleteAll failed")
        }
    }

//==================================================================================================

    //Update eines Objektes mit Änderungen
    suspend fun updatePersonalData(personalData: PersonalData) {
        try {
            database.userDataDao.updateUser(personalData)
        } catch (e: Exception) {
           // Log.e("Repository", "updateObject failed")
        }
    }

    suspend fun insertPersonalData(personalData: PersonalData) {
        try {
            database.userDataDao.insertUserData(personalData)
        } catch (e: Exception) {
           // Log.e("Repository", "updateObject failed")
        }
    }
}
