package dev.leuchtstark.training.erledigt.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChoreDao {

    @Query("SELECT * FROM chore ORDER BY id ASC")
    fun getAllChores(): Flow<List<ChoreInformation>>

    @Query("SELECT * FROM chore " +
           "JOIN active_reminders ON chore.id == active_reminders.chore_id "+
           "ORDER BY chore.id, active_reminders.day_of_week ASC")
    fun getMappedChores(): Flow<Map<ChoreInformation,List<ActiveReminder>>>


    @Query("SELECT * from chore WHERE id = :id")
    fun getChore(id: Int): Flow<ChoreInformation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chore: ChoreInformation)

    @Update(entity = ChoreInformation::class)
    suspend fun update(chore: ChoreInfo)

    @Update(entity = ChoreInformation::class)
    suspend fun update(chore: ChoreState)

    @Delete(entity = ChoreInformation::class)
    suspend fun delete(chore: ChoreId)
}