package com.example.mygithubsubmission.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mygithubsubmission.data.local.ConfigurationPreferences
import com.example.mygithubsubmission.data.remote.ApiConfig
import com.example.mygithubsubmission.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: ConfigurationPreferences): ViewModel() {

    val resultUser = MutableLiveData<Result>()

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    fun getUser() {
        viewModelScope.launch {
                flow {
                    val response = ApiConfig
                        .githubService
                        .getUser()

                    emit(response)
                }.onStart {
                    resultUser.value = Result.Loading(true)
                }.onCompletion {
                    resultUser.value = Result.Loading(false)
                }.catch {
                    Log.e("Error", it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Result.Error(it)
                }.collect {
                    resultUser.value = Result.Success(it)
            }
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
                flow {
                    val response = ApiConfig
                        .githubService
                        .getSearchUser(mapOf(
                            "q" to username,
                            "per_page" to 10
                        ))

                    emit(response)
                }.onStart {
                    resultUser.value = Result.Loading(true)
                }.onCompletion {
                    resultUser.value = Result.Loading(false)
                }.catch {
                    Log.e("Error", it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Result.Error(it)
                }.collect {
                    resultUser.value = Result.Success(it.items)
            }
        }
    }

    class Factory(private val preferences: ConfigurationPreferences) :
            ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(preferences) as T
        }
}