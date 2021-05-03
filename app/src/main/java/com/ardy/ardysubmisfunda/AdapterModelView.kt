package com.ardy.ardysubmisfunda

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ardy.ardysubmisfunda.databinding.GithubListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class AdapterModelView: RecyclerView.Adapter<AdapterModelView.WeatherViewHolder>() {

    private val mData = ArrayList<Github>()

    fun setData(items: ArrayList<Github>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): WeatherViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.github_list, viewGroup, false)
        return WeatherViewHolder(mView)
    }

    override fun onBindViewHolder(weatherViewHolder: WeatherViewHolder, position: Int) {
        weatherViewHolder.bind(mData[position])

        weatherViewHolder.itemView.setOnClickListener() {

            val activity = weatherViewHolder.itemView.context as Activity
            val intent = Intent(activity , Detailgithub::class.java).apply {
                putExtra(Detailgithub.ARG_section_username, mData[position])

            }
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = mData.size

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = GithubListBinding.bind(itemView)
        fun bind(gith: Github) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(gith.photo)
                    .apply(RequestOptions().override(200, 200))
                    .into(binding.imgItemPhoto)
                binding.tvItemName.text = gith.username
                itemView.setOnClickListener { Toast.makeText(itemView.context, "Kamu memilih ${gith.username}", Toast.LENGTH_SHORT).show()


                }
            }
        }

    }
}