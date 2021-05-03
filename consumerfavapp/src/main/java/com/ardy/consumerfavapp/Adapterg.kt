package com.ardy.consumerfavapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ardy.consumerfavapp.databinding.GithubListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class Adapterg (val listLatih: ArrayList<Github>) : RecyclerView.Adapter<Adapterg.ListViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = GithubListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listLatih[position])
        holder.itemView.setOnClickListener() {

           val activity = holder.itemView.context as Activity
            val intent = Intent(activity , Detailgithub::class.java).apply {
                    putExtra(Detailgithub.ARG_section_username, listLatih[position])
                }
           activity.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = listLatih.size


    inner class ListViewHolder(private val binding: GithubListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gith: Github) {
            with(binding){
                Glide.with(itemView.context)
                .load(gith.photo)
                .apply(RequestOptions().override(200, 200))
                .into(imgItemPhoto)
            tvItemName.text = gith.username
            itemView.setOnClickListener { Toast.makeText(itemView.context, "Kamu memilih ${gith.username}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}