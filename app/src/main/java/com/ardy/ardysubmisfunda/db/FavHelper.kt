package com.ardy.ardysubmisfunda.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ardy.ardysubmisfunda.db.FavContract.FavColumns.Companion.TABLE_NAME
import com.ardy.ardysubmisfunda.db.FavContract.FavColumns

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbtestt"

        private const val DATABASE_VERSION = 3

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${FavColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${FavColumns.username} TEXT NOT NULL," +
                " ${FavColumns.avatar} TEXT NOT NULL)"

    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}