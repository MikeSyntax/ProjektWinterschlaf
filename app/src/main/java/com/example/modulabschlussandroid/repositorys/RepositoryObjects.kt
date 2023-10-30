package com.example.modulabschlussandroid.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.modulabschlussandroid.data.datamodels.apicall.Geo
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.datamodels.apicall.Location
import com.example.modulabschlussandroid.data.exampledata.ObjectsExampleData
import com.example.modulabschlussandroid.data.local.ObjectDatabase
import com.example.modulabschlussandroid.data.remote.GeoCoderApiObject
import com.example.modulabschlussandroid.data.remote.LocationApiObject


class RepositoryObjects(

    //Verbindung zur Datenbank
    private val database: ObjectDatabase,

    //Verbindung zum Objekt in der PositionApiService
    private val api: GeoCoderApiObject,
    //private val api2: LocationApiObject
) {
//LiveData ObjectList==========================================================================================

    val objectList: LiveData<List<Objects>> = database.objectDao.showALL()

    /*
    //Anlegen einer LiveData Variablen mit allen Items der Database
    private val _objectList: MutableLiveData<List<Objects>> = MutableLiveData()
    val objectList: LiveData<List<Objects>>
        get() = _objectList
    */
//Geliked Objects==========================================================================================

    val likedObjects: LiveData<List<Objects>> = database.objectDao.showALLLikedObjects()

//GEOdata==========================================================================================
    //LiveData der GeoDaten Abfrage über einen API Call
    private val _geoResult: MutableLiveData<Geo> = MutableLiveData()
    val geoResult: LiveData<Geo>
        get() = _geoResult

    //LiveData der GeoDaten Abfrage über einen API Call
    /* private val _geoResult: MutableLiveData<Location> = MutableLiveData()
     val geoResult: LiveData<Location>
         get () = _geoResult*/

    //API Call starten
    suspend fun getGeoResult(city : String) {
        try {
            val geo = api.retrofitService.getGeoCode(city,1,"de","json")
            _geoResult.value = geo
            //   _geoResult.value = api2.retrofitService2.getLocationCode()
        } catch (e: Exception) {
            Log.e("Repository", "$e - getGeoResult API Call failed")
        }
    }
//Datenbank anlegen==========================================================================================

    //Falls die Datenbank noch leer ist, einmal bitte alle Objekte hineinladen
    suspend fun loadAllObjects() {
        val data = ObjectsExampleData
        try {
       /*     //objectList muss zuerst angeschaut werden bzw. geprüft ob leer
            _objectList.value = database.objectDao.showALL().value
            Log.e(
                "Repository",
                "Database Anfang vor if Bedingung - ${database.objectDao.showALL().value}"
            )
            if (objectList.value.isNullOrEmpty()) {
                Log.e("Repository", "if Bedingung erfüllt")*/

                //Diese If Abfrage funktioniert auch nicht, es werden überhaupt keine Daten in die Datenbank übergeben
               // if (countObjects() == 0){
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

         /*       Log.e(
                    "Repository",
                    "Datenbank nach dem inserten - ${database.objectDao.showALL().value}"
                )
                //Nach dem inserten wird die Datenbank nochmal aktualisiert auf den Wert
                _objectList.value = database.objectDao.showALL().value
                Log.e(
                    "Repository",
                    "Database Ende if Bedingung - ${database.objectDao.showALL().value}"
                )*/

            //}
        } catch (e: Exception) {
            Log.e("Repository", "$e loadAllObjects failed")
        }
    }

    //Count Funktion aus der Dao
    fun countObjects(): Int {
        return try {
            var count = 0
            count = database.objectDao.countObjects()
            count
        } catch (e: Exception) {
            Log.e("Repository", " $e - count Objects failed")
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

    //Löschen aller Objekte
    suspend fun deleteAll() {
        try {
            database.objectDao.deleteALL()
        } catch (e: Exception) {
            Log.e("Repository", "deleteAll failed")
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
}
