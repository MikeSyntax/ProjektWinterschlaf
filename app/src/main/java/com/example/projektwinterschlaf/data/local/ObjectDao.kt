package com.example.projektwinterschlaf.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projektwinterschlaf.data.datamodels.Objects

@Dao
interface ObjectDao {

    //Alle Objekt in die Datenbank einpflegen mit onConflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllObjects(itemObject: List<Objects>)

    //Nur ein Objekt in die Datenbank einpflegen mit onConflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObject(objects: Objects)

    //Alle Objekte aus der Datenbank lesen mit LiveData
    @Query("SELECT * FROM Objects")
    fun showALL(): LiveData<List<Objects>>

    //Alle Objekte aus der Datenbank nach Postleitzahl lesen
    @Query("SELECT * FROM Objects WHERE zipCode = :zip")
    fun getZipCodeObject(zip: String): LiveData<List<Objects>>

    //Alle Objekte mit liked=true aus der Datenbank lesen mit LiveDate
    @Query("SELECT * FROM Objects WHERE liked = 1")
    fun showALLLikedObjects(): LiveData<List<Objects>>

    //Alle Objekte in der Datenbank löschen
    @Query("DELETE FROM Objects")
    suspend fun deleteALL()

    //Ein über die Id ausgewähltes Löschen, die Id wird übergeben
    @Query("DELETE FROM Objects WHERE id = :id")
    suspend fun deleteById(id:Long)

    //Update der Datenbank
    @Update
    suspend fun updateObject(objects: Objects)

    //Zählen der Einträge in der Datenbank
    @Query("SELECT COUNT (*) FROM Objects")
    suspend fun countObjects(): Int
}