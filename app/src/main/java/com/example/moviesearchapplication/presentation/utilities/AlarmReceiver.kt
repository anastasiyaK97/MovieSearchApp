package com.example.moviesearchapplication.presentation.utilities

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.presentation.view.FilmDetailFragment
import com.example.moviesearchapplication.presentation.view.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == MainActivity.ALARM_ACTION) {
            val notificationManager = NotificationManagerCompat.from(context)
            val filmId = intent.getIntExtra(MainActivity.FILM_ID_EXTRA, -1)

            val notificationIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP

                putExtra(FilmDetailFragment.FILM_ID_EXTRA, filmId)
                action = "notification $filmId"
            }
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

            val builder = NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.simple_rectangle)
                .setContentTitle(context.getString(R.string.watch_later_title))
                .setContentText(context.getString(R.string.watch_later_text)
                        + " ${intent.getStringExtra(MainActivity.FILM_NAME_EXTRA)}")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            notificationManager.notify(filmId, builder.build())
        }
    }

}