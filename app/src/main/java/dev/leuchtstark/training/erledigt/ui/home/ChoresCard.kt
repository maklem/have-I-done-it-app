package dev.leuchtstark.training.erledigt.ui.home

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.leuchtstark.training.erledigt.R
import dev.leuchtstark.training.erledigt.ui.theme.SimpleChoreHelperTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
private fun formatDate(context: Context, timestamp: Long?): String
{
    return if (timestamp == null) {
        context.getString(R.string.chorecard_never_done)
    }else{
        val localZone = ZoneOffset.systemDefault().rules.getOffset(Instant.ofEpochSecond(timestamp))
        dateFormatter.format(
            LocalDateTime.ofEpochSecond(timestamp, 0, localZone)
        )
    }
}


@Composable
fun ChoreCard(
    name: String,
    reminder: String,
    lastTimeDone: Long?,
    isDue: Boolean,
    modifier: Modifier = Modifier,
    onDoChore: () -> Unit = {},
    onEdit: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    val description_done = stringResource(R.string.chorecard_done_description).format(name)
    val description_edit = stringResource(R.string.chorecard_edit_description).format(name)

    Card (
        modifier= modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row (
                        verticalAlignment = Alignment.Top
                    ){
                        DueIcon(
                            isDue = isDue,
                            modifier =  Modifier
                                .height(30.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = name,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            )
                        )
                    }
                    Text(text = stringResource(R.string.chorecard_remind_at, reminder))
                    Text(text = stringResource(R.string.chorecard_last_time_done, formatDate(context = LocalContext.current, lastTimeDone)))
                }
                IconToggleButton(
                    checked = expanded,
                    onCheckedChange = {expanded = it},
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.chorecard_expand_description).format(name),
                        modifier = Modifier.scale(2f)
                    )
                }
            }
            if(expanded)
            {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onEdit() },
                    ) {
                        Text(
                            text = stringResource(R.string.chorecard_edit),
                            modifier = Modifier.semantics { this.contentDescription = description_edit },
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onDoChore() }
                    ) {
                        Text(
                            text = stringResource(R.string.chorecard_done),
                            modifier = Modifier.semantics { this.contentDescription = description_done },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DueIcon(
    isDue: Boolean,
    modifier: Modifier = Modifier,
){
    val iconId = if(isDue) {
        R.drawable.chores_icon_due
    } else {
        if(isSystemInDarkTheme()) {
            R.drawable.chores_icon_bright
        }else{
            R.drawable.chores_icon_dark
        }
    }
    Icon(
        modifier = modifier,
        painter = painterResource(id = iconId),
        contentDescription = null,
        tint = Color.Unspecified,
    )
}

@Preview
@Composable
fun ChoreCardPreview()
{
    SimpleChoreHelperTheme {
        Surface {
            ChoreCard(
                name = "Preview Chore",
                reminder = "daily at 7 am",
                lastTimeDone = null,
                isDue = false,
            )
        }
    }
}
