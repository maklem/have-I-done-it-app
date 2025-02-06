package dev.leuchtstark.training.erledigt.data

import kotlinx.coroutines.flow.Flow

interface ChoreRepository {
    fun getAllChoresStream(): Flow<List<ChoreInformation>>
    fun getChoreStreamById(id: Int): Flow<ChoreInformation?>

    suspend fun addChore(chore: ChoreInfo)
    suspend fun updateChore(chore: ChoreInfo)
    suspend fun doChore(chore: ChoreId)
    suspend fun removeChore(choreId: ChoreId)

}