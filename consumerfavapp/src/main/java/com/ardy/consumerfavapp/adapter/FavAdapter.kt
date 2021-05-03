package com.ardy.consumerfavapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ardy.consumerfavapp.Detailgithub
import com.ardy.consumerfavapp.R
import com.ardy.consumerfavapp.databinding.FavoriteListBinding
import com.ardy.consumerfavapp.entity.FavDat
import com.bumptech.glide.request.RequestOptions

class FavAdapter (private val activity: Activity) : RecyclerView.Adapter<FavAdapter.NoteViewHolder>() {
    var listNotes = ArrayList<FavDat>()
        set(listNotes) {

            this.listNotes.clear()
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

    fun addItem(note: FavDat) {
        this.listNotes.add(note)
        notifyItemInserted(this.listNotes.size - 1)
    }

    fun updateItem(position: Int, note: FavDat) {
        this.listNotes[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNotes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_list, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
        holder.itemView.setOnClickListener() {

            val activity = holder.itemView.context as Activity
            val intent = Intent(activity , Detailgithub::class.java).apply {
                putExtra(Detailgithub.EXTRA_USER, listNotes[position])

            }
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = this.listNotes.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FavoriteListBinding.bind(itemView)
        fun bind(note: FavDat) {
            com.bumptech.glide.Glide.with(itemView.context)
                .load(note.photo)
                .apply(RequestOptions().override(200, 200))
                .into(binding.imgItemPhoto)
            binding.tvItemName.text = note.username

        }
    }
}

