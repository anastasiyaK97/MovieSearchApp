package com.example.moviesearchapplication.presentation.view

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.presentation.utilities.AlarmReceiver
import com.example.moviesearchapplication.presentation.viewmodel.FilmListViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(),
    OnFilmClickListener, OnWatchesClickListeners {

    private lateinit var viewModel: FilmListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(FilmListViewModel::class.java)

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
        if (supportFragmentManager.backStackEntryCount >  0) {
            supportFragmentManager.popBackStack()
        } else {
            ExitDialog (exitAction = {super.onBackPressed()})
                .show(supportFragmentManager, "dialog")
        }
    }

    override fun onClick(itemId : Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, FilmDetailFragment.newInstance(itemId))
            .addToBackStack("")
            .commit()
    }

    override fun onWatchIconClick(itemId : Int) {
        var date: Calendar = Calendar.getInstance()
        val listener =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, monthOfYear)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                createAlarm(date, itemId)
            }
        DatePickerDialog(
            this, listener,
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        )
            .show()
    }

    private fun createAlarm(date: Calendar, id: Int) {
        Executors.newSingleThreadScheduledExecutor().execute {
            val filmName = viewModel.getFilmTitleById(id)

            val intent = Intent(this, AlarmReceiver::class.java).apply {
                action = ALARM_ACTION
                putExtra(FILM_NAME_EXTRA, filmName)
                putExtra(FILM_ID_EXTRA, id)
            }
            val alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.YEAR, date.get(Calendar.YEAR));
                set(Calendar.MONTH, date.get(Calendar.MONTH)); // January has value 0
                set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
                //set(Calendar.HOUR_OF_DAY, 21);
                //set(Calendar.MINUTE, 14);
                //set(Calendar.SECOND, 0);
                //set(Calendar.AM_PM, Calendar.PM);
            }

            val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            alarmManager?.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )

            viewModel.updateFilmNotificationSettings(id)
        }

        Toast.makeText(this, R.string.success_toast_text, Toast.LENGTH_SHORT).show()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val data = intent.getIntExtra(FilmDetailFragment.FILM_ID_EXTRA, -1)
            if (data != -1) {
                val fragment: Fragment = FilmDetailFragment.newInstance(data)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_placeholder, fragment)
                    .addToBackStack(null)
                    .commit()
                Executors.newSingleThreadScheduledExecutor().execute {
                    viewModel.resetWatchLaterState(data)
                }
            }
        }
    }

    companion object {
        const val FILM_ID_EXTRA = "FILM_ID_EXTRA"
        const val FILM_NAME_EXTRA = "film name"

        const val LOG_TAG = "LOG TAG"
        const val ALARM_ACTION = "notification about film"
    }

}