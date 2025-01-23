package dev.leuchtstark.training.erledigt.ui.chore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.leuchtstark.training.erledigt.data.Chore
import dev.leuchtstark.training.erledigt.data.ChoreId
import dev.leuchtstark.training.erledigt.data.ChoreInformation
import dev.leuchtstark.training.erledigt.data.ChoreRepository
import dev.leuchtstark.training.erledigt.data.ReminderRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class EditUiState(
    val name: String = "Chore",
    val hour: Long = 7,
    val minute: Long = 0,
    val existsInDatabase: Boolean = false,
)

fun Chore.toChoreUiState(): EditUiState =
    EditUiState(
        name = name,
        hour = remindAtSecondOfDay / 3600,
        minute = (remindAtSecondOfDay / 60) % 60,
        existsInDatabase = true,
    )


class EditScreenViewModel (
    savedStateHandle: SavedStateHandle,
    private val choreRepository: ChoreRepository,
    private val reminderRepository: ReminderRepository,
) : ViewModel() {
    var choreUiState by mutableStateOf( EditUiState() )
        private set

    private var choreId: Int? = null

    fun reset(choreId: ChoreId?) {
        if(choreId == null) {
            this.choreId = null
            choreUiState = EditUiState()
        }else{
            this.choreId = choreId.id
            viewModelScope.launch {
                choreUiState = choreRepository.getChoreStreamById(choreId.id)
                    .filterNotNull()
                    .first()
                    .toChoreUiState()
            }
        }
    }

    fun onNameChanged(newName:String){
        choreUiState = choreUiState.copy( name = newName )
    }

    fun onSetTime(hour: Int, minute: Int) {
        choreUiState = choreUiState.copy(
            hour = hour.toLong(),
            minute = minute.toLong(),
        )
    }

    fun saveChore(){
        if(this.choreId == null)
        {
            viewModelScope.launch {
                runBlocking {
                    choreRepository.addChore(
                        ChoreInformation(
                            name = choreUiState.name,
                            remindAtSecondOfDay = choreUiState.hour * 3600 + choreUiState.minute * 60,
                        )
                    )
                }
                val updatedChore = choreRepository.getAllChoresStream().first().lastOrNull()
                if (updatedChore != null) {
                    reminderRepository.updateReminder(updatedChore)
                }
            }
        }else{
            val choreId = this.choreId!!
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
                if (updatedChore != null) {
                    reminderRepository.updateReminder(updatedChore)
                }
            }
        }
    }

    fun deleteChore(){
        if(this.choreId != null) {
            val choreId = this.choreId!!
            viewModelScope.launch {
                choreRepository.removeChore(ChoreId(choreId))
            }
        }
    }
}

