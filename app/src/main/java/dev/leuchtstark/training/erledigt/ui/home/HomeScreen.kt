package dev.leuchtstark.training.erledigt.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.leuchtstark.training.erledigt.R
import dev.leuchtstark.training.erledigt.data.ChoreId
import dev.leuchtstark.training.erledigt.ui.AppViewModelProvider
import dev.leuchtstark.training.erledigt.ui.theme.SimpleChoreHelperTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToEdit: (ChoreId) -> Unit = {},
    navigateToInfo: () -> Unit = {},
    navigateToNew: () -> Unit = {},
    ) {
    Scaffold(
        topBar = {
            HomeScreenTopBar(
                navigateToInfo = navigateToInfo,
            )
        },
        modifier = modifier,
    ) {
        HomeScreenContents(
            modifier = Modifier.padding(it),
            navigateToEdit = navigateToEdit,
            navigateToNew = navigateToNew,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    navigateToInfo: () -> Unit = {},
){
    CenterAlignedTopAppBar(
        title = {
            ChoreTitle()
        },
        actions = {
            TextButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = navigateToInfo,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.homescreen_about_the_app),
                )
            }
        },
    )
}

@Composable
fun ChoreTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .height(40.dp)
                .padding(end = 5.dp),
            painter = painterResource(
                id = if (isSystemInDarkTheme()) {
                    R.drawable.chores_icon_bright
                } else {
                    R.drawable.chores_icon_dark
                }
            ),
            contentDescription = null,
        )
        Text(stringResource(id = R.string.app_name))
    }
}

@Preview
@Composable
fun ChoreTitlePreview()
{
    SimpleChoreHelperTheme {
        HomeScreenTopBar()
    }
}

@Composable
fun HomeScreenContents(
    modifier: Modifier = Modifier,
    navigateToEdit: (ChoreId) -> Unit = {},
    navigateToNew: () -> Unit = {},
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val states = viewModel.homeUiState.collectAsState().value

    val neededPermissions = listOf(
        PermissionModel(
            description = stringResource(R.string.permissions_send_notifications),
            identifier = "android.permission.POST_NOTIFICATIONS"
        )
    )

    Column(
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(
                items = states.choreList,
                key = { it.id },
            ) {
                ChoreCard(
                    name = it.name,
                    reminder = it.reminder,
                    lastTimeDone = it.lastDoneAt,
                    isDue = it.isDue,
                    onDoChore = { viewModel.doChore(it.id) },
                    onEdit = {navigateToEdit(ChoreId(it.id))},
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 0.dp),
            onClick = navigateToNew
        ) {
            Text(text = stringResource(R.string.homescreen_add_new_chore))
        }
        RequestPermissionsLayer(
            permissions = neededPermissions,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}