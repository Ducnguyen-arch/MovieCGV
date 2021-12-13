package com.ducnn17.movieCGV.receiver

import com.ducnn17.movieCGV.data.movies.entity.Movies
import android.app.AlarmManager
import com.ducnn17.movieCGV.receiver.ReminderBroadCast
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

//object AlarmManage {
//    fun createAlarm(context: Context, movieDTO: Movies?) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, ReminderBroadCast::class.java)
//        intent.putExtra("e7631ffcb8e766993e5ec0c1f4245f93", movieDTO)
//        val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
//        //        alarmManager.set(AlarmManager.RTC_WAKEUP , Long.parseLong(movieDTO.timeReminder) , pendingIntent);
//    }
//}