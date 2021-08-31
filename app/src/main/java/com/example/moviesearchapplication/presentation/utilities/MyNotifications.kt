package com.example.moviesearchapplication.presentation.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.moviesearchapplication.R

object MyNotifications {

    fun createWatchLaterChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.resources.getString(R.string.film_channel_name)
            val description = context.resources.getString(R.string.film_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(context.getString(R.string.default_notification_channel_id), name, importance)
            channel.description = description
            channel.enableVibration(true)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager!!.createNotificationChannel(channel)
        }
    }

}