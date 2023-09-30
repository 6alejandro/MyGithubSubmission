package com.example.mygithubsubmission.saved

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubsubmission.R
import com.example.mygithubsubmission.UserAdapter
import com.example.mygithubsubmission.data.local.DatabaseModule
import com.example.mygithubsubmission.databinding.ActivitySavedBinding
import com.example.mygithubsubmission.detail.DetailActivity

class SavedActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedBinding
    private val adapter by lazy {
        UserAdapter {
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", it)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<SavedViewModel>() {
        SavedViewModel.Factory(DatabaseModule(this))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFav.layoutManager = LinearLayoutManager(this)
        binding.rvFav.adapter = adapter

        viewModel.getUserFav().observe(this)  {
            adapter.setData(it)
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