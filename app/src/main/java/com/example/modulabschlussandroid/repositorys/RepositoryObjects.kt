package com.example.modulabschlussandroid.repositorys

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.modulabschlussandroid.data.datamodels.Advertisement
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.data.datamodels.apicall.distance.DistanceMatrix
import com.example.modulabschlussandroid.data.datamodels.apicall.geo.Geo
import com.example.modulabschlussandroid.data.datamodels.chat.Message
import com.example.modulabschlussandroid.data.exampledata.ObjectsExampleData
import com.example.modulabschlussandroid.data.local.ObjectDatabase
import com.example.modulabschlussandroid.data.remote.DistanceApiObject
import com.example.modulabschlussandroid.data.remote.GeoCoderApiObject
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch

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

//==================================================================================================
//LiveData Verbindung zum Firebase Repository=======================================================
//==================================================================================================

    //Verbindung zum Firebase Repository
    private val firebaseRepository = RepositoryFirebase()

    val countAdvertises = firebaseRepository.countAdvertises

    //LiveData des aktuellen Users
    var currentUser = firebaseRepository.currentUser

    //LiveData des aktuellen Users ob er eingeloggt ist oder nicht
    var currentAppUser = firebaseRepository.currentAppUser

    //Live Data des aktuellen Users dessen Id
    var uId = firebaseRepository.uId

    //LiveData erkennen der aktuellen AdvertismentId
    val currentAdvertisementId = firebaseRepository.currentAdvertisementId

    //LiveData alle meine eigenen Inserate
    val allMyAdvertises = firebaseRepository.allMyAdvertises

    //LiveData für die Nachrichten einer bestimmten Advertisement Id
    val myMessage = firebaseRepository.myMessage

//==================================================================================================
//LiveData==========================================================================================
//==================================================================================================

    //Alle Objekte in der Datenbank als LiveData anzeigen
    val objectList: LiveData<List<Objects>> = database.objectDao.showALL()

    //LiveData Geliked Objects
    //Alle favorisierten Objekte in der Datenbank als LiveData anzeigen
    val likedObjects: LiveData<List<Objects>> = database.objectDao.showALLLikedObjects()

    //LiveData Postleitzahlensuche Objekte
    //Nullable LiveData der _zipObjects
    private var _zipObjects: MutableLiveData<List<Objects>?> = MutableLiveData()
    val zipObjects: LiveData<List<Objects>?>
        get() = _zipObjects

    //LiveData Eingegebener Text für die Postleitzahlen Suche
    private val _inputText = MutableLiveData<String>()
    val inputText: LiveData<String>
        get() = _inputText

    //LiveData mit dem aktuellen Objekt (Room Datenbank)
    private val _currentObject: MutableLiveData<Objects> = MutableLiveData()
    val currentObject: LiveData<Objects>
        get() = _currentObject

    //LivaData mit dem aktuellen Advertisments (Firebase Datenbank)
    private val _currentAdvertisment: MutableLiveData<Advertisement> = MutableLiveData()
    val currentAdvertisement: LiveData<Advertisement>
        get() = _currentAdvertisment

    //LiveData der GeoDaten Abfrage über einen API Call
    private val _geoResult: MutableLiveData<Geo> = MutableLiveData()
    val geoResult: LiveData<Geo>
        get() = _geoResult

    //LiveData der DistanceData Abfrage über einen Api Call
    private val _distanceData: MutableLiveData<DistanceMatrix> = MutableLiveData()
    val distanceData: LiveData<DistanceMatrix>
        get() = _distanceData

//==================================================================================================
//Firebase Storage Funktionen aus dem Repository MVVM================================================
//==================================================================================================

    //Funktion um Bilder in das Firebase Storage hochzuladen========================================
    fun uploadImagetoStorage(uri: Uri) {
        firebaseRepository.uploadImagetoStorage(uri)
    }

//==================================================================================================
//Firebase Authentication User Funktionen aus dem Repository MVVM===================================
//==================================================================================================

    fun login(email: String, password: String, context: Context): Task<String> {
        return firebaseRepository.login(email, password, context)
    }

    fun register(
        email: String,
        password: String,
        passConfirmation: String,
        context: Context
    ): Task<String> {
        return firebaseRepository.register(email, password, passConfirmation, context)
    }

    //Funktion um den aktuellen User anhand seiner Id zu identifizieren aus der Authentication
    fun showCurrentUserId() {
        firebaseRepository.showCurrentUserId()
        // Log.d("Repo Objects", "User Id ${uId.value}")
    }

    //Ausloggen des aktuellen Users
    fun signOutUser() {
        //Ausloggen
        firebaseRepository.signOutUser()
    }

    //Prüfen ob der User schon alle Angaben ausgefüllt hat
    fun checkUserDataComplete(uId: String): Task<String> {
        return firebaseRepository.checkUserDataComplete(uId)
    }

