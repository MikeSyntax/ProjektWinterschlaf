package com.example.modulabschlussandroid.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.datamodels.apicall.Geo
import com.example.modulabschlussandroid.data.datamodels.apicall.Location
import com.example.modulabschlussandroid.data.local.ObjectDatabase
import com.example.modulabschlussandroid.data.remote.GeoCoderApiObject
import com.example.modulabschlussandroid.repositorys.RepositoryObjects
import kotlinx.coroutines.launch
import com.example.modulabschlussandroid.data.datamodels.apicall.Result
import com.example.modulabschlussandroid.data.remote.LocationApiObject

//Hier muss das ViewModel AndroidViewModel sein, da nur hier die Mögllichkeit besteht Daten mit zugeben
class ViewModelObjects(application: Application) : AndroidViewModel(application) {

    private val database = ObjectDatabase.getDatabase(application)
    private val repository = RepositoryObjects(database, GeoCoderApiObject)
    //private val repository = RepositoryObjects(database, LocationApiObject)

    //Hier wird die Objekt-Liste aus dem Repository eingeschleift
    var objectList = repository.objectList

    //Hier werden die gelikten Objekte aus dem Repository eingeschleift
    var likedObjectsLive = repository.likedObjects

    //Hier wird die LiveData der Geo API Abfrage aus dem Repository eingeschleift
    var geoResult: LiveData<Geo> = repository.geoResult
    //var geoResult: LiveData<Location> = repository.geoResult

/*
    //Erstellen einer LiveData mit dem aktuellen GeoCode
    private val _currentGeoData: MutableLiveData<Result> = MutableLiveData()
    val currentGeoData: LiveData<Result>
        get()=_currentGeoData
*/

    //Erstellen einer LivaData mit dem aktuellen Objekt
    private val _currentObject: MutableLiveData<Objects> = MutableLiveData()
    val currentObject: LiveData<Objects>
        get() = _currentObject

    //Erste Befüllung der Datenbank
    init {
        //Erst alle in der Datenbank löschen
       /* viewModelScope.launch {
            repository.deleteAll()
        }*/
        //Dann neu einfügen

        //TODO hier muss unbedingt die Kontrolle stattfinden, ob die Datenbank leer ist, denn nur dann soll eingefügt werden
        viewModelScope.launch {
                repository.loadAllObjects()
        }
    }

    //GeoDaten der jeweiligen Objekte holen
    fun getGeoResult(city : String) {
        viewModelScope.launch {
            repository.getGeoResult(city)
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

/*
    //Anzeige des aktuellen GeoCodes aus der Result Klasse
    fun setCurrentGeoData(geoCode: Result) {
        viewModelScope.launch {
            _currentGeoData.postValue(geoCode)
        }
    }
 */

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