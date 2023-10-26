package com.example.modulabschlussandroid.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.modulabschlussandroid.data.datamodels.apicall.Geo
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.exampledata.ObjectsExampleData
import com.example.modulabschlussandroid.data.local.ObjectDatabase
import com.example.modulabschlussandroid.data.remote.GeoCoderApiObject


class RepositoryObjects(

    //Verbindung zur Datenbank
    private val database: ObjectDatabase,

    //Verbindung zum Objekt in der PositionApiService
    private val api: GeoCoderApiObject
) {
    //Anlegen einer LiveData Variablen mit allen Items der Database
    val objectList: LiveData<List<Objects>> = database.objectDao.showALL()
    val likedObjects: LiveData<List<Objects>> = database.objectDao.showALLLikedObjects()


    private val _geoResult: MutableLiveData<Geo> = MutableLiveData()
    val geoResult: LiveData<Geo>
        get () = _geoResult

    //API Call starten
    suspend fun getGeoResult(){
        try {
           _geoResult.value =  api.retrofitService.getGeoCode()
        } catch (e: Exception) {
            Log.e("Repository", "getGeoResult API Call failed")
        }
    }


    //Falls die Datenbank noch leer ist, einmal bitte alle Objekte hineinladen
    suspend fun loadAllObjects() {
        val data = ObjectsExampleData
        try {
            //TODO
            //Diese If Abfrage funktioniert nicht, er setzt fleißig weitere die daten in die Datenbank
            //if(objectList.value.isNullOrEmpty()){
            //Diese If Abfrage funktioniert auch nicht, es werden überhaupt keine Daten in die Datenbank übergeben
             //if(countObjects() == 0){
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
            //}
        } catch (e: Exception) {
            Log.e("Repository", "loadAllObjects failed")
        }
    }

    //Count Funktion aus der Dao
    private fun countObjects():Int {
        var count = 0
        count = database.objectDao.countObjects()
        return count
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
