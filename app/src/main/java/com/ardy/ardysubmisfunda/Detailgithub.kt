package com.ardy.ardysubmisfunda


import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.ardy.ardysubmisfunda.databinding.ActivityDetailgithubBinding
import com.ardy.ardysubmisfunda.db.FavContract
import com.ardy.ardysubmisfunda.db.FavContract.FavColumns.Companion.CONTENT_URI
import com.ardy.ardysubmisfunda.db.FavHelperQuery
import com.ardy.ardysubmisfunda.entity.FavDat
import com.ardy.ardysubmisfunda.helper.MappingHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject


class Detailgithub : AppCompatActivity() {
    private lateinit var binding: ActivityDetailgithubBinding
    private lateinit var favhelper: FavHelperQuery
    private var position: Int = 0
    private var statusFav = false
    private var fav: FavDat? = null
    private lateinit var uriWithId: Uri

    companion object {
        private val TAG = Detailgithub::class.java.simpleName
        const val ARG_section_username= "username"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val EXTRA_USER = "extra_user"

        const val ALERT_DIALOG_CLOSE = 10
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_DELETE = 20

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,)
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.title = getString(R.string.detail_act)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailgithubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if ( intent?.getParcelableExtra(ARG_section_username) as? Github != null ) {
            val user = intent.getParcelableExtra(ARG_section_username) as Github
            val item = intent.getParcelableExtra(ARG_section_username) as Github
            val sectionsPagerAdapter = SectionsPagerAdapter(this)
            sectionsPagerAdapter.username = user?.username
            binding.viewPager.adapter = sectionsPagerAdapter
            gettext(item.username)
            favhelper = FavHelperQuery.getInstance(applicationContext)
            favhelper.open()
            user.username?.let { gettext(it) }
            val cursor: Cursor = favhelper.queryByuser(user.username.toString())
            if (cursor.moveToNext()) {
                statusFav = true
                setstatfav(true)
            }
        }

