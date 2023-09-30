package com.example.mygithubsubmission.data.local

import android.content.Context
import androidx.room.Room

class DatabaseModule(private val ctx: Context) {
    private val db = Room.databaseBuilder(ctx, Database::class.java, "usergithub.db")
        .allowMainThreadQueries()
        .build()

    val userDao = db.userDao()
}