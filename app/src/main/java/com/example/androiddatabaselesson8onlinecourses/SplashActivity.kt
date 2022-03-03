package com.example.androiddatabaselesson8onlinecourses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddatabaselesson8onlinecourses.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {
    private val binding: ActivitySplashBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}