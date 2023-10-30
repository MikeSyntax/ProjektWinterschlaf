package com.example.modulabschlussandroid.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.modulabschlussandroid.data.datamodels.Objects



//@Database(entities = [Objects::class, PersonalData::class], version = 4,
@Database(entities = [Objects::class], version = 3,
    //wechselt hiermit auf die neue Version, da noch zipCode zur Datenbank hinzugefügt wurde
     autoMigrations = [AutoMigration(from = 2, to = 3)]
)

abstract class ObjectDatabase: RoomDatabase(){

    //Abstrakte Variable Object
    abstract val objectDao: ObjectDao

    //Abstrakte Variable UserData
    //abstract val userDataDao: UserDataDao

    companion object {

        private lateinit var INSTANCE: ObjectDatabase

        fun getDatabase(context: Context) :ObjectDatabase {

            synchronized(ObjectDatabase::class.java){

                if (!::INSTANCE.isInitialized){

                    INSTANCE = Room.databaseBuilder(

                        context.applicationContext,

                        ObjectDatabase::class.java,

                        "Objects"
                    )
                        .build()
                }
                return INSTANCE
            }
        }
    }
}
