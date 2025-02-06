package dev.leuchtstark.training.erledigt.data

import android.content.Context
import dev.leuchtstark.training.erledigt.worker.updateChoreReminder

class WorkManagerReminderRepository(private val context: Context) : ReminderRepository {
    override fun updateReminder(chore: ChoreInformation)
    {
        updateChoreReminder(context, chore)
    }
}