package com.example.modulabschlussandroid.viewmodels

import android.app.Application
import android.content.Context
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
    //private val repository = RepositoryObjects(database, LocationApiObject)

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
    //var geoResult: LiveData<Location> = repository.geoResult

    //Hier wird die LiveData der Distance Api Abfrage aus dem Repository eingeschleift
    var distanceData: LiveData<DistanceMatrix> = repository.distanceData

    //Eingegebener Text für die Suche als LiveData
    private val _inputText = MutableLiveData<String>()
    val inputText: LiveData<String>
        get() = _inputText

    //Erstellen einer LivaData mit dem aktuellen Objekt
    private val _currentObject: MutableLiveData<Objects> = MutableLiveData()
    val currentObject: LiveData<Objects>
        get() = _currentObject

    //Erste Befüllung der Datenbank
    init {
        //Prüfen ob die Datenbank leer ist, denn nur dann soll eingefügt werden
        viewModelScope.launch {
            repository.loadAllObjects()
        }
    }

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

    //Insert eines bestimmten Objektes mit allen Daten
    fun insertObject(objects: Objects) {
        viewModelScope.launch {
            repository.insertObject(objects)
        }
    }

    //Anzeige des aktuellen Objektes
    fun setCurrentObject(objects: Objects) {
        viewModelScope.launch {
            _currentObject.postValue(objects)
        }
    }

    //Für die Suche auf der Home wird hier der Text aus dem LiveData übergeben
    fun updateInputText(text: String) {
        viewModelScope.launch {
        _inputText.value = text
        }
    }

    fun getZipCodeObject(zip: String) {
        viewModelScope.launch {
            repository.getZipCodeObject(zip)
            Log.d("success ViewModel", "$zip input Text")
        }
    }

//NEU Update der aktuellen User Id  TODO!!!!!!!!!
    fun showCurrentUserId(){
    viewModelScope.launch {
        repository.showCurrentUserId()
    Log.d("viewModel", "User Id ${uId.value}")
    }
    }

    //Neuer User muss erst seine Benutzerdaten eingeben
    fun newUserDataFirstSignIn(personalData: PersonalData){
        viewModelScope.launch {
        //Aufruf der Funktion einen neuen User anzulegen
        repository.newUserDataFirstSignIn(personalData)
        }
    }

//NEU Update aller Userdaten mit einer bestimmten Id aus der Firestore Database=====================
    fun updateCurrentUserFromFirestore(){
    viewModelScope.launch {
        repository.updateCurrentUserFromFirestore()
    }
    }

    fun login(email: String, password: String, context: Context): Task<String> {
       return repository.login(email, password, context)
    }

    fun register(email: String, password: String, passConfirmation: String, context: Context): Task<String> {
        return repository.register(email,password,passConfirmation,context)
    }

    //Ausloggen des aktuellen Users
    fun signOutUser(){
        viewModelScope.launch {
        //Ausloggen
        repository.signOutUser()
        }
    }

    //Inserieren eines neuen Advertisments (Objekt)
    fun saveItemToDatabase(advertisement: Advertisement) {
        viewModelScope.launch {
        repository.saveItemToDatabase(advertisement)
        }
    }


    fun checkUserDateComplete(uId: String) :Task<String>{
        return repository.checkUserDataComplete(uId)
    }

    //Counter für alle Anzeigen online==================================================================

    //Anzahl meiner bisheriger Anzeigen erhöhen
    fun addCounterForAdvertises(personalData: PersonalData){
        viewModelScope.launch {
        repository.addCounterForAdvertises(personalData)
        }
    }

    //Zählen meiner jetzigen Inserate, die online sind
    val countAdvertises = repository.countAdvertises
    fun countAdvertises(){
        viewModelScope.launch {
        repository.countAdvertises()
        }
    }

    //Zählen aller Inserate die von dem eingeloggten User gerade online sind
    //Auslesen der Datenbank von Firebase
    val allMyAdvertises = repository.allMyAdvertises
    fun checkDatabaseForMyAds(){
        viewModelScope.launch {
        repository.checkDatabaseForMyAds()
        }
    }

    //LiveData des aktuellen Users eingeloggt oder nicht
    var currentAppUser = repository.currentAppUser

    //Funktion des aktuellen Userstatus ermitteln, eingeloogt und setzten der LiveData
    fun currentAppUserLogged(){
        repository.currentAppUserLogged()
    }

    //Ein einzelnes Objekt löschen
    fun deleteById() {
        viewModelScope.launch {
            val thisObject = _currentObject.value
            if (thisObject != null) {
                val id = thisObject.id
                viewModelScope.launch {
                    repository.deleteById(id)
                }
            }
        }
    }
}