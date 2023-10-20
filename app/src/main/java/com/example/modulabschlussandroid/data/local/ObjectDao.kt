package com.example.modulabschlussandroid.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.modulabschlussandroid.data.datamodels.Objects

@Dao
interface ObjectDao {

    //Alle Objekt in die Datenbank einpflegen mit onConflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllObjects(itemObject: List<Objects>)

    //Nur ein Objekt in die Datenbank einpflegen mit onConflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObject(objects: Objects)

    //Alle Objekte aus der Datenbank lesen mit LiveData
    @Query("SELECT * FROM your_rental_place")
    fun getALL(): LiveData<List<Objects>>

    //Alle Objekte in der Datenbank löschen
    @Query("DELETE FROM your_rental_place")
    suspend fun deleteALL(): List<Objects>

    //Ein über die Id ausgewähltes Löschen, die Id wird übergeben
    @Query("DELETE FROM your_rental_place WHERE id = :id")
    suspend fun deleteById(id:Long)

    //Update der Datenbank
    @Update
    fun updateObjects(objects: Objects)

}