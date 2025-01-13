package dev.leuchtstark.training.erledigt.ui.home

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import dev.leuchtstark.training.erledigt.R


data class PermissionModel(
    val description: String,
    val identifier: String,
)
@Composable
fun RequestPermissionsLayer(
    permissions: List<PermissionModel>,
    modifier: Modifier = Modifier,
){
    val permissionIsGiven = permissions.map { permission ->
        ContextCompat.checkSelfPermission(
            LocalContext.current,
            permission.identifier
        ) == PERMISSION_GRANTED
    }
    var missingPermissionCount by remember { mutableIntStateOf(permissionIsGiven.count { !it }) }

    if(missingPermissionCount > 0) {
        HorizontalDivider(Modifier.padding(top = 15.dp))
        Card(
            modifier = modifier.padding(15.dp),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ){
                Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.permissions_description))
                    permissions.forEachIndexed { index, permission ->
                        if (!permissionIsGiven[index]) {
                            MissingPermissionCard(
                                description = permission.description,
                                permission = permission.identifier,
                                onUpdate = { wasGiven -> if (wasGiven) missingPermissionCount -= 1 }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MissingPermissionCard(
    description: String,
    permission: String,
    modifier: Modifier = Modifier,
    onUpdate: (Boolean) -> Unit,
) {
    val permsRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        Log.d("PERMS", "Received result: $it ")
        onUpdate(it)
    }
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Text(text = description)
        Spacer(modifier = Modifier.weight(1F))
        Button(
            onClick = {
                permsRequest.launch(permission)
            },
            modifier = modifier,
        ) {
            Text(text = "add")
        }
    }
}
