package dev.leuchtstark.training.erledigt.data

interface ReminderRepository {
    fun updateReminder(chore: ChoreInformation)
}