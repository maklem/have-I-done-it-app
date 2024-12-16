package com.github.maklem.training.simplechorehelper.data

import kotlinx.coroutines.flow.Flow

interface ChoreRepository {
    fun getAllChoresStream(): Flow<List<Chore>>
    fun getChoreStreamById(id: Int): Flow<Chore?>

    suspend fun addChore(chore: ChoreInformation)
    suspend fun updateChore(chore: ChoreInformation)
    suspend fun doChore(chore: ChoreId)
    suspend fun removeChore(choreId: ChoreId)

}