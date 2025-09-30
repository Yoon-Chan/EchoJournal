package com.chan.echojournal.app.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import com.chan.echojournal.R
import com.chan.echojournal.app.MainActivity
import com.chan.echojournal.app.navigation.ACTION_CREATE_ECHO

class RecordEchoWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = RecordEchoWidget()
}

class RecordEchoWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val recordNewEcho = context.getString(R.string.record_new_echo)
        provideContent {
            GlanceTheme {
                Column(
                    modifier = GlanceModifier
                        .clickable {
                            val intent = Intent(context, MainActivity::class.java).also {
                                it.data = "https://echojournal.com/echos/true".toUri()
                                it.action = ACTION_CREATE_ECHO
                            }
                            val pendingIntent = TaskStackBuilder
                                .create(context)
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

                            pendingIntent?.send()
                        }
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.widget),
                        contentDescription = recordNewEcho
                    )
                }
            }
        }
    }
}