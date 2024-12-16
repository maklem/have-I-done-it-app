package com.github.maklem.training.simplechorehelper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.maklem.training.simplechorehelper.data.Chore
import com.github.maklem.training.simplechorehelper.data.ChoreId
import com.github.maklem.training.simplechorehelper.data.ChoreInformation
import com.github.maklem.training.simplechorehelper.data.ChoreRepository
import com.github.maklem.training.simplechorehelper.data.approximateNextResetTime
import com.github.maklem.training.simplechorehelper.data.isDue
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

data class UiChore(
    val id: Int,
    val name: String,
    val reminder: String,
    val lastDoneAt: Long?,
    val isDue: Boolean,
)

data class HomeUiState(
    val choreList: List<UiChore> = listOf()
)

class HomeScreenViewModel(private val choreRepository: ChoreRepository) : ViewModel() {
    private fun formatReminder(secondOfDay: Long): String {
        val hour = secondOfDay / 3600
        val minute = (secondOfDay / 60) % 60

        return "%02d:%02d".format(hour, minute)
    }

    private val choreState: StateFlow<List<Chore>> =
        choreRepository.getAllChoresStream().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf())

    private val _homeUiState = MutableStateFlow( HomeUiState() )
    val homeUiState = _homeUiState.asStateFlow()

    private var refreshJob: Job? = null
    private fun evaluateNewUiState() {
        _homeUiState.update {
            HomeUiState(choreState.value.map { chore ->
                UiChore(
                    id = chore.id,
                    name = chore.name,
                    reminder = formatReminder(chore.remindAtSecondOfDay),
                    lastDoneAt = chore.lastTimeDone,
                    isDue = chore.isDue(),
                )
            })
        }
        refreshJob = viewModelScope.launch {
            val now = Instant.now().epochSecond
            val nextUpdateSeconds = choreState.value.map { chore: Chore ->
                chore.approximateNextResetTime() - now
            }.filter { seconds -> seconds > 0 }.minOrNull()
            if(nextUpdateSeconds != null) {
                delay(nextUpdateSeconds * 1000L)
                evaluateNewUiState()
            }
        }
    }

    init {
        viewModelScope.launch {
            choreState.collectLatest {
                evaluateNewUiState()
            }
        }
    }


    fun addChore(name: String) {
        viewModelScope.launch {
            choreRepository.addChore(
                ChoreInformation(
                    name = name,
                    remindAtSecondOfDay = 3600*7,
                )
            )
        }
    }

    fun doChore(id: Int){
        viewModelScope.launch {
            choreRepository.doChore(ChoreId(id))
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}