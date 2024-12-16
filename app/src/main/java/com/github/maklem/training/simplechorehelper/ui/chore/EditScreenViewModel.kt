package com.github.maklem.training.simplechorehelper.ui.chore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.maklem.training.simplechorehelper.data.Chore
import com.github.maklem.training.simplechorehelper.data.ChoreId
import com.github.maklem.training.simplechorehelper.data.ChoreInformation
import com.github.maklem.training.simplechorehelper.data.ChoreRepository
import com.github.maklem.training.simplechorehelper.data.ReminderRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class EditUiState(
    val name: String = "Chore",
    val hour: Long = 7,
    val minute: Long = 0,
)

fun Chore.toChoreUiState(): EditUiState =
    EditUiState(
        name = name,
        hour = remindAtSecondOfDay / 3600,
        minute = (remindAtSecondOfDay / 60) % 60,
    )


class EditScreenViewModel (
    savedStateHandle: SavedStateHandle,
    private val choreRepository: ChoreRepository,
    private val reminderRepository: ReminderRepository,
) : ViewModel() {
    var choreUiState by mutableStateOf( EditUiState() )
        private set

    private val choreId: Int = checkNotNull(savedStateHandle["choreId.id"])

    init {
        viewModelScope.launch {
            choreUiState = choreRepository.getChoreStreamById(choreId)
                .filterNotNull()
                .first()
                .toChoreUiState()
        }
    }

    fun onNameChanged(newName:String){
        choreUiState = choreUiState.copy( name = newName )
    }

    fun onIncreaseHour() {
        choreUiState = choreUiState.copy( hour = (choreUiState.hour + 1) % 24 )
    }
    fun onDecreaseHour() {
        val newHour = if(choreUiState.hour > 0) {
            choreUiState.hour - 1
        }else{ 23 }
        choreUiState = choreUiState.copy( hour = newHour )
    }
    fun onIncreaseMinute() {
        choreUiState = choreUiState.copy( minute = (choreUiState.minute + 1) % 60 )
    }
    fun onDecreaseMinute() {
        val newMinute = if(choreUiState.minute > 0) {
            choreUiState.minute - 1
        }else{ 59 }
        choreUiState = choreUiState.copy( minute = newMinute )
    }

    fun saveChore(){
        viewModelScope.launch {
            runBlocking {
                choreRepository.updateChore(
                    ChoreInformation(
                        id = choreId,
                        name = choreUiState.name,
                        remindAtSecondOfDay = choreUiState.hour * 3600 + choreUiState.minute * 60,
                    )
                )
            }
            val updatedChore = choreRepository.getChoreStreamById(choreId).first()
            if(updatedChore != null) {
                reminderRepository.updateReminder(updatedChore)
            }
        }
    }

    fun deleteChore(){
        viewModelScope.launch {
            choreRepository.removeChore(ChoreId(choreId))
        }
    }
}

