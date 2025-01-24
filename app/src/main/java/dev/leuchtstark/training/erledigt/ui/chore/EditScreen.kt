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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun EditScreen(
    modifier: Modifier = Modifier,
    onRequestTime: () -> Unit = {},
    onDone: ()->Unit = {},
    viewModel: EditScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.choreUiState
    val onDelete = {
        viewModel.deleteChore()
        onDone()
    }

    Scaffold (
        topBar = {
            EditScreenTopBar(
                uiState = uiState,
                onCancel = onDone,
                onDelete = onDelete,
            )
        },
        modifier = modifier,
    ){
        EditScreenElements(
            uiState,
            modifier = Modifier.padding(it),
            onNameChanged = viewModel::onNameChanged,
            onRequestTime = onRequestTime,
            onSave = {
                viewModel.saveChore()
                onDone()
            },
            onCancel = onDone,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenTopBar(
    uiState: EditUiState,
    onCancel: () -> Unit = {},
    onDelete: () -> Unit = {},
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
        actions = {
            if(uiState.existsInDatabase) {
                TextButton(
                    modifier = Modifier.fillMaxHeight(),
                    onClick = onDelete,
                    colors = errorButtonStyle,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.editscreen_delete_chore)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun EditScreenTopBarPreview(){
    SimpleChoreHelperTheme {
        EditScreenTopBar(EditUiState())
    }
}

@Composable
fun EditScreenElements(
    uiState: EditUiState,
    modifier: Modifier = Modifier,
    onNameChanged: (String) -> Unit = {},
    onRequestTime: () -> Unit = {},
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val horizontalSpace = 10.dp
    val verticalSpace = 20.dp
    Column (
        modifier = modifier.padding(horizontalSpace),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.editscreen_remind_me_of))
        OutlinedTextField(
            value = uiState.name,
            onValueChange = { onNameChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(verticalSpace))
        Text(text = stringResource(R.string.editscreen_reminder_me_at))
        OutlinedButton(
            modifier=Modifier.fillMaxWidth(),
            onClick = onRequestTime,
        ) {
            Text(
                text = "%2d:%02d".format(uiState.hour, uiState.minute)
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
                onClick = onSave,
            ) {
                Text(text = stringResource(R.string.editscreen_save))
            }
        }
        Spacer(modifier = Modifier.weight(3f))
    }
}

@Preview
@Composable
fun DetailsScreenPreview(){
    SimpleChoreHelperTheme {
        Surface {
            EditScreenElements(
                EditUiState(
                    name = "EditScreen",
                    hour = 3,
                    minute = 7,
                )
            )
        }
    }
}