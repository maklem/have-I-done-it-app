package com.github.maklem.training.simplechorehelper.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.maklem.training.simplechorehelper.R
import com.github.maklem.training.simplechorehelper.ui.theme.SimpleChoreHelperTheme


@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
){
    Scaffold(
        modifier = modifier,
        topBar = { InfoScreenTopBar(
            onCancel = onCancel,
        )}
    ) { paddingValues ->
        InfoScreenContents(
            modifier = Modifier.padding(paddingValues)
        )
    }

}



@Composable
fun InfoScreenContents(
    modifier: Modifier = Modifier,
){
    Column (
        modifier = modifier.padding(5.dp),
    ){
        Text(
            text = stringResource(R.string.infoscreen_about),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(R.string.infoscreen_about_text),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(R.string.infoscreen_license),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(R.string.infoscreen_license_text),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreenTopBar(
    onCancel: () -> Unit = {},
){
    CenterAlignedTopAppBar(
        title = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = stringResource(R.string.infoscreen_title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
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
                    contentDescription = stringResource(R.string.infoscreen_back_to_main_screen),
                )
            }
        },
    )
}

@Preview
@Composable
fun EditScreenTopBarPreview(){
    SimpleChoreHelperTheme {
        InfoScreenTopBar()
    }
}

@Preview
@Composable
fun InfoScreenPreview(){
    SimpleChoreHelperTheme {
        InfoScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}