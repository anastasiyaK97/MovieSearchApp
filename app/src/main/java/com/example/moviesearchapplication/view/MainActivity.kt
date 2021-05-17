package com.example.moviesearchapplication.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.moviesearchapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LOG_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadFragment(MainFilmsFragment())

        val navigate = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigate.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.nav_main -> {
                    Toast.makeText(this,"Main", Toast.LENGTH_SHORT).show()
                    loadFragment(MainFilmsFragment())
                }
                R.id.nav_favorite -> {
                    Toast.makeText(this,"Favorite", Toast.LENGTH_SHORT).show()
                    loadFragment(FavoriteFilmsFragment())
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_invite -> {
                val email = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, "Приглашаю тебя в приложение по поиску фильмов. Ссылка в GooglePlay...")
                    type = "text/plain"
                }
                startActivity(email)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun onBackPressed() {
        ExitDialog {super.onBackPressed()}.show(supportFragmentManager, "dialog")
    }
}