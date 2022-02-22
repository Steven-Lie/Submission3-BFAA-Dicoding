package com.dicoding.githubusers.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers.R
import com.dicoding.githubusers.ui.detailUser.DetailUserActivity
import com.dicoding.githubusers.adapter.UserAdapter
import com.dicoding.githubusers.api.UsersData
import com.dicoding.githubusers.databinding.ActivityMainBinding
import com.dicoding.githubusers.ui.favorite.FavoriteActivity
import com.dicoding.githubusers.ui.setting.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = mainBinding.searchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "" && newText != null) {
                    mainViewModel.findUser(newText, this@MainActivity)
                } else {
                    mainViewModel.getUser()
                }
                return false
            }
        })

        mainViewModel.listUsers.observe(this, { users ->
            showUsers(users)
        })

        mainViewModel.isLoading.observe(this, { isLoading ->
            mainBinding.progressBarSearch.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.main_menu_setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                true
            }
        }
    }

    private fun showUsers(users: List<UsersData>) {
        if (users.isEmpty()) {
            mainBinding.tvNoResultSearch.visibility = View.VISIBLE
        } else {
            mainBinding.tvNoResultSearch.visibility = View.GONE
        }
        mainBinding.rvUsersSearch.layoutManager = LinearLayoutManager(this)
        val userAdapter = UserAdapter(users)
        mainBinding.rvUsersSearch.adapter = userAdapter
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersData) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UsersData) {
        val detailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
        startActivity(detailUserIntent)
    }
}