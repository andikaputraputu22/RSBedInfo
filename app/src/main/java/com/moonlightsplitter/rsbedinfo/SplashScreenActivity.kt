package com.moonlightsplitter.rsbedinfo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {

    lateinit var context: Context
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        context = this

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}