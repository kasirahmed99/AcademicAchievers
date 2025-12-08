package com.example.academicachievers

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGetStarted = findViewById<Button>(R.id.btnGetStarted)

        btnGetStarted.setOnClickListener {
            val intent = Intent(this, MainNavActivity::class.java)
            startActivity(intent)
        }
    }
}
