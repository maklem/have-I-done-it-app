package dev.leuchtstark.training.erledigt.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.leuchtstark.training.erledigt.ChoreApplication
import dev.leuchtstark.training.erledigt.ui.chore.EditScreenViewModel
import dev.leuchtstark.training.erledigt.ui.home.HomeScreenViewModel

fun CreationExtras.choreApplication(): ChoreApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ChoreApplication)

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeScreenViewModel(choreApplication().container.choreRepository)
        }

        initializer {
            EditScreenViewModel(
                this.createSavedStateHandle(),
                choreRepository = choreApplication().container.choreRepository,
                reminderRepository = choreApplication().container.reminderRepository,
            )
        }
    }
}