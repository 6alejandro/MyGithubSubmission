package com.example.mygithubsubmission.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygithubsubmission.data.local.DatabaseModule
import com.example.mygithubsubmission.detail.DetailViewModel

class SavedViewModel(private val dbModule: DatabaseModule): ViewModel() {

    fun getUserFav() = dbModule.userDao.loadAll()
    class Factory(private val db: DatabaseModule): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = SavedViewModel(db) as T
    }
}