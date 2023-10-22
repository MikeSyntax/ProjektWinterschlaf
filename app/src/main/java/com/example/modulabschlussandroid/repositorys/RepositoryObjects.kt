package com.example.modulabschlussandroid.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.modulabschlussandroid.data.datamodels.Objects
import com.example.modulabschlussandroid.data.exampledata.ObjectsExampleData
import com.example.modulabschlussandroid.data.local.ObjectDatabase

class RepositoryObjects(
    private val database: ObjectDatabase
) {
    //Anlegen einer LiveData Variablen mit allen Items der Database
    val objectList: LiveData<List<Objects>> = database.objectDao.showALL()

    //Falls die Datenbank noch leer ist, einmal bitte alle Objekte hineinladen
    suspend fun loadAllObjects() {
        val data = ObjectsExampleData
        try {
            if (objectList == null) {
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
            Log.e("Repository", "loadAllObjects failed")
        }
    }
}