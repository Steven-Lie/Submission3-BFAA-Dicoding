package com.dicoding.githubusers.ui.detailUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubusers.R
import com.dicoding.githubusers.api.UserDetailResponse
import com.dicoding.githubusers.adapter.SectionsPagerAdapter
import com.dicoding.githubusers.database.Favorite
import com.dicoding.githubusers.databinding.ActivityDetailUserBinding
import com.dicoding.githubusers.helper.FavoriteViewModelFactory
import com.dicoding.githubusers.ui.favorite.FavoriteViewModel
import com.dicoding.githubusers.ui.setting.SettingActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var detailUserBinding: ActivityDetailUserBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>()
    private var avatarUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailUserBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailUserBinding.root)
        val username = intent.getStringExtra(EXTRA_USERNAME).toString()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = detailUserBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = detailUserBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.user_detail)

        detailUserViewModel.getUserDetail(username, this)

        detailUserViewModel.userDetail.observe(this, { userDetail ->
            showDetailUser(userDetail)
        })

        detailUserViewModel.isLoading.observe(this, { isLoading ->
            detailUserBinding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        val favoriteViewModel = obtainViewModel(this@DetailUserActivity)

        favoriteViewModel.getUser(username).observe(this, { favorite ->
            detailUserBinding.fabAddToFavorite.setOnClickListener {
                val fav = Favorite()
                fav.let {
                    it.avatar = avatarUrl
                    it.username = username
                }
                if (favorite != null){
                    favoriteViewModel.delete(favorite)
                    Toast.makeText(this, "Deleted from favorite", Toast.LENGTH_SHORT).show()
                } else {
                    favoriteViewModel.insert(fav)
                    Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show()
                }
            }
            if (favorite != null){
                detailUserBinding.fabAddToFavorite.setImageResource(R.drawable.ic_favorite_red)
            } else {
                detailUserBinding.fabAddToFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.setting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

    private fun showDetailUser(detailUser: UserDetailResponse){
        avatarUrl = detailUser.avatarUrl
        Glide.with(this.applicationContext)
            .load(detailUser.avatarUrl)
            .circleCrop()
            .into(detailUserBinding.imgProfileDetail)
        detailUserBinding.tvNameDetail.text = detailUser.name
        detailUserBinding.tvUsernameDetail.text = detailUser.login
        detailUserBinding.includeFollowersFollowing.imgFollowersFollowingDetail.setImageResource(R.drawable.ic_people)
        detailUserBinding.includeFollowersFollowing.tvFollowersDetail.text = getString(R.string.amount_followers, detailUser.followers.toString())
        detailUserBinding.includeFollowersFollowing.tvFollowingDetail.text = getString(R.string.amount_following, detailUser.following.toString())
        detailUserBinding.includeRepository.imgRepositoryDetail.setImageResource(R.drawable.ic_book)
        detailUserBinding.includeRepository.tvRepositoryDetail.text = getString(R.string.amount_repository, detailUser.publicRepos.toString())
        if (detailUser.company == null){
            detailUserBinding.includeCompany.imgCompanyDetail.visibility = View.GONE
            detailUserBinding.includeCompany.tvCompanyDetail.visibility = View.GONE
        } else {
            detailUserBinding.includeCompany.imgCompanyDetail.setImageResource(R.drawable.ic_apartment)
            detailUserBinding.includeCompany.tvCompanyDetail.text = detailUser.company
        }
        if (detailUser.location == null){
            detailUserBinding.includeLocation.imgLocationDetail.visibility = View.GONE
            detailUserBinding.includeLocation.tvLocationDetail.visibility = View.GONE
        } else {
            detailUserBinding.includeLocation.imgLocationDetail.setImageResource(R.drawable.ic_location)
            detailUserBinding.includeLocation.tvLocationDetail.text = detailUser.location
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val EXTRA_USERNAME = "extra_username"
    }
}