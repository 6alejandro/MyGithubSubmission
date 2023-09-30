package com.example.mygithubsubmission.configurations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.mygithubsubmission.R
import com.example.mygithubsubmission.data.local.ConfigurationPreferences
import com.example.mygithubsubmission.data.local.DatabaseModule
import com.example.mygithubsubmission.databinding.ActivityConfigBinding
import com.example.mygithubsubmission.detail.DetailViewModel

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfigBinding
    private val viewModel by viewModels<ConfigViewModel>() {
        ConfigViewModel.Factory(ConfigurationPreferences(this))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getTheme().observe(this) {
            if (it) {
                binding.switchTheme.text = "Dark Theme"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                binding.switchTheme.text = "Light Theme"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            binding.switchTheme.isChecked = it
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveTheme(isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}