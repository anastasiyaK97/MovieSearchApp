package com.example.moviesearchapplication.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapplication.R

class FilmItemAnimator(private val context : Context) : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        dispatchAddFinished(holder)
        return false
    }

    override fun animateMove(holder: RecyclerView.ViewHolder?,
                             fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        return false
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder?, newHolder: RecyclerView.ViewHolder?,
                               fromLeft: Int, fromTop: Int, toLeft: Int, toTop: Int): Boolean {
        return false
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        holder!!.itemView.clearAnimation()
        val set = AnimatorInflater.loadAnimator(
            context,
            R.animator.slide_to_right
        )
        set.interpolator = AccelerateInterpolator()
        set.setTarget(holder.itemView)
        set.addListener(object : AnimatorListenerAdapter() {
            @Override
            override fun onAnimationEnd(animation : Animator) {
                dispatchRemoveFinished(holder)
            }})
        set.start()
        return false
    }

    override fun isRunning(): Boolean {
        return false
    }

    override fun runPendingAnimations() {
    }

    override fun endAnimation(holder: RecyclerView.ViewHolder) {
    }

    override fun endAnimations() {
    }

    override fun canReuseUpdatedViewHolder(
        viewHolder: RecyclerView.ViewHolder, payloads: MutableList<Any>
    ) = true

    override fun canReuseUpdatedViewHolder(
        viewHolder: RecyclerView.ViewHolder) = true

}