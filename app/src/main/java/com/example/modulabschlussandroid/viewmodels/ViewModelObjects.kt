package com.example.modulabschlussandroid.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.local.ObjectDatabase
import com.example.modulabschlussandroid.repositorys.RepositoryObjects
import kotlinx.coroutines.launch

//Hier muss das ViewModel AndroidViewModel sein, da nur hier die Mögllichkeit besteht Daten mit zugeben
class ViewModelObjects(application: Application) : AndroidViewModel(application) {

    private val database = ObjectDatabase.getDatabase(application)
    private val repository = RepositoryObjects(database)

    //Hier wird die Objekt-Liste aus dem Repository reingeholt
    var objectListLive = repository.objectList
    var likedObjectsLive = repository.likedObjects

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


    fun updateObjects(objects: Objects) {
        viewModelScope.launch {
            repository.updateObject(objects)
        }
    }


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
