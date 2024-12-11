package com.thepetot.mindcraft.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.repository.UserRepository
import com.thepetot.mindcraft.di.Injection
import com.thepetot.mindcraft.ui.main.MainActivity
import com.thepetot.mindcraft.ui.settings.SettingsFragment
import com.thepetot.mindcraft.utils.logMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val userRepository: UserRepository by lazy { Injection.provideUserRepository(applicationContext) }

    override fun onNewToken(token: String) {
        logMessage(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        logMessage(TAG, "From: ${remoteMessage.from}")
        logMessage(TAG, "Message data payload: " + remoteMessage.data)
        logMessage(TAG, "Message Notification Body: ${remoteMessage.notification?.body}")

        val isEnable = runBlocking {
            userRepository
                .getPreferenceSettings(UserPreference.NOTIFICATION_ENABLED_KEY, false)
                .first() // Safely collects the first value
        }

        if (isEnable) {
            sendNotification(
                remoteMessage.data["title"],
                remoteMessage.data["message"]
            )
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            val isEnable = userRepository
//                .getPreferenceSettings(SettingsFragment.NOTIFICATION_KEY, false)
//                .first() // Safely collects the first value
//            println("WTF are u kidding me: $isEnable")
//            if (isEnable) {
//                withContext(Dispatchers.Main) {
//                    sendNotification(
//                        remoteMessage.notification?.title,
//                        remoteMessage.notification?.body
//                    )
//                }
//            }
//        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        logMessage(TAG, "From: ")
        logMessage(TAG, "Message data payload: " )
        logMessage(TAG, "Message Notification Body: }")
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            SettingsFragment.NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(applicationContext, SettingsFragment.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_mindcraft_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(SettingsFragment.NOTIFICATION_ID, notificationBuilder.build())
    }

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
    }
}