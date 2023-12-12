package com.example.modulabschlussandroid.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.modulabschlussandroid.data.datamodels.Advertisement
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.data.datamodels.apicall.distance.DistanceMatrix
import com.example.modulabschlussandroid.data.datamodels.apicall.geo.Geo
import com.example.modulabschlussandroid.data.datamodels.chat.Message
import com.example.modulabschlussandroid.data.local.ObjectDatabase
import com.example.modulabschlussandroid.data.remote.DistanceApiObject
import com.example.modulabschlussandroid.data.remote.GeoCoderApiObject
import com.example.modulabschlussandroid.repositorys.RepositoryObjects
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch


//Hier im ViewModel wird die View Ebene gebaut aus der MVVM
//Hier muss das ViewModel AndroidViewModel sein, da nur hier die Mögllichkeit besteht Daten mit zugeben
class ViewModelObjects(application: Application) : AndroidViewModel(application) {

    private val database = ObjectDatabase.getDatabase(application)
    private val repository = RepositoryObjects(database, GeoCoderApiObject, DistanceApiObject)

//==================================================================================================
//Alle Daten aus dem Repository übergeben MVVM======================================================
//==================================================================================================

    //Für die Navigation auf die Detailseite, da sowohl aus Home und Profil unterschiedliche Datenbanken
    var homeFragment: Boolean = false

    //Hier wird die Objekt-Liste aus dem Repository eingeschleift
    var objectList = repository.objectList

    //Hier werden die gelikten Objekte aus dem Repository eingeschleift
    var likedObjectsLive = repository.likedObjects

    //Hier werden die Userdaten aber nur die Email aus dem Repository übergeben
    var uId = repository.uId

    //Hier werden die User aus dem Repository übergeben
    var currentUser = repository.currentUser

    //Hier werden die Postleitzahlen Objekte aus dem Repository übergeben
    var zipObjects = repository.zipObjects

    //Hier wird die LiveData der Geo API Abfrage aus dem Repository eingeschleift
    var geoResult: LiveData<Geo> = repository.geoResult

    //Hier wird die LiveData der Distance Api Abfrage aus dem Repository eingeschleift
    var distanceData: LiveData<DistanceMatrix> = repository.distanceData

    //Hier wird die LiveData der Postleitzahlensuche aus dem Repository übergeben
    var inputText = repository.inputText

    //Erstellen einer LivaData mit dem aktuellen Objekt (Room Datenbank)
    var currentObject = repository.currentObject

    //Erstellen einer LivaData mit dem aktuellen Advertisments (Firebase Datenbank)
    var currentAdvertisement = repository.currentAdvertisement

    //LiveData des aktuellen Users eingeloggt oder nicht
    var currentAppUser = repository.currentAppUser

    //Erkennen der aktuellen AdvertismentId mit LiveData
    val currentAdvertisementId = repository.currentAdvertisementId

    //Zählen meiner jetzigen Inserate, die online sind
    val countAdvertises = repository.countAdvertises

    //Zählen aller Inserate die von dem eingeloggten User gerade online sind
    val allMyAdvertises = repository.allMyAdvertises

    //LiveData für die Nachrichten einer bestimmten Advertisement Id
    val myMessage = repository.myMessage

//==================================================================================================
//Init Block========================================================================================
//==================================================================================================


    //Erste Befüllung der Datenbank
    init {
        //Prüfen ob die Datenbank leer ist, denn nur dann soll eingefügt werden
        viewModelScope.launch {
            repository.loadAllObjects()
        }
    }

//==================================================================================================
//Alle Funktionen aus dem Repository MVVM===========================================================
//==================================================================================================

    //GeoDaten der jeweiligen Objekte holen
    fun getGeoResult(city: String) {
        viewModelScope.launch {
            repository.getGeoResult(city)
        }
    }

    //DistanceDaten der übergebenen Koordinaten
    fun getDistanceData(origins: String, destinations: String) {
        viewModelScope.launch {
            repository.getDistanceData(origins, destinations)
        }
    }

    //Update eines bestimmten Objektes mit Daten die hinzugefügt werden sollen
    fun updateObjects(objects: Objects) {
        viewModelScope.launch {
            repository.updateObject(objects)
        }
    }

    //Anzeige des aktuellen Objektes (Room)
    fun setCurrentObject(objects: Objects) {
        repository.setCurrentObject(objects)
    }

