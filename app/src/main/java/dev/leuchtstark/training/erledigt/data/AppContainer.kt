package dev.leuchtstark.training.erledigt.data


import android.content.Context

interface AppContainer {
    val choreRepository: ChoreRepository
    val reminderRepository: ReminderRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val choreRepository: ChoreRepository by lazy {
        OfflineChoreRepository(ChoreDatabase.getDatabase(context).choreDao())
    }
    override val reminderRepository: ReminderRepository by lazy {
        WorkManagerReminderRepository(context)
    }
}