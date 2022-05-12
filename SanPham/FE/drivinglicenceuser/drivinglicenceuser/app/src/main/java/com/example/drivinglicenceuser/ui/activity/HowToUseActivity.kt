package com.example.drivinglicenceuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.drivinglicenceuser.R
import com.example.drivinglicenceuser.databinding.ActivityHowToUseBinding

class HowToUseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHowToUseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHowToUseBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_how_to_use)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}