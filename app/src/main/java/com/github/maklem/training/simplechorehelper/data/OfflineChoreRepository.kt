package com.github.maklem.training.simplechorehelper.data

import kotlinx.coroutines.flow.Flow
import java.time.Instant

class OfflineChoreRepository(private val choreDao: ChoreDao) : ChoreRepository {
    override fun getAllChoresStream(): Flow<List<Chore>> {
        return choreDao.getAllChores()
    }

    override fun getChoreStreamById(id: Int): Flow<Chore?> {
        return choreDao.getChore(id)
    }

    override suspend fun addChore(chore: ChoreInformation) {
        choreDao.insert(Chore(
            id = chore.id,
            name = chore.name,
            remindAtSecondOfDay = chore.remindAtSecondOfDay,
            lastTimeDone = null,
        ))
    }

    override suspend fun updateChore(chore: ChoreInformation) {
        choreDao.update(chore)
    }

    override suspend fun doChore(chore: ChoreId) {
        val now = Instant.now().epochSecond

        choreDao.update(ChoreState(
            id = chore.id,
            lastTimeDone = now,
        ))
    }

    override suspend fun removeChore(choreId: ChoreId) {
        choreDao.delete(choreId)
    }
}