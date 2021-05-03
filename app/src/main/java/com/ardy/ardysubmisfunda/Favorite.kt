package com.ardy.ardysubmisfunda

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardy.ardysubmisfunda.adapter.FavAdapter
import com.ardy.ardysubmisfunda.databinding.ActivityFavoriteBinding
import com.ardy.ardysubmisfunda.db.FavContract.FavColumns.Companion.CONTENT_URI
import com.ardy.ardysubmisfunda.entity.FavDat
import com.ardy.ardysubmisfunda.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Favorite : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavAdapter
    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.fav)

        binding.rvGithub.layoutManager = LinearLayoutManager(this)
        binding.rvGithub.setHasFixedSize(true)
        adapter = FavAdapter(this)
        binding.rvGithub.adapter = adapter


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        }
        else {

            val list = savedInstanceState.getParcelableArrayList<FavDat>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                Detailgithub.REQUEST_ADD -> if (resultCode == Detailgithub.RESULT_ADD) {
                    val note = data.getParcelableExtra<FavDat>(Detailgithub.EXTRA_NOTE) as FavDat
                    adapter.addItem(note)
                    binding.rvGithub.smoothScrollToPosition(adapter.itemCount - 1)
                }

                Detailgithub.RESULT_DELETE -> {
                    val position = data.getIntExtra(Detailgithub.EXTRA_POSITION, 0)
                    adapter.removeItem(position)
                }
            }
        }
    }
    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList()
            }
            binding.progressBar.visibility = View.INVISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_git, menu)
        val  search : MenuItem = menu.findItem(R.id.search);
        search.setVisible(false);
        var actionBar = getSupportActionBar()

        if (actionBar != null) {


            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        if (item.itemId == R.id.setting) {
            val intent = Intent(this@Favorite , settings::class.java)
            startActivity((intent))
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            android.R.id.home  -> {
                Findah()
            }

        }
    }


    private fun Findah() {

        val move =  Intent (this@Favorite, MainActivity::class.java)
        startActivity(move)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }
}