    //Anzeigen des aktuellen Advertisments (Firebase)
    fun setCurrentAdvertisement(advertisement: Advertisement) {
        repository.setCurrentAdvertisement(advertisement)
    }

    //Für die Suche auf der Home wird hier der Text aus dem LiveData übergeben
    fun updateInputText(text: String) {
        repository.updateInputText(text)
    }

    fun getZipCodeObject(zip: String) {
        viewModelScope.launch {
            repository.getZipCodeObject(zip)
            //  Log.d("success ViewModel", "$zip input Text")
        }
    }

    //Funktion um Bilder in das Firebase Storage hochzuladen
    fun uploadImagetoStorage(uri: Uri) {
        viewModelScope.launch {
            repository.uploadImagetoStorage(uri)
        }
    }

    //NEU Update der aktuellen User Id  TODO!!!!!!!!!
    fun showCurrentUserId() {
        repository.showCurrentUserId()
        //     Log.d("viewModel", "User Id ${uId.value}")
    }

    //Neuer User muss erst seine Benutzerdaten eingeben
    fun saveUserData(personalData: PersonalData) {
        viewModelScope.launch {
            //Aufruf der Funktion einen neuen User anzulegen
            repository.saveUserData(personalData)
        }
    }

    //Update aller Userdaten mit einer bestimmten Id aus der Firestore Database
    fun updateCurrentUserFromFirestore() {
        viewModelScope.launch {
            repository.updateCurrentUserFromFirestore()
        }
    }

    //Login in Firebase Authentication
    fun login(email: String, password: String, context: Context): Task<String> {
        return repository.login(email, password, context)
    }

    //Registrierung in Firebase Authentication
    fun register(
        email: String,
        password: String,
        passConfirmation: String,
        context: Context
    ): Task<String> {
        return repository.register(email, password, passConfirmation, context)
    }

    //Ausloggen des aktuellen Users in Firebase Authentication
    fun signOutUser() {
        //Ausloggen
        repository.signOutUser()
    }

    //Inserieren eines neuen Advertisments (Objekt)
    fun saveItemToDatabase(advertisement: Advertisement) {
        viewModelScope.launch {
            repository.saveItemToDatabase(advertisement)
        }
    }

    //Erstellen einer neuen Chat Nachricht NOCH NICHT IN BENUTZUNG
    fun saveMessageToDatabase(message: Message) {
        viewModelScope.launch {
            repository.saveMessageToDatabase(message)
        }
    }

    //Erkennen der AdvertismentId
    fun getAllAdId() {
        repository.getAllAdId()
    }

    //Entsprechende Id des aktuellen Advertisements erhalten zur Weitergabe an Chat
    fun getAdvertisementId(advertisement: Advertisement) {
        repository.getAdvertisementId(advertisement)
    }

    //Bei Erstanmelung wird kontrolliet ob die Userdaten komplett sind
    fun checkUserDateComplete(uId: String): Task<String> {
        return repository.checkUserDataComplete(uId)
    }

    //Anzahl meiner bisherigen Anzeigen erhöhen
    fun addCounterForAdvertises(personalData: PersonalData) {
        repository.addCounterForAdvertises(personalData)
    }

    //Counter für alle Anzeigen online
    fun countAdvertises() {
        repository.countAdvertises()
    }

    //Zählen aller Inserate die von dem eingeloggten User gerade online sind
    fun checkDatabaseForMyAds() {
        repository.checkDatabaseForMyAds()
    }

    //Konvertieren der Uhrzeit von Milli und Nanosekunden, zu normaler Uhrzeit
    fun convertTimestampToDateTime(timestamp: com.google.firebase.Timestamp): String {
        return repository.convertTimestampToDateTime(timestamp)
    }

    //Abfrage in der Firebase Database aller MEINER Nachrichten
    fun checkMessages() {
        viewModelScope.launch {
        repository.checkMessages()
        }
    }

    //Funktion des aktuellen Userstatus ermitteln, eingeloogt und setzten der LiveData
    fun currentAppUserLogged() {
        repository.currentAppUserLogged()
    }

    //Ein einzelnes Objekt aus der Room Datenbank löschen
    fun deleteById() {
        viewModelScope.launch {
            val thisObject = currentObject.value
            if (thisObject != null) {
                val id = thisObject.id
                viewModelScope.launch {
                    repository.deleteById(id)
                }
            }
        }
    }
}

//==================================================================================================
//Ende====================================Ende===================================Ende===============
//==================================================================================================
