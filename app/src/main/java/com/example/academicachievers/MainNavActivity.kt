package com.example.academicachievers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainNavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)


        if (savedInstanceState == null) {
            switchFragment(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    switchFragment(HomeFragment())
                    true
                }

                R.id.nav_scholarships -> {

                    val intent = Intent(this, WebViewActivity::class.java)
                    intent.putExtra("path", "/scholarships")
                    startActivity(intent)
                    false
                }

                R.id.nav_eligibility -> {

                    switchFragment(EligibilityFragment())
                    true
                }

                R.id.nav_profile -> {
                    switchFragment(ProfileFragment())
                    true
                }

                R.id.nav_more -> {
                    switchFragment(MoreFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
