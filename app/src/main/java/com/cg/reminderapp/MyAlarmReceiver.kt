package com.cg.reminderapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.*

class MyAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Toast.makeText(context, "Reminder ALERT!!!", Toast.LENGTH_SHORT).show()
        val nManager = ContextCompat.getSystemService(context!!, NotificationManager::class.java) as NotificationManager
        lateinit var builder: Notification.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("test", "Reminder", NotificationManager.IMPORTANCE_HIGH)

            nManager.createNotificationChannel(channel)
            builder = Notification.Builder(context, "test")
        }
        else
        {
            builder = Notification.Builder(context)
        }
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)

        val title = intent.getStringExtra("title")
       // val date = intent.getStringExtra("date")
       // val time = intent.getStringExtra("time")
        val description = intent.getStringExtra("description")
        val id=intent.getIntExtra("reqCode",3)
        builder.setContentTitle("Time's Up for your $title")
        builder.setContentText(description)
        val i = Intent(context, ViewRem::class.java)
        val pi = PendingIntent.getActivity(context, id, i, 0)
        val myNotification = builder.build()
        builder.setContentIntent(pi)
        nManager.notify(id, myNotification)




    }
}