package com.example.moviesearchapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesearchapplication.data.Film

class FilmDetailActivity : AppCompatActivity() {

    companion object {
        const val FILM_EXTRA = "FILM_EXTRA"
        const val IS_LIKED_EXTRA = "IS_LIKED"
        const val COMMENT_EXTRA = "COMMENT_TEXT"
    }
    private val checkBox by lazy { findViewById<CheckBox>(R.id.checkBox) }
    private val commentET by lazy { findViewById<EditText>(R.id.editTextComment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.film_detail_layout)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val film = intent.getParcelableExtra<Film>(FILM_EXTRA)
        film?.let {
            findViewById<TextView>(R.id.film_name).text = it.name
            findViewById<TextView>(R.id.descr).text = it.descr
            findViewById<ImageView>(R.id.imageView).setImageResource(it.imageID)
        }

        checkBox.setOnCheckedChangeListener {
                buttonView, isChecked ->
            if (isChecked) {
                commentET.visibility = View.VISIBLE
            } else {
                commentET.visibility = View.INVISIBLE
                commentET.setText("")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bool = checkBox.isChecked
        outState.putBoolean(IS_LIKED_EXTRA, checkBox.isChecked)
        outState.putString(COMMENT_EXTRA, commentET.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            checkBox.isChecked = savedInstanceState.getBoolean(IS_LIKED_EXTRA)
            commentET.setText(savedInstanceState.getString(COMMENT_EXTRA, ""))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_secondary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {val intent = Intent().apply {
                putExtra(IS_LIKED_EXTRA, checkBox.isChecked)
                putExtra(COMMENT_EXTRA, commentET.text.toString())
            }
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}