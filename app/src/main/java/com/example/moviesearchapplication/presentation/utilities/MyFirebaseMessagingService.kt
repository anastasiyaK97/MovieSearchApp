package com.example.moviesearchapplication.presentation.utilities

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.moviesearchapplication.R
import com.example.moviesearchapplication.presentation.view.FilmDetailFragment
import com.example.moviesearchapplication.presentation.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val filmId : Int = remoteMessage.data.get(FILM_ID_KEY)?.toInt() ?: 0
            val message = remoteMessage.notification?.body ?: getString(R.string.fcm_message)
            sendNotification(filmId, message)
        }
    }

    private fun sendNotification(filmId: Int, message: String) {

        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

            putExtra(FilmDetailFragment.FILM_ID_EXTRA, filmId)
            action = "notification $filmId"
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
            .setSmallIcon(R.drawable.simple_rectangle)
            .setContentTitle(getString(R.string.fcm_title))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }

    companion object {
        const val TAG = "FCM_TAG"
        const val FILM_ID_KEY = "filmId"
    }
}