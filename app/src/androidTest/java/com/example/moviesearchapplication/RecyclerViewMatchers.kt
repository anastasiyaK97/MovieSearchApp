package com.example.moviesearchapplication

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.internal.util.Checks
import com.example.moviesearchapplication.presentation.view.FilmRecyclerViewAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher


class RecyclerViewMatchers {
    companion object{
        fun hasItem(matcher: Matcher<View?>): Matcher<View?>? {
            return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description) {
                    description.appendText("has item: ")
                    matcher.describeTo(description)
                }

                override fun matchesSafely(view: RecyclerView): Boolean {
                    val adapter = view.adapter
                    for (position in 0 until adapter!!.itemCount) {
                        val type = adapter.getItemViewType(position)
                        val holder = adapter.createViewHolder(view, type)
                        adapter.onBindViewHolder(holder, position)
                        if (matcher.matches(holder.itemView)) {
                            return true
                        }
                    }
                    return false
                }
            }
        }

        fun withFilmTitle(title: String): Matcher<RecyclerView.ViewHolder?>? {
            Checks.checkNotNull(title)
            return object : BoundedMatcher<RecyclerView.ViewHolder?, FilmRecyclerViewAdapter.FilmViewHolder>(
                FilmRecyclerViewAdapter.FilmViewHolder::class.java
            ) {
                override fun matchesSafely(viewHolder: FilmRecyclerViewAdapter.FilmViewHolder): Boolean {
                    val subjectTextView =
                        viewHolder.itemView.findViewById(R.id.name) as TextView
                    return title == subjectTextView.text
                        .toString() && subjectTextView.visibility == View.VISIBLE
                }

                override fun describeTo(description: Description) {
                    description.appendText("item with subject: $title")
                }
            }
        }
    }
}