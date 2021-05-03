package com.ardy.consumerfavapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject


class MainViewModel : ViewModel() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    val listGith = MutableLiveData<ArrayList<Github>>()



    fun getGith(): LiveData<ArrayList<Github>> {
        return listGith
    }



    fun setItems(username : String) {
        val listuser = ArrayList<Github>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization","token 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")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(TAG, result)
                Log.d(TAG,"berhasil oyyyyyyyyyyyyyyyyyyy")
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    for(i in 0 until items.length()){
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val user = Github()
                        user.username = username
                        user.photo = avatar
                        listuser.add(user)
                    }
                    listGith.postValue(listuser)



                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {


                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("onFailure", errorMessage)
            }
        })
    }

}