package com.example.mygithubsubmission.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mygithubsubmission.data.local.DatabaseModule
import com.example.mygithubsubmission.data.model.Item
import com.example.mygithubsubmission.data.remote.ApiConfig
import com.example.mygithubsubmission.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val db: DatabaseModule): ViewModel() {
    val resultDetailUser = MutableLiveData<Result>()
    val resultFollowers = MutableLiveData<Result>()
    val resultFollowing = MutableLiveData<Result>()
    val resultFav = MutableLiveData<Boolean>()
    val resultDelFav = MutableLiveData<Boolean>()

    private var isFavorite = false
    fun saveUser(item: Item?) {
        viewModelScope.launch {
            item?.let {
                if(isFavorite) {
                    db.userDao.delete(item)
                    resultDelFav.value = true
                }else {
                    db.userDao.insert(item)
                    resultFav.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findFav(id: Int, listenFav: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if(user!= null) {
                listenFav()
                isFavorite = true
            }
        }
    }

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .getDetailUser(username)

                emit(response)
            }.onStart {
                resultDetailUser.value = Result.Loading(true)
            }.onCompletion {
                resultDetailUser.value = Result.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultDetailUser.value = Result.Error(it)
            }.collect {
                resultDetailUser.value = Result.Success(it)
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .getFollowers(username)

                emit(response)
            }.onStart {
                resultFollowers.value = Result.Loading(true)
            }.onCompletion {
                resultFollowers.value = Result.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultFollowers.value = Result.Error(it)
            }.collect {
                resultFollowers.value = Result.Success(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .getFollowing(username)

                emit(response)
            }.onStart {
                resultFollowing.value = Result.Loading(true)
            }.onCompletion {
                resultFollowing.value = Result.Loading(false)
            }.catch {
                Log.e("Error", it.message.toString())
                it.printStackTrace()
                resultFollowing.value = Result.Error(it)
            }.collect {
                resultFollowing.value = Result.Success(it)
            }
        }
    }

    class Factory(private val db: DatabaseModule): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}