//==================================================================================================
//Firebase Database Funktionen aus dem Repository MVVM==============================================
//==================================================================================================

    //Firebase Firestore zählen der Advertisements
    fun countAdvertises() {
        firebaseRepository.countAdvertises()
    }

    //Funktion des aktuellen Userstatus ermitteln, eingeloogt und setzten der LiveData
    fun currentAppUserLogged() {
        firebaseRepository.currentAppUserLogged()
    }

    //Funktion um den aktuellen User upzudaten und die Daten aus dem Firestore zu holen
    fun updateCurrentUserFromFirestore() {
        firebaseRepository.updateCurrentUserFromFirestore()
    }

    //Daten des neuen Users oder bei Änderung speichern
    fun saveUserData(personalData: PersonalData) {
        firebaseRepository.saveUserData(personalData)
    }

    //Anzeige des aktuellen Objektes (Room)
    fun setCurrentObject(objects: Objects) {
        _currentObject.value = objects
    }

    //Anzeigen des aktuellen Advertisments (Firebase)
    fun setCurrentAdvertisement(advertisement: Advertisement) {
        _currentAdvertisment.value = advertisement
    }

    //Für die Suche auf der Home wird hier der Text aus dem LiveData übergeben
    fun updateInputText(text: String) {
        _inputText.value = text
    }

    //Anzahl bisheriger Anzeigen erhöhen
    fun addCounterForAdvertises(personalData: PersonalData) {
        firebaseRepository.addCounterForAdvertises(personalData)
    }

    //Inserieren eines neuen Advertisments (Objekt)
    fun saveItemToDatabase(advertisement: Advertisement) {
        firebaseRepository.saveItemToDatabase(advertisement)
    }

    //Erstellen einer neuen Chat Nachricht
    fun saveMessageToDatabase(message: Message) {
        firebaseRepository.saveMessageToDatabase(message)
    }

    //Erkennen aller Advertisement Ids
    fun getAllAdId() {
        firebaseRepository.getAllAdId()
    }

    //Erkennen der aktuellen AdvertismentId mit LiveData
    fun getAdvertisementId(advertisement: Advertisement) {
        firebaseRepository.getAdvertisementId(advertisement)
    }

    //Auslesen der Datenbank von Firebase
    fun checkDatabaseForMyAds() {
        firebaseRepository.checkDatabaseForMyAds()
    }

    //Abfrage in der Firebase Database aller MEINER Nachrichten
    fun checkMessages() {
        firebaseRepository.checkMessages()
    }

//==================================================================================================
//Funktion für die Schnellsuche über Postleitzahl===================================================
//==================================================================================================

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

//==================================================================================================
//API Call Zielkoordinaten und 2. Call Entfernung zum Ziel =========================================
//==================================================================================================

//GEOdata===========================================================================================

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

    suspend fun getDistanceData(origins: String, destinations: String) {
        try {
            val data = apiDistance.retrofitService3.getDistance(origins, destinations)
            //Log.d("success Repository", "$destinations für Ziel, $origins für Start")
            _distanceData.value = data
        } catch (e: Exception) {
            // Log.e("Repository", "$e - getDistance API Call failed")
        }
    }

//==================================================================================================
//Room Datenbank====================================================================================
//==================================================================================================

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
                database.objectDao.insertObject(data.object12)
                database.objectDao.insertObject(data.object13)
                database.objectDao.insertObject(data.object14)
                database.objectDao.insertObject(data.object15)
                database.objectDao.insertObject(data.object16)
                database.objectDao.insertObject(data.object17)
                database.objectDao.insertObject(data.object18)
                database.objectDao.insertObject(data.object19)
                database.objectDao.insertObject(data.object20)
                database.objectDao.insertObject(data.object21)
                database.objectDao.insertObject(data.object22)
                database.objectDao.insertObject(data.object23)

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

    //Löschen eines einzigen Objektes mit Kennung der id
    suspend fun deleteById(id: Long) {
        try {
            database.objectDao.deleteById(id)
        } catch (e: Exception) {
            // Log.e("Repository", "deleteById failed")
        }
    }

//==================================================================================================
//Noch ungenutzte Funktionen========================================================================
//==================================================================================================

    //Einfügen eines Objektes
    suspend fun insertObject(objects: Objects) {
        try {
            database.objectDao.insertObject(objects)
        } catch (e: Exception) {
            //  Log.e("Repository", "insertObject failed")
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
//==================================================================================================
//Ende====================================Ende===================================Ende===============
//==================================================================================================
