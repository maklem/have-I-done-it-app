package dev.leuchtstark.training.erledigt.data

import java.time.DayOfWeek

data class Chore(
    val id: Int = 0,
    val name: String,
    val remindAtSecondOfDay: Long,
    val remindAtDays: List<DayOfWeek>,
    val lastTimeDone: Long? = null
)
