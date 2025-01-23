package dev.leuchtstark.training.erledigt.ui.chore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.leuchtstark.training.erledigt.R
import dev.leuchtstark.training.erledigt.ui.AppViewModelProvider
import dev.leuchtstark.training.erledigt.ui.theme.SimpleChoreHelperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTimeScreen(
    modifier: Modifier = Modifier,
    onDone: ()->Unit = {},
    viewModel: EditScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState = viewModel.choreUiState

    Scaffold (
        topBar = {
            EditTimeScreenTopBar(
                onCancel = onDone,
            )
        },
        modifier = modifier,
    ){
        EditTimeScreenElements(
            uiState,
            modifier = Modifier.padding(it),
            onSave = { hour, minute ->
                viewModel.onSetTime(hour, minute)
                onDone()
            },
            onCancel = onDone,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTimeScreenTopBar(
    onCancel: () -> Unit = {},
) {
    val errorButtonStyle = ButtonColors(
        containerColor = Color.Unspecified,
        contentColor = MaterialTheme.colorScheme.error,
        disabledContentColor = Color.Unspecified,
        disabledContainerColor = Color.Unspecified
    )
    CenterAlignedTopAppBar(
        title = {
            Row(
                Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.editscreen_title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        },
        navigationIcon = {
            TextButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = onCancel,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.editscreen_cancel_editing),
                )
            }
        },
    )
}

@Preview
@Composable
fun EditTimeScreenTopBarPreview(){
    SimpleChoreHelperTheme {
        EditTimeScreenTopBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTimeScreenElements(
    uiState: EditUiState,
    modifier: Modifier = Modifier,
    onSave: (hour: Int, minute: Int) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {},
) {
    val horizontalSpace = 10.dp
    val verticalSpace = 20.dp
    Column (
        modifier = modifier.padding(horizontalSpace),
        verticalArrangement = Arrangement.Center,
    ) {
        val time = rememberTimePickerState(
            initialHour = uiState.hour.toInt(),
            initialMinute = uiState.minute.toInt(),
            is24Hour = true,
        )
        Spacer(modifier = Modifier.height(verticalSpace))
        Text(text = stringResource(R.string.editscreen_reminder_me_at))
        Spacer(modifier = Modifier.height(verticalSpace))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TimePicker(
                state = time,
                modifier = Modifier,
            )
        }
        Spacer(modifier = Modifier.height(verticalSpace))
        Row (
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1F),
                onClick = onCancel,
            ) {
                Text(text = stringResource(R.string.editscreen_cancel))
            }
            Spacer(modifier = Modifier.width(horizontalSpace))
            Button(
                modifier = Modifier.weight(1f),
                onClick = { onSave(time.hour, time.minute) },
            ) {
                Text(text = stringResource(R.string.editscreen_save))
            }
        }
        Spacer(modifier = Modifier.weight(3f))
    }
}

@Preview
@Composable
fun EditTimeScreenPreview(){
    SimpleChoreHelperTheme {
        Surface {
            EditTimeScreenElements(
                EditUiState(
                    name = "EditScreen",
                    hour = 3,
                    minute = 7,
                )
            )
        }
    }
}