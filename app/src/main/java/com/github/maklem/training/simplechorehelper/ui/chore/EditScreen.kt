package com.github.maklem.training.simplechorehelper.ui.chore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.maklem.training.simplechorehelper.R
import com.github.maklem.training.simplechorehelper.ui.AppViewModelProvider
import com.github.maklem.training.simplechorehelper.ui.theme.SimpleChoreHelperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
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
            onHourIncrease = viewModel::onIncreaseHour,
            onHourDecrease = viewModel::onDecreaseHour,
            onMinuteIncrease = viewModel::onIncreaseMinute,
            onMinuteDecrease = viewModel::onDecreaseMinute,
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
    )
}

@Preview
@Composable
fun EditScreenTopBarPreview(){
    SimpleChoreHelperTheme {
        EditScreenTopBar()
    }
}

@Composable
fun EditScreenElements(
    uiState: EditUiState,
    modifier: Modifier = Modifier,
    onNameChanged: (String) -> Unit = {},
    onHourIncrease: () -> Unit = {},
    onHourDecrease: () -> Unit = {},
    onMinuteIncrease: () -> Unit = {},
    onMinuteDecrease: () -> Unit = {},
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
        TimePicker(
            hour = uiState.hour,
            minute = uiState.minute,
            onHourIncrease = onHourIncrease,
            onHourDecrease = onHourDecrease,
            onMinuteIncrease = onMinuteIncrease,
            onMinuteDecrease = onMinuteDecrease,
        )
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

@Composable
fun TimePicker(
    hour: Long,
    minute: Long,
    modifier: Modifier = Modifier,
    onHourIncrease: ()->Unit = {},
    onHourDecrease: ()->Unit = {},
    onMinuteIncrease: ()->Unit = {},
    onMinuteDecrease: ()->Unit = {},
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        NumberScrollPicker(
            value = hour.toString(),
            contentDescription = stringResource(R.string.timepicker_change_hour_format),
            onIncrease = onHourIncrease,
            onDecrease = onHourDecrease,
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.displaySmall,
            )
        NumberScrollPicker(
            value = "%02d".format(minute),
            contentDescription = stringResource(R.string.timepicker_change_minute_format),
            onIncrease = onMinuteIncrease,
            onDecrease = onMinuteDecrease,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun NumberScrollPicker(
    value: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onIncrease: ()->Unit = {},
    onDecrease: ()->Unit = {},
){
    val radius = 15.dp
    val topRoundedRectangle = RoundedCornerShape(topStart = radius, topEnd = radius)
    val bottomRoundedRectangle = RoundedCornerShape(bottomStart = radius, bottomEnd = radius)
    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            shape = topRoundedRectangle,
            contentPadding = PaddingValues(),
            onClick = onIncrease,
            modifier = Modifier.height(2 * radius)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = contentDescription?.format(stringResource(R.string.timepicker_increase)),
                )
        }
        Surface (
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
            )

        }
        Button(
            onClick = onDecrease,
            contentPadding = PaddingValues(),
            shape = bottomRoundedRectangle,
            modifier = Modifier.height(2 * radius)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = contentDescription?.format(stringResource(R.string.timepicker_decrease)),
                )
        }
    }
}

@Preview
@Composable
fun TimePickerPreview()
{
    SimpleChoreHelperTheme {
        TimePicker(hour = 4L, minute = 6L)
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