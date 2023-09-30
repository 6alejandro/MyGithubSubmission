package com.example.mygithubsubmission.detail

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mygithubsubmission.R
import com.example.mygithubsubmission.data.local.DatabaseModule
import com.example.mygithubsubmission.data.model.Item
import com.example.mygithubsubmission.data.model.ResponseDetailUser
import com.example.mygithubsubmission.databinding.ActivityDetailBinding
import com.example.mygithubsubmission.detail.follow.FollowsFragment
import com.example.mygithubsubmission.utils.Result
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>() {
        DetailViewModel.Factory(DatabaseModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<Item>("item")
        val username = item?.login ?: ""

        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as ResponseDetailUser
                    binding.ivProfile.load(user.avatarUrl) {
                        transformations(CircleCropTransformation())
                    }

                    binding.tvName.text = user.name
                    binding.tvUsername.text = user.login

                    val followersText = getString(R.string.followers_template, user.followers.toString())
                    val followingText = getString(R.string.following_template, user.following.toString())

                    binding.tvTotalFollowers.text = followersText
                    binding.tvTotalFollowing.text = followingText
                }
                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getDetailUser(username)

        val fragments = mutableListOf<Fragment>(
            FollowsFragment.newInstance(FollowsFragment.FOLLOWERS),
            FollowsFragment.newInstance(FollowsFragment.FOLLOWING)
        )
        val titleFragment = mutableListOf(
            getString(R.string.tab_text_1),
            getString(R.string.tab_text_2)
        )
        val adapter = SectionsPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titleFragment[position]
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewModel.getFollowers(username)

        viewModel.resultFav.observe(this) {
            binding.btnFav.changeIconColor(R.color.red)
        }
        viewModel.resultDelFav.observe(this) {
            binding.btnFav.changeIconColor(R.color.white)
        }

        binding.btnFav.setOnClickListener{
            viewModel.saveUser(item)
        }

        viewModel.findFav(item?.id ?: 0) {
            binding.btnFav.changeIconColor(R.color.red)
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

fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}