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

    @Query("SELECT * from chore ORDER BY id ASC")
    fun getAllChores(): Flow<List<Chore>>

    @Query("SELECT * from chore WHERE id = :id")
    fun getChore(id: Int): Flow<Chore>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chore: Chore)

    @Update(entity = Chore::class)
    suspend fun update(chore: ChoreInformation)

    @Update(entity = Chore::class)
    suspend fun update(chore: ChoreState)

    @Delete(entity = Chore::class)
    suspend fun delete(chore: ChoreId)
}