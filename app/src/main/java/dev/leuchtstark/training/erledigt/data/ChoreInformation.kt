package dev.leuchtstark.training.erledigt.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chore")
data class ChoreInformation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "remind_at_second_of_day")
    val remindAtSecondOfDay: Long,

    @ColumnInfo(name = "last_time_done_as_utc_timestamp")
    val lastTimeDone: Long? = null
)

@Entity
data class ChoreInfo(
    val id: Int = 0,
    val name: String,

    @ColumnInfo(name = "remind_at_second_of_day")
    val remindAtSecondOfDay: Long
)

@Entity
data class ChoreState(
    val id: Int,

    @ColumnInfo(name = "last_time_done_as_utc_timestamp")
    val lastTimeDone: Long?
)

@Entity
data class ChoreId(
    val id: Int,
)