        if ( intent?.getParcelableExtra(EXTRA_USER) as? FavDat != null) {
            val userr = intent.getParcelableExtra(EXTRA_USER) as FavDat
            val sectionsPagerAdapter = SectionsPagerAdapter(this)
            sectionsPagerAdapter.username = userr?.username
            binding.viewPager.adapter = sectionsPagerAdapter
            favhelper = FavHelperQuery.getInstance(applicationContext)
            favhelper.open()
            userr.username?.let { gettext(it) }
            val cursor: Cursor = favhelper.queryByuser(userr.username.toString())
            if (cursor.moveToNext()) {
                statusFav = true
                setstatfav(true)
            }
        }

    }


    private fun setstatfav(statusFav : Boolean){

        if (statusFav)
        {
            binding.floatingActionButton?.imageTintList = AppCompatResources.getColorStateList(this, R.color.pink)
        }

        else{

            binding.floatingActionButton?.imageTintList  = AppCompatResources.getColorStateList(this, android.R.color.white)

        }

    }

    private fun gettext(username : String) {

        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization","token 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")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(Detailgithub.TAG, result)
                Log.d(Detailgithub.TAG,"berhasil oyyyyyyyyyyyyyyyyyyy")
                try {
                    val responseObject = JSONObject(result)
                    val GithubUser = Github()
                    GithubUser.username = responseObject .getString("login")
                    GithubUser.photo = responseObject .getString("avatar_url")
                    GithubUser.name = responseObject .getString("name")
                    GithubUser.repository = responseObject .getString("public_repos")
                    GithubUser.company = responseObject .getString("company").toString()
                    GithubUser.location  = responseObject .getString("location")
                    GithubUser.followers  = responseObject .getString("followers")
                    GithubUser.following  = responseObject .getString("following")

                    binding.txtUsername.text = "("+"@" +   GithubUser.username+")"
                    binding.txtKerja.text =   GithubUser.company
                    binding.txtRepos.text =   GithubUser?.repository
                    binding.txtRepo.text  = getString(R.string.repository)
                    binding.txtLocation.text =   GithubUser?.location
                    binding.txtNamaGithub.text =   GithubUser?.name
                    val  Werswing = intArrayOf(GithubUser.followers.toInt(),GithubUser.following.toInt())
                    TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                        tab.text = resources.getString(Detailgithub.TAB_TITLES[position], Werswing[position])
                        if  (binding.txtKerja.text == "null" )
                        {
                            binding.txtKerja.text = getString(R.string.not_avail)
                        }

                        if (binding.txtRepos.text == "null" )
                        {
                            binding.txtRepos.text = getString(R.string.not_avail)

                        }
                        if (binding.txtLocation.text == "null" )
                        {
                            binding.txtLocation.text = getString(R.string.not_avail)

                        }
                        if (binding.txtNamaGithub.text == "null" )
                        {
                            binding.txtNamaGithub.text = getString(R.string.not_avail)

                        }
                    }.attach()
                    binding.tabs?.elevation = 0f

                    with(binding){
                        Glide.with(this.profileImage)
                            .load(  GithubUser.photo)
                            .apply(RequestOptions().override(500, 500))
                            .into(profileImage)
                    }

                    binding.imgShare?.setOnClickListener {

                        try {
                            val message: String =  "Follow " + GithubUser.username +" On Github!!!  " + "("+url+")"
                            val intent = Intent(android.content.Intent.ACTION_SEND)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra(Intent.EXTRA_TEXT, message)
                            intent.type = "text/plain"
                            startActivity(Intent.createChooser(intent, "Share image via"))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    if (statusFav==true)
                    {
                        setstatfav(statusFav)
                        binding.floatingActionButton?.setOnClickListener {
                            statusFav = !statusFav
                            showAlertDialog(ALERT_DIALOG_DELETE)
                            setstatfav(statusFav)

                        }

                    }

                    if (statusFav==false) {
                        setstatfav(statusFav)
                        binding.floatingActionButton?.setOnClickListener {
                            statusFav = !statusFav
                            setstatfav(statusFav)
                            if (statusFav==true)
                            {
                                setstatfav(statusFav)
                                binding.floatingActionButton?.setOnClickListener {
                                    statusFav = !statusFav
                                    showAlertDialog(ALERT_DIALOG_DELETE)
                                    setstatfav(statusFav)

                                }

                            }
                        val username = GithubUser.username
                        val photo = GithubUser.photo

                        fav?.username = username
                        fav?.photo = photo

                        val intent = Intent()
                        intent.putExtra(EXTRA_NOTE, fav)
                        intent.putExtra(EXTRA_POSITION, position)
                        val values = ContentValues()
                        values.put(FavContract.FavColumns.username, username)
                        values.put(FavContract.FavColumns.avatar, photo)
                        contentResolver.insert(CONTENT_URI, values)
                        Toast.makeText(this@Detailgithub, "Satu item berhasil disimpan", Toast.LENGTH_SHORT).show()
                        }
                    }





                    } catch (e: Exception) {
                    Toast.makeText(this@Detailgithub, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }


            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@Detailgithub, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if ( intent?.getParcelableExtra(EXTRA_USER) as? FavDat != null || intent?.getParcelableExtra(ARG_section_username) as? Github != null ) {

                fav = intent.getParcelableExtra(EXTRA_USER)

            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + fav?.id)
            val cursor = contentResolver.query(uriWithId, null, null, null, null)
            if (cursor != null) {
                fav = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
            if (isDialogClose) {
                dialogTitle = "Batal"
                dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
            } else {
                dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
                dialogTitle = "Hapus Favorite"
            }

            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle(dialogTitle)
            alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya") { _, _ ->
                    if (isDialogClose) {
                        statusFav = true
                        setstatfav(true)
                        finish()
                    } else {
                        if ( intent?.getParcelableExtra(EXTRA_USER) as? FavDat != null) {
                            contentResolver.delete(uriWithId, null, null)
                            Toast.makeText(this, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        if (intent?.getParcelableExtra(ARG_section_username) as? Github != null) {

                            val user = intent?.getParcelableExtra(ARG_section_username) as? Github

                            GlobalScope.launch(Dispatchers.Main) {

                                val deferredNotes = async(Dispatchers.IO) {

                                    val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                                    MappingHelper.mapCursorToArrayList(cursor)
                                }
                                val notes = deferredNotes.await()


                                for(i in 0 until notes.size){
                                    Log.d(TAG, notes.get(position).username.toString())



                                    if (user?.username == notes.get(i).username.toString() )
                                    {
                                        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + notes.get(i).id.toString())

                                        contentResolver.delete(uriWithId, null, null)
                                        Toast.makeText(this@Detailgithub, "Satu item berhasil dihapus", Toast.LENGTH_SHORT)
                                            .show()
                                        finish()
                                    }
                                }

                            }
                        }
                    }
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    statusFav = true
                    setstatfav(true)
                    dialog.cancel() }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } }

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
            val intent = Intent(this@Detailgithub , settings::class.java)
            startActivity((intent))
        }

        if (item.itemId == R.id.fav) {
            val intent = Intent(this@Detailgithub , Favorite::class.java)
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
        val move =  Intent (this@Detailgithub, MainActivity::class.java)

        startActivity(move)
    }


}