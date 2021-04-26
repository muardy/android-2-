package com.ardy.ardysubmisfunda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.ardy.ardysubmisfunda.databinding.ActivityDetailgithubBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject


class Detailgithub : AppCompatActivity() {
    private lateinit var binding: ActivityDetailgithubBinding
    companion object {
        private val TAG = Detailgithub::class.java.simpleName
        const val ARG_section_username= "username"

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
        val user = intent.getParcelableExtra(ARG_section_username) as Github
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
            sectionsPagerAdapter.username = user?.username
            binding.viewPager.adapter = sectionsPagerAdapter
        val item = intent.getParcelableExtra(ARG_section_username)  as Github
        gettext(item.username)

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
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)

            startActivity(mIntent)
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