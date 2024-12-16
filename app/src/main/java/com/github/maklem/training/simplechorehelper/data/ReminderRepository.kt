package com.github.maklem.training.simplechorehelper.data

interface ReminderRepository {
    fun updateReminder(chore: Chore)
}