package dev.leuchtstark.training.erledigt.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dev.leuchtstark.training.erledigt.ChoreBroadcastReceiver
import dev.leuchtstark.training.erledigt.INTENT_DO_CHORE_COMMAND
import dev.leuchtstark.training.erledigt.R
import dev.leuchtstark.training.erledigt.data.ChoreInformation
import dev.leuchtstark.training.erledigt.data.ChoreDatabase
import dev.leuchtstark.training.erledigt.data.OfflineChoreRepository
import dev.leuchtstark.training.erledigt.data.isDue
import dev.leuchtstark.training.erledigt.data.nextTimeDue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.util.concurrent.TimeUnit


private const val TAG = "NOTIFY_WORK"

fun updateMissingChoreReminders(context: Context, chores: List<ChoreInformation>)
{
    for(chore in chores)
    {
        updateChoreReminder(context, chore, ExistingWorkPolicy.KEEP)
    }
}

fun updateChoreReminder(context: Context, chore: ChoreInformation, policy: ExistingWorkPolicy = ExistingWorkPolicy.REPLACE){
    val now = Instant.now()
    val delay = chore.nextTimeDue() - now.epochSecond

    val data = Data.Builder()
    data.putInt("chore_id", chore.id)

    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay, TimeUnit.SECONDS)
        .setInputData(data.build())
        .build()
    WorkManager
        .getInstance(context)
        .enqueueUniqueWork(
            "ReminderForChore${chore.id}",
            policy,
            workRequest)

    Log.d(TAG, "Scheduled reminder for ${chore.id} in $delay")
}

class NotificationWorker(private val context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters) {
    // ... ExistingPeriodicWorkPolicy.UPDATE
    override suspend fun doWork(): Result
    {
        val choreId = inputData.getInt("chore_id", -1)

        val database = ChoreDatabase.getDatabase(context)
        val repository = OfflineChoreRepository(database.choreDao())
        val chore = runBlocking {
            repository.getChoreStreamById(choreId).first()
        }
        if(chore == null) {
            Log.d(TAG, "Could not find a chore for id = $choreId")
            return Result.success()
        }
        if(chore.isDue()) {
            sendNotification(
                title = chore.name,
                choreId = chore.id,
                context = context,
            )
        }
        updateChoreReminder(context, chore)
        return Result.success()
    }
}


private const val REMINDER_NOTIFICATION_CHANNEL_ID = "ReminderChannel"

private fun sendNotification(
    title: String,
    choreId: Int,
    context: Context,
)
{

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(
            REMINDER_NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.notification_channel_description),
            importance
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)

        val doChoreIntent = Intent(context, ChoreBroadcastReceiver::class.java).apply {
            putExtra(INTENT_DO_CHORE_COMMAND, choreId)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, choreId, doChoreIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, REMINDER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.chores_icon_bright)
            .setContentTitle(title)
            .setAutoCancel(true)
            .addAction(0, context.getString(R.string.notification_action_done), pendingIntent)
        NotificationManagerCompat.from(context).notify(choreId ,builder.build())
    }
}
