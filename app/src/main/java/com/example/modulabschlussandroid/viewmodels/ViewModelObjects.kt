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

    //Hier wird die Objekt Liste aus dem Repository reingeholt
    var objectListLive = repository.objectList

    //Erstellen einer LivaData mit dem aktuellen Objekt
    private val _currentObject: MutableLiveData<List<Objects>> = MutableLiveData()
    private val currentObject: LiveData<List<Objects>>
        get() = _currentObject


    //Erste Befüllung der Datenbank
    init {
        viewModelScope.launch {
            repository.loadAllObjects()
        }
    }

    fun loadFromDatabase(){
        viewModelScope.launch {
            objectListLive = repository.getAllObjects()
        }
    }

}