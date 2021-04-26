package com.ardy.ardysubmisfunda

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardy.ardysubmisfunda.databinding.FragmentFollowingBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray



class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding

    private var list = ArrayList<Github>()
    companion object {
        @JvmStatic
      private val  ARG_section_username = "username"
        private val TAG = FollowingFragment::class.java.simpleName

        fun newInstance(Username: String?)= FollowingFragment().apply {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_section_username, Username)

            fragment.arguments  = bundle
            return fragment


        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFollowingBinding.inflate(inflater,container,false)
        val view = binding.root
        return view

    }
    private fun showRecyclerList() {
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        val listadapterg = Adapterg(list)
        binding.rvFollowing.adapter= listadapterg


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_section_username)
        showRecyclerList()
        getfollowing(username)
    }


    private fun getfollowing(username : String? ) {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization","token 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")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                binding.progressBar.visibility = View.INVISIBLE
                val listuser = ArrayList<Github>()
                val result = String(responseBody)
                Log.d(TAG, result)
                Log.d(TAG,"berhasil oyyyyyyyyyyyyyyyyyyy")

                try {

                    val items = JSONArray(result)


                    for(i in 0 until items.length()){

                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val user = Github()
                        user.username = username
                        user.photo = avatar
                        listuser.add(user)

                    }
                    showRecyclerList()
                    list.addAll(listuser)
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal

                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}