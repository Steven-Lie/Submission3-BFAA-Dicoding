package com.dicoding.githubusers.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers.BuildConfig
import com.dicoding.githubusers.R
import com.dicoding.githubusers.adapter.FavoriteAdapter
import com.dicoding.githubusers.databinding.ActivityFavoriteBinding
import com.dicoding.githubusers.helper.FavoriteViewModelFactory
import com.dicoding.githubusers.ui.setting.SettingActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorites().observe(this, {
            if (it != null) {
                adapter.setListFavorites(it)
            }
            if (it.isEmpty()){
                favoriteBinding.tvNoResultFavorite.visibility = View.VISIBLE
            } else {
                favoriteBinding.tvNoResultFavorite.visibility = View.GONE
            }
        })

        adapter = FavoriteAdapter()
        favoriteBinding.rvFavorite.layoutManager = LinearLayoutManager(this)
        favoriteBinding.rvFavorite.setHasFixedSize(true)
        favoriteBinding.rvFavorite.adapter = adapter
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

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}