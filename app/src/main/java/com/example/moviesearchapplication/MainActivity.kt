package com.example.moviesearchapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.moviesearchapplication.data.Film
import com.example.moviesearchapplication.data.FilmRepository

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val INDEX_BUNDLE = "INDEX_BUNDLE"
        const val COMMENT_CODE = 1
        const val TAG = "LOG_TAG"
    }

    private val filmName1 by lazy { findViewById<TextView>(R.id.film_name1) }
    private val filmName2 by lazy { findViewById<TextView>(R.id.film_name2) }
    private val filmName3 by lazy { findViewById<TextView>(R.id.film_name3) }
    private var selectedIndex : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val films : ArrayList<Film> = FilmRepository.filmCollectoin

        filmName1.text = films[0].name
        filmName2.text = films[1].name
        filmName3.text = films[2].name

        findViewById<View>(R.id.button1).setOnClickListener(this)
        findViewById<View>(R.id.button2).setOnClickListener(this)
        findViewById<View>(R.id.button3).setOnClickListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    override fun onClick(v: View?) {
        val intent = Intent(this, FilmDetailActivity::class.java)
        var film : Film? = null

        when (v?.id) {
            R.id.button1 -> {
                film = FilmRepository.filmCollectoin[0]
                selectedIndex = 0
            }
            R.id.button2 -> {
                film = FilmRepository.filmCollectoin[1]
                selectedIndex = 1
            }
            R.id.button3 -> {
                film = FilmRepository.filmCollectoin[2]
                selectedIndex = 2
            }
        }
        changeColorIfSelected()
        intent.putExtra(FilmDetailActivity.FILM_EXTRA, film)

        startActivityForResult(intent, COMMENT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == COMMENT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG, "Фильм пользователю " +
                    "${if (data.getBooleanExtra(FilmDetailActivity.IS_LIKED_EXTRA, false)) "нравится" else "не нравится"}. " +
                    "Комментарий: ${data.getStringExtra(FilmDetailActivity.COMMENT_EXTRA)}")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedIndex?.let {
            outState.putInt(INDEX_BUNDLE, selectedIndex!!)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            selectedIndex = it.getInt(INDEX_BUNDLE)
            changeColorIfSelected()
        }
    }

    private fun changeColorIfSelected(){
        when (selectedIndex) {
            0 -> {
                filmName1.setBackgroundColor(resources.getColor(R.color.text_background_selected))

                filmName2.setBackgroundColor(resources.getColor(R.color.text_background_main))
                filmName3.setBackgroundColor(resources.getColor(R.color.text_background_main))
            }
            1 -> {
                filmName2.setBackgroundColor(resources.getColor(R.color.text_background_selected))

                filmName1.setBackgroundColor(resources.getColor(R.color.text_background_main))
                filmName3.setBackgroundColor(resources.getColor(R.color.text_background_main))
            }
            2 -> {
                filmName3.setBackgroundColor(resources.getColor(R.color.text_background_selected))

                filmName1.setBackgroundColor(resources.getColor(R.color.text_background_main))
                filmName2.setBackgroundColor(resources.getColor(R.color.text_background_main))
            }
        }
    }
}