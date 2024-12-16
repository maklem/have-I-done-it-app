package com.github.maklem.training.simplechorehelper.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.maklem.training.simplechorehelper.ChoreApplication
import com.github.maklem.training.simplechorehelper.ui.chore.EditScreenViewModel
import com.github.maklem.training.simplechorehelper.ui.home.HomeScreenViewModel

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