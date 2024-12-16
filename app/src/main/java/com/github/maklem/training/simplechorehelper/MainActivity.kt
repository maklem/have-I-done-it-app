package com.github.maklem.training.simplechorehelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.github.maklem.training.simplechorehelper.ui.navigation.ChoreNavHost
import com.github.maklem.training.simplechorehelper.ui.theme.SimpleChoreHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ChoreApp(Modifier.fillMaxSize())
        }
    }
}

@Composable
fun ChoreApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    SimpleChoreHelperTheme {
        ChoreNavHost(
            modifier = modifier,
            navController = navController,
        )
    }
}
