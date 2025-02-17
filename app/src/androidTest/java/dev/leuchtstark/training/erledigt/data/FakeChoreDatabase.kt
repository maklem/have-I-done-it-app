package dev.leuchtstark.training.erledigt.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Chore::class], version = 2, exportSchema = false)
abstract class FakeChoreDatabase: RoomDatabase() {
    abstract fun choreDao(): ChoreDao

    companion object {
        fun getDatabase(context: Context): ChoreDatabase {
            return Room.inMemoryDatabaseBuilder(context, ChoreDatabase::class.java)
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}