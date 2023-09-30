package com.example.mygithubsubmission.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mygithubsubmission.data.model.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun userDao(): UserDao
}