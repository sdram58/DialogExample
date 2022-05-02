package com.example.dialogexample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        private var instance:UserDao?=null
        fun getInstance(context: Context):UserDao{
            return instance?: Room.databaseBuilder(context,AppDatabase::class.java,"Users-db").build().userDao().also { instance = it }
        }
    }
}