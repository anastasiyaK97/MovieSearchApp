package com.example.moviesearchapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.moviesearchapplication.presentation.view.FilmRecyclerViewAdapter
import com.example.moviesearchapplication.presentation.view.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class FilmListUITest {

    private val LIST_ITEM_TO_CLICK = 1

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test_isListFragmentVisible_onAppLaunch() {
        Espresso.onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun test_selectListItem_isDetailFilmFragmentVisible() {
        Espresso.onView(withId(R.id.list))
            .perform(
                actionOnItemAtPosition<FilmRecyclerViewAdapter.FilmViewHolder>(
                    LIST_ITEM_TO_CLICK,
                    click()
                )
            )
        Espresso.onView(withId(R.id.film_name)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.descr)).check(matches(isDisplayed()))
    }

    @Test
    @Throws(androidx.test.espresso.PerformException::class)
    fun test_backNavigation_toTheFilmListFragment() {
        Espresso.onView(withId(R.id.list))
            .perform(
                actionOnItemAtPosition<FilmRecyclerViewAdapter.FilmViewHolder>(
                    LIST_ITEM_TO_CLICK,
                    click()
                )
            )
        Espresso.pressBack()
        Espresso.onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun test_clickMovieItemLikeIcon_isMovieAddedInFavoriteList() {
        Espresso.onView(withId(R.id.list))
            .perform(
                actionOnItemAtPosition<FilmRecyclerViewAdapter.FilmViewHolder>(
                    LIST_ITEM_TO_CLICK,
                    LikeViewAction.clickLikeIconViewWithId(R.id.like)
            )
        )
        Thread.sleep(1000)
    }


}
