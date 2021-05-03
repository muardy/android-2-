package com.ardy.ardysubmisfunda.db

import android.net.Uri
import android.provider.BaseColumns

object FavContract {

    const val AUTHORITY = "com.ardy.ardysubmisfunda"
    const val SCHEME = "content"


    internal class FavColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val username = "username"
            const val avatar = "avatar"


            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }

}