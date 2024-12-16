package com.github.maklem.training.simplechorehelper.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "chore")
data class Chore(
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
data class ChoreInformation(
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
