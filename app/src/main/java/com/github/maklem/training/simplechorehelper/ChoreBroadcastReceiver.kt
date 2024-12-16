package com.github.maklem.training.simplechorehelper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.github.maklem.training.simplechorehelper.data.ChoreDatabase
import com.github.maklem.training.simplechorehelper.data.ChoreId
import com.github.maklem.training.simplechorehelper.data.ChoreRepository
import com.github.maklem.training.simplechorehelper.data.OfflineChoreRepository
import kotlinx.coroutines.runBlocking

const val INTENT_DO_CHORE_COMMAND = "DoChoreById"

private const val TAG = "CHORE_BROADCAST"

class ChoreBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null)
        {
            Log.d(TAG, "no context found with broadcast")
            return
        }
        if(intent == null){
            Log.d(TAG, "no intent found with broadcast")
            return
        }

        val choreId = intent.getIntExtra(INTENT_DO_CHORE_COMMAND, -1)
        val choreRepository: ChoreRepository = OfflineChoreRepository(ChoreDatabase.getDatabase(context).choreDao())
        runBlocking {
            choreRepository.doChore(ChoreId(choreId))
        }
        NotificationManagerCompat.from(context).cancel(choreId)
    }
}