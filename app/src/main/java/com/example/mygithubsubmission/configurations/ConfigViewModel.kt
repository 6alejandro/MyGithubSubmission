package com.example.mygithubsubmission.configurations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mygithubsubmission.data.local.ConfigurationPreferences
import kotlinx.coroutines.launch

class ConfigViewModel(private val pref: ConfigurationPreferences): ViewModel() {

    fun getTheme() = pref.getThemeSetting().asLiveData()

    fun saveTheme(isDark: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDark)
        }
    }
    class Factory(private val pref: ConfigurationPreferences): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = ConfigViewModel(pref) as T
    }
}