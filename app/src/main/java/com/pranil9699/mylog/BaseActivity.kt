package com.pranil9699.mylog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {

    protected lateinit var topAppBar: MaterialToolbar
    protected lateinit var bottomNav: BottomNavigationView

    fun bindTopAppBar(toolbar: MaterialToolbar) {
        topAppBar = toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setContentLayout(layoutResId: Int) {
        val container = findViewById<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(layoutResId, container, true)
    }

    protected fun setupNavigation(currentItemId: Int) {
        setupToolbarAndBottomNav(R.id.topAppBar, R.id.bottomNav, currentItemId)
    }

    protected fun setupToolbarAndBottomNav(toolbarId: Int, bottomNavId: Int, currentItemId: Int) {
        val toolbar = findViewById<MaterialToolbar>(toolbarId)
        if (toolbar == null) {
            Log.e("BaseActivity", "Toolbar not found! Check if ID 'topAppBar' exists in your layout.")
        }
        setupTopAppBar(toolbar!!) // Will crash if null, but helps you debug why

        val bottomNavigation = findViewById<BottomNavigationView>(bottomNavId)
        if (bottomNavigation == null) {
            Log.e("BaseActivity", "BottomNavigationView not found! Check if ID 'bottomNav' exists in your layout.")
        }
        setBottomNavigationBar(bottomNavigation!!, currentItemId)
    }

    private fun setupTopAppBar(toolbar: MaterialToolbar) {
        topAppBar = toolbar
        topAppBar.setNavigationOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }


    private fun setBottomNavigationBar(bottomNavigationView: BottomNavigationView, currentItemId: Int) {
        bottomNav = bottomNavigationView
        bottomNav.selectedItemId = currentItemId

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_log -> {
                    if (this !is HomeActivity) startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    true
                }
                R.id.nav_recommendation -> {
                    startActivity(Intent(this, RecommendationActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
