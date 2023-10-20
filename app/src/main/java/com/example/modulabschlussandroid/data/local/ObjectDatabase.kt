package com.example.modulabschlussandroid.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.modulabschlussandroid.data.datamodels.Objects


@Database(entities = [Objects::class], version = 1)
abstract class ObjectDatabase: RoomDatabase(){

    abstract val objectDao: ObjectDao

    companion object {

        private lateinit var INSTANCE: ObjectDatabase

        fun getDatabase(context: Context) :ObjectDatabase {

            synchronized(this){

                if (this::INSTANCE.isInitialized){

                    INSTANCE = Room.databaseBuilder(

                        context.applicationContext,

                        ObjectDatabase::class.java,

                        "object_database"
                    )
                        .build()
                }
                return INSTANCE
            }
        }
    }
}
