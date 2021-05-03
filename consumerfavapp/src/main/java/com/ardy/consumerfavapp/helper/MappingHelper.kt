package com.ardy.consumerfavapp.helper

import android.database.Cursor
import com.ardy.consumerfavapp.db.FavContract
import com.ardy.consumerfavapp.entity.FavDat

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<FavDat> {
        val notesList = ArrayList<FavDat>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(FavContract.FavColumns._ID))
                val username = getString(getColumnIndexOrThrow(FavContract.FavColumns.username))
                val avatar = getString(getColumnIndexOrThrow(FavContract.FavColumns.avatar))

                notesList.add(FavDat(id, username, avatar))
            }
        }
        return notesList
    }

    fun mapCursorToObject(notesCursor: Cursor?): FavDat {
        var note = FavDat()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(FavContract.FavColumns._ID))
            val username = getString(getColumnIndexOrThrow(FavContract.FavColumns.username))
            val avatar = getString(getColumnIndexOrThrow(FavContract.FavColumns.avatar))
            note = FavDat(id,username, avatar)
        }
        return note
    }

}