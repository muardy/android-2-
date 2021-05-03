package com.ardy.ardysubmisfunda

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.ardy.ardysubmisfunda.db.FavHelperQuery
import com.ardy.ardysubmisfunda.helper.MappingHelper
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers


internal class StackRemoteViewsFactory (private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<Bitmap>()

    companion object {

        private val TAG = StackRemoteViewsFactory::class.java.simpleName
         }


    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val noteHelper = FavHelperQuery.getInstance(mContext)
        noteHelper.open()
        val cursor = noteHelper.queryAll()
        MappingHelper.mapCursorToArrayList(cursor)
        val deferredNotes = synchronized(Dispatchers.IO) {
        val cursor = noteHelper.queryAll()
        MappingHelper.mapCursorToArrayList(cursor)
        }
        val notes = deferredNotes
            for(i in 0 until notes.size){
                Log.d(TAG, notes.get(i).photo.toString())
                val bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(notes.get(i).photo.toString())
                    .submit(512, 512)
                    .get()
                mWidgetItems.add(bitmap)
            }

    }

    override fun onDestroy() {

    }

    override fun getCount(): Int =  mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val noteHelper = FavHelperQuery.getInstance(mContext)
        noteHelper.open()
        val cursor = noteHelper.queryAll()
        MappingHelper.mapCursorToArrayList(cursor)
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])
        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}