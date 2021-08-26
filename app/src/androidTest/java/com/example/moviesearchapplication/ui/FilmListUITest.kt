package com.example.moviesearchapplication.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.moviesearchapplication.R
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
        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun test_selectListItem_isDetailFilmFragmentVisible() {
        onView(withId(R.id.list))
            .perform(
                actionOnItemAtPosition<FilmRecyclerViewAdapter.FilmViewHolder>(
                    LIST_ITEM_TO_CLICK,
                    click()
                )
            )
        onView(withId(R.id.film_name)).check(matches(isDisplayed()))
        onView(withId(R.id.descr)).check(matches(isDisplayed()))
    }

    @Test
    fun test_backNavigation_toTheFilmListFragment() {
        onView(withId(R.id.list))
            .perform(
                actionOnItemAtPosition<FilmRecyclerViewAdapter.FilmViewHolder>(
                    LIST_ITEM_TO_CLICK,
                    click()
                )
            )
        Espresso.pressBack()
        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun test_clickMovieItemLikeIcon_isMovieAddedInFavoriteList() {
        val testFilmTitle = "Интерстеллар"
        /*
            Find a movie in the list and add to favorite
         */
        onView(withId(R.id.list)).perform(
            RecyclerViewActions.actionOnHolderItem(
                RecyclerViewMatchers.withFilmTitle(
                    testFilmTitle
                ),
                LikeViewAction.clickLikeIconViewWithId(
                    R.id.like
                )
            )
        )
        /*
            Check if the movie is on the favorites list
        */
        onView(withId(R.id.nav_favorite)).perform(click())
        onView(withId(R.id.list)).check(matches(hasDescendant(withText(testFilmTitle))))

        /*
            Remove movie from favorites
        */
        onView(withId(R.id.nav_main)).perform(click())
        onView(withId(R.id.list)).perform(
            RecyclerViewActions.actionOnHolderItem(
                RecyclerViewMatchers.withFilmTitle(
                    testFilmTitle
                ),
                LikeViewAction.clickLikeIconViewWithId(
                    R.id.like
                )
            )
        )

    }


}
