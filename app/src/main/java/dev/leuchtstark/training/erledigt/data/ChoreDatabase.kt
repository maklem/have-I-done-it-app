package dev.leuchtstark.training.erledigt.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Chore::class], version = 2, exportSchema = false)
abstract class ChoreDatabase: RoomDatabase() {
    abstract fun choreDao(): ChoreDao

    companion object {
        @Volatile
        private var instance: ChoreDatabase? = null

        fun getDatabase(context: Context): ChoreDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, ChoreDatabase::class.java, "chore_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}