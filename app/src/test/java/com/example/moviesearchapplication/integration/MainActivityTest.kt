package com.example.moviesearchapplication.integration

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.example.moviesearchapplication.presentation.view.FilmDetailFragment
import com.example.moviesearchapplication.presentation.view.MainActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    lateinit var activity: MainActivity

    @Before
    fun setUp(){
        activity = buildActivity(MainActivity::class.java).create().start().resume().get()
    }

    @Test
    fun whenActivityResume_shouldLoadFragment() {
        val fragment = activity
            .supportFragmentManager
            .findFragmentByTag("fragment")
        Assert.assertNotNull(fragment)
    }

    @Test
    fun whenActivityCreatedWithIntent_detailFragmentIsShownToTheUser() {
        val filmId = 838
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java).apply {
            putExtra(FilmDetailFragment.FILM_ID_EXTRA, filmId)
            action = "notification $filmId"
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val newActivity =
            buildActivity(MainActivity::class.java, intent).create().newIntent(intent).start().resume().get()
        val fragment = newActivity
            .supportFragmentManager
            .findFragmentByTag("detail fragment")
        Assert.assertNotNull(fragment)
    }

}