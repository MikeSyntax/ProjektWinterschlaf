package com.example.modulabschlussandroid.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.datamodels.apicall.distance.DistanceMatrix
import com.example.modulabschlussandroid.data.datamodels.apicall.geo.Geo
import com.example.modulabschlussandroid.data.local.ObjectDatabase
import com.example.modulabschlussandroid.data.remote.GeoCoderApiObject
import com.example.modulabschlussandroid.repositorys.RepositoryObjects
import kotlinx.coroutines.launch
import com.example.modulabschlussandroid.data.remote.DistanceApiObject

//Hier muss das ViewModel AndroidViewModel sein, da nur hier die Mögllichkeit besteht Daten mit zugeben
class ViewModelObjects(application: Application) : AndroidViewModel(application) {

    private val database = ObjectDatabase.getDatabase(application)
    private val repository = RepositoryObjects(database, GeoCoderApiObject, DistanceApiObject)
    //private val repository = RepositoryObjects(database, LocationApiObject)

    //Hier wird die Objekt-Liste aus dem Repository eingeschleift
    var objectList = repository.objectList

    //Hier werden die gelikten Objekte aus dem Repository eingeschleift
    var likedObjectsLive = repository.likedObjects


    var zipObjects = repository.zipObjects

    //Hier wird die LiveData der Geo API Abfrage aus dem Repository eingeschleift
    var geoResult: LiveData<Geo> = repository.geoResult
    //var geoResult: LiveData<Location> = repository.geoResult

    //Hier wird die LiveData der Distance Api Abfrage aus dem Repository eingeschleift
    var distanceData: LiveData<DistanceMatrix> = repository.distanceData

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
        _inputText.value = text
    }

    fun getZipCodeObject(zip: String) {
        viewModelScope.launch {
            repository.getZipCodeObject(zip)
            Log.d("success ViewModel", "$zip input Text")
        }
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