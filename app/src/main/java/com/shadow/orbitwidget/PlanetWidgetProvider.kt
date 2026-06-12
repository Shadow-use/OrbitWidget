// (#) Responsibility: Періодичне оновлення віджета планети кожні 5 хвилин за допомогою системного таймера
package com.shadow.orbitwidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class PlanetWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.activity_main)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        setupRepeatingAlarm(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        setupRepeatingAlarm(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        cancelAlarm(context)
    }

    private fun setupRepeatingAlarm(context: Context) {
        val intent = Intent(context, PlanetWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Запускаємо таймер: кожні 5 хвилин (5 * 60 * 1000 мілісекунд) надсилаємо сигнал оновлення
        val interval = 5 * 60 * 1000L
        alarmManager.setRepeating(
            AlarmManager.RTC,
            System.currentTimeMillis() + interval,
            interval,
            pendingIntent
        )
    }

    private fun cancelAlarm(context: Context) {
        val intent = Intent(context, PlanetWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
