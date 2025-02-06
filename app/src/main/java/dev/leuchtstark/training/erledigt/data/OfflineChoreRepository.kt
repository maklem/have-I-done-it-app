package dev.leuchtstark.training.erledigt.data

import kotlinx.coroutines.flow.Flow
import java.time.Instant

class OfflineChoreRepository(private val choreDao: ChoreDao) : ChoreRepository {
    override fun getAllChoresStream(): Flow<List<ChoreInformation>> {
        return choreDao.getAllChores()
    }

    override fun getChoreStreamById(id: Int): Flow<ChoreInformation?> {
        return choreDao.getChore(id)
    }

    override suspend fun addChore(chore: ChoreInfo) {
        choreDao.insert(
            ChoreInformation(
            id = chore.id,
            name = chore.name,
            remindAtSecondOfDay = chore.remindAtSecondOfDay,
            lastTimeDone = null,
        )
        )
    }

    override suspend fun updateChore(chore: ChoreInfo) {
        choreDao.update(chore)
    }

    override suspend fun doChore(chore: ChoreId) {
        val now = Instant.now().epochSecond

        choreDao.update(
            ChoreState(
            id = chore.id,
            lastTimeDone = now,
        )
        )
    }

    override suspend fun removeChore(choreId: ChoreId) {
        choreDao.delete(choreId)
    }
}