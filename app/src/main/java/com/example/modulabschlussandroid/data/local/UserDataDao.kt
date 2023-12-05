package com.example.modulabschlussandroid.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.modulabschlussandroid.data.datamodels.PersonalData

@Dao
interface UserDataDao {


        //Nur ein Objekt in die Datenbank einpflegen mit onConflict
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertUserData(personalData: PersonalData)

        //Updaten eines Users
        @Update
        suspend fun updateUser(personalData: PersonalData)

        //Ein über die Id ausgewähltes Löschen, der UserName wird als ID übergeben
        @Query("DELETE FROM PersonalData WHERE userName = :userName")
        suspend fun deleteById(userName: String)

    }