package dev.leuchtstark.training.erledigt

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.leuchtstark.training.erledigt.data.ChoreInformation
import dev.leuchtstark.training.erledigt.data.FakeChoreDatabase
import dev.leuchtstark.training.erledigt.data.OfflineChoreRepository
import dev.leuchtstark.training.erledigt.ui.home.HomeScreen
import dev.leuchtstark.training.erledigt.ui.home.HomeScreenViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun HomeScreen_listsChores() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = OfflineChoreRepository(FakeChoreDatabase.getDatabase(appContext).choreDao())

        runBlocking {
            repository.addChore(ChoreInformation(0, "Test A", 0))
            repository.addChore(ChoreInformation(0, "Test B", 0))
        }

        val viewmodel = HomeScreenViewModel(repository)
        composeTestRule.setContent {
            HomeScreen(viewModel = viewmodel)
        }

        composeTestRule.onAllNodesWithText("Test").assertCountEquals(0)
        composeTestRule.onAllNodesWithText("Test A").assertCountEquals(1)
        composeTestRule.onAllNodesWithText("Test B").assertCountEquals(1)
    }

    @Test
    fun HomeScreen_canShowEmptyList() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = OfflineChoreRepository(FakeChoreDatabase.getDatabase(appContext).choreDao())

        val viewmodel = HomeScreenViewModel(repository)
        composeTestRule.setContent {
            HomeScreen(viewModel = viewmodel)
        }
        composeTestRule.onAllNodesWithText("Test").assertCountEquals(0)
    }
}