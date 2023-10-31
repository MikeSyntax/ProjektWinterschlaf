package com.example.modulabschlussandroid.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.modulabschlussandroid.data.datamodels.apicall.Geo
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.datamodels.PersonalData
import com.example.modulabschlussandroid.data.datamodels.apicall.Location
import com.example.modulabschlussandroid.data.exampledata.ObjectsExampleData
import com.example.modulabschlussandroid.data.local.ObjectDatabase
import com.example.modulabschlussandroid.data.remote.GeoCoderApiObject
import com.example.modulabschlussandroid.data.remote.LocationApiObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RepositoryObjects(

    //Verbindung zur Datenbank
    private val database: ObjectDatabase,

    //Verbindung zum Objekt in der PositionApiService
    private val api: GeoCoderApiObject,
    //private val api2: LocationApiObject
) {
//LiveData ObjectList===============================================================================

    //Alle Objekte in der Datenbank als LiveData anzeigen
    val objectList: LiveData<List<Objects>> = database.objectDao.showALL()

//Geliked Objects===================================================================================

    //Alle favorisierten Objekte in der Datenbank als LiveData anzeigen
    val likedObjects: LiveData<List<Objects>> = database.objectDao.showALLLikedObjects()

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
            Log.e("Repository", "$e - getGeoResult API Call failed")
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
            Log.e("Repository", "$e loadAllObjects failed")
        }
    }

    //Update eines Objektes mit Änderungen
    suspend fun updateObject(objects: Objects) {
        try {
            database.objectDao.updateObject(objects)
        } catch (e: Exception) {
            Log.e("Repository", "updateObject failed")
        }
    }

    //Einfügen eines Objektes
    suspend fun insertObject(objects: Objects) {
        try {
            database.objectDao.insertObject(objects)
        } catch (e: Exception) {
            Log.e("Repository", "insertObject failed")
        }
    }


    //Löschen eines einzigen Objektes mit Kennung der id
    suspend fun deleteById(id: Long) {
        try {
            database.objectDao.deleteById(id)
        } catch (e: Exception) {
            Log.e("Repository", "deleteById failed")
        }
    }

    //Löschen aller Objekte
    suspend fun deleteAll() {
        try {
            database.objectDao.deleteALL()
        } catch (e: Exception) {
            Log.e("Repository", "deleteAll failed")
        }
    }
//======================================================================================================
//Update eines Objektes mit Änderungen
suspend fun updatePersonalData(personalData: PersonalData) {
    try {
        database.userDataDao.updateUser(personalData)
    } catch (e: Exception) {
        Log.e("Repository", "updateObject failed")
    }
}

    suspend fun insertPersonalData(personalData: PersonalData) {
        try {
            database.userDataDao.insertUserData(personalData)
        } catch (e: Exception) {
            Log.e("Repository", "updateObject failed")
        }
    }

}
