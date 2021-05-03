package com.ardy.ardysubmisfunda


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ardy.ardysubmisfunda.databinding.ActivityFavoriteListtBinding


class favorite_listt : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteListtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteListtBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}