package com.example.moviesearchapplication.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.moviesearchapplication.App
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.presentation.viewmodel.FilmListViewModel
import com.example.moviesearchapplication.presentation.viewmodel.MainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    OnFilmClickListener, OnWatchesClickListeners {

    companion object {
        const val TAG = "LOG_TAG"
    }
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: FilmListViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.instance.applicationComponent.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FilmListViewModel::class.java)
        initToolbar()
        loadFragment(MainFilmsFragment())
        initBottomNavigation()
    }

    private fun initToolbar(){
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    private fun initBottomNavigation(){
        val navigate = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigate.setOnNavigationItemSelectedListener{
            when (it.itemId) {
                R.id.nav_main -> {
                    loadFragment(MainFilmsFragment())
                }
                R.id.nav_favorite -> {
                    loadFragment(FavoriteFilmsFragment())
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, "fragment")
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
        if (supportFragmentManager.backStackEntryCount >  0) {
            supportFragmentManager.popBackStack()
        } else {
            openExitDialog()
        }
    }

    fun openExitDialog(){
        supportFragmentManager.beginTransaction()
        val dialog = ExitDialog()
        dialog.show(supportFragmentManager, "dialog")
    }

    override fun onClick(itemId : Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, FilmDetailFragment.newInstance(itemId))
            .addToBackStack("")
            .commit()
    }

    override fun onWatchIconClick(itemId : Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, SetUpWatchLaterFragment.newInstance(itemId))
            .addToBackStack("")
            .commit()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val data = intent.getIntExtra(FilmDetailFragment.FILM_ID_EXTRA, -1)
            if (data != -1) {
                val fragment: Fragment = FilmDetailFragment.newInstance(data)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_placeholder, fragment, "detail fragment")
                    .addToBackStack(null)
                    .commit()
                viewModel.resetWatchLaterState(data)
            }
        }
    }

}