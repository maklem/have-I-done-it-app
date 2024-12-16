package com.github.maklem.training.simplechorehelper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.maklem.training.simplechorehelper.ui.chore.EditScreen
import com.github.maklem.training.simplechorehelper.ui.home.HomeScreen
import com.github.maklem.training.simplechorehelper.ui.info.InfoScreen

@Composable
fun ChoreNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = "home",
    ){
        composable(route = "home"){
            HomeScreen(
                navigateToEdit = {choreId ->
                    navController.navigate("edit/${choreId.id}")
                },
                navigateToInfo = {
                    navController.navigate("info")
                },
            )
        }
        composable(
            route = "edit/{choreId.id}",
            arguments = listOf(navArgument("choreId.id") {type = NavType.IntType})
        ){
            EditScreen(
                onDone = { navController.navigateUp() }
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