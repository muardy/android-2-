package com.ardy.consumerfavapp

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardy.consumerfavapp.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity(){

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: AdapterModelView
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Github App"
        showRecyclerList()

    }


private fun showRecyclerList(){
    adapter = AdapterModelView()
    adapter.notifyDataSetChanged()
    binding.rvGithub.layoutManager = LinearLayoutManager(this)
    binding.rvGithub.adapter= adapter

    mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    mainViewModel.getGith().observe(this, { Github ->
        if (Github != null) {
            adapter.setData(Github)
        }
    })
}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_git, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        adapter = AdapterModelView()
        adapter.notifyDataSetChanged()
        binding.rvGithub.layoutManager = LinearLayoutManager(this)
        binding.rvGithub.adapter= adapter
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainViewModel.getGith().observe(this, { Github ->
            if (Github != null) {
                showRecyclerList()
                adapter.setData(Github)
                binding.progressBar.visibility = View.INVISIBLE
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.progressBar.visibility = View.VISIBLE
                mainViewModel.setItems(query)
                searchView.clearFocus()
                return true

            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {

            val intent = Intent(this@MainActivity , settings::class.java)
            startActivity((intent))
        }


        if (item.itemId == R.id.fav) {
            val intent = Intent(this@MainActivity , Favorite::class.java)
            startActivity((intent))
        }
        return super.onOptionsItemSelected(item)
    }
}