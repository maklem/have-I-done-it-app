package com.github.maklem.training.simplechorehelper.data

import android.content.Context
import com.github.maklem.training.simplechorehelper.worker.updateChoreReminder

class WorkManagerReminderRepository(private val context: Context) : ReminderRepository {
    override fun updateReminder(chore: Chore)
    {
        updateChoreReminder(context, chore)
    }
}