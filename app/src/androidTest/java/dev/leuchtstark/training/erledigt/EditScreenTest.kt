package dev.leuchtstark.training.erledigt

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.leuchtstark.training.erledigt.data.ChoreId
import dev.leuchtstark.training.erledigt.data.ChoreInformation
import dev.leuchtstark.training.erledigt.data.FakeChoreDatabase
import dev.leuchtstark.training.erledigt.data.FakeReminderRepository
import dev.leuchtstark.training.erledigt.data.OfflineChoreRepository
import dev.leuchtstark.training.erledigt.ui.chore.EditScreen
import dev.leuchtstark.training.erledigt.ui.chore.EditScreenViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun EditScreen_whenChoreIsNull_createsChore() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val choreRepository = OfflineChoreRepository(FakeChoreDatabase.getDatabase(appContext).choreDao())

        runBlocking {
            choreRepository.addChore(ChoreInformation(0, "Test A", 0))
            choreRepository.addChore(ChoreInformation(0, "Test B", 0))
        }
        val reminderRepository = FakeReminderRepository()

        val newChoreName = "newTestChoreTag"

        val viewmodel = EditScreenViewModel(choreRepository, reminderRepository)
        viewmodel.reset(null)
        viewmodel.onNameChanged(newChoreName)

        composeTestRule.setContent {
            EditScreen(viewModel = viewmodel)
        }

        composeTestRule.onAllNodesWithText(newChoreName).assertCountEquals(1)

        runBlocking {
            val totalCount = choreRepository.getAllChoresStream().first().count()
            assertEquals(2, totalCount)

            val specificCount = choreRepository.getAllChoresStream().first()
                .filter { chore -> chore.name == newChoreName }.count()
            assertEquals(0, specificCount)
        }

        val saveButton = appContext.getString(R.string.editscreen_save)
        composeTestRule.onNodeWithText(saveButton).performClick()

        runBlocking {
            val totalCount = choreRepository.getAllChoresStream().first().count()
            assertEquals(3, totalCount)

            val specificCount = choreRepository.getAllChoresStream().first()
                .filter { chore -> chore.name == newChoreName }.count()
            assertEquals(1, specificCount)
        }
    }


    @Test
    fun EditScreen_whenChoreIsSet_updatesChore() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val choreRepository = OfflineChoreRepository(FakeChoreDatabase.getDatabase(appContext).choreDao())


        val oldTestChoreName = "oldTestChoreName"
        val newTestChoreName = "newTestChoreName"
        var choreIdInDatabase: Int
        runBlocking {
            choreRepository.addChore(ChoreInformation(0, name = oldTestChoreName, 0))

             choreIdInDatabase = choreRepository.getAllChoresStream().first().first().id
        }
        val reminderRepository = FakeReminderRepository()

        val viewmodel = EditScreenViewModel(choreRepository, reminderRepository)
        viewmodel.reset(ChoreId(choreIdInDatabase))

        composeTestRule.setContent {
            EditScreen(viewModel = viewmodel)
        }
        composeTestRule.onAllNodesWithText(oldTestChoreName).assertCountEquals(1)

        composeTestRule.onNodeWithText(oldTestChoreName).performTextReplacement(newTestChoreName)

        val saveButton = appContext.getString(R.string.editscreen_save)
        composeTestRule.onNodeWithText(saveButton).performClick()

        runBlocking {
            val totalCount = choreRepository.getAllChoresStream().first().count()
            assertEquals(1, totalCount)

            val specificCount = choreRepository.getAllChoresStream().first()
                .filter { chore -> chore.name == newTestChoreName }.count()
            assertEquals(1, specificCount)
        }
    }
}