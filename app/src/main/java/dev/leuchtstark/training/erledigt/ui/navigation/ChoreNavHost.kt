package dev.leuchtstark.training.erledigt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.leuchtstark.training.erledigt.R
import dev.leuchtstark.training.erledigt.ui.AppViewModelProvider
import dev.leuchtstark.training.erledigt.ui.chore.EditScreen
import dev.leuchtstark.training.erledigt.ui.chore.EditScreenViewModel
import dev.leuchtstark.training.erledigt.ui.chore.EditTimeScreen
import dev.leuchtstark.training.erledigt.ui.home.HomeScreen
import dev.leuchtstark.training.erledigt.ui.info.InfoScreen

@Composable
fun ChoreNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    editScreenViewModel: EditScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val newChoreName = stringResource(R.string.homescreen_new_chore_name)

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = "home",
    ){
        composable(route = "home"){
            HomeScreen(
                navigateToEdit = {choreId ->
                    editScreenViewModel.reset(choreId)
                    navController.navigate("edit")
                },
                navigateToInfo = {
                    navController.navigate("info")
                },
                navigateToNew = {
                    editScreenViewModel.reset(null)
                    editScreenViewModel.onNameChanged(newChoreName)
                    navController.navigate("create")
                },
            )
        }
        composable(
            route = "edit",
        ){
            EditScreen(
                onRequestTime = {
                    navController.navigate("editTime")
                },
                onDone = { navController.navigateUp() },
                viewModel = editScreenViewModel,
            )
        }
        composable(
            route = "create",
        ){
            EditScreen(
                onRequestTime = {
                    navController.navigate("editTime")
                },
                onDone = { navController.navigateUp() },
                viewModel = editScreenViewModel,
            )
        }
        composable(
            route = "editTime",
        ){
            EditTimeScreen(
                onDone = { navController.navigateUp() },
                viewModel = editScreenViewModel,
            )
        }
        composable(route = "info"){
            InfoScreen(
                onCancel = {
                    navController.navigateUp()
                }
            )
        }
    }
}