package com.example.mygithubsubmission.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mygithubsubmission.data.model.Item

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: Item)

    @Query("SELECT * FROM users")
    fun loadAll(): LiveData<MutableList<Item>>

    @Query("SELECT * FROM users WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): Item

    @Delete
    fun delete(user: Item)
}