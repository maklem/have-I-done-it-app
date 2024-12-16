package com.github.maklem.training.simplechorehelper

import android.app.Application
import com.github.maklem.training.simplechorehelper.data.AppContainer
import com.github.maklem.training.simplechorehelper.data.AppDataContainer
import com.github.maklem.training.simplechorehelper.worker.updateMissingChoreReminders
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