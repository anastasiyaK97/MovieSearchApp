package com.example.moviesearchapplication.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.data.FilmRepository

class FilmDetailActivity : AppCompatActivity() {

    companion object {
        const val FILM_EXTRA = "FILM_EXTRA"
    }

    private var filmId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.film_detail_layout)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        filmId = intent.getIntExtra(FILM_EXTRA, 0)
        with (FilmRepository.filmCollection[filmId]) {
            findViewById<TextView>(R.id.film_name).text = this.name
            findViewById<TextView>(R.id.descr).text = this.descr
            findViewById<ImageView>(R.id.imageView).setImageResource(this.imageID)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_secondary, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent().apply {
            putExtra(FILM_EXTRA, filmId)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }

}