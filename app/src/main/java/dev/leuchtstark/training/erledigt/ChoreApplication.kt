package dev.leuchtstark.training.erledigt

import android.app.Application
import dev.leuchtstark.training.erledigt.data.AppContainer
import dev.leuchtstark.training.erledigt.data.AppDataContainer
import dev.leuchtstark.training.erledigt.worker.updateMissingChoreReminders
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChoreApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(context = this)

        kotlinx.coroutines.MainScope().launch {
            val chores = container.choreRepository.getAllChoresStream().first()
            updateMissingChoreReminders(context = applicationContext, chores = chores)
        }
    }
}