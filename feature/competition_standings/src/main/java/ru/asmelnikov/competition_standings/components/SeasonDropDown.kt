package ru.asmelnikov.competition_standings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun SeasonDropDown(
    modifier: Modifier = Modifier,
    onItemChanged: (String) -> Unit,
    items: List<String>,
    selectedItem: String,
    isMenuOpen: Boolean = false
) {

    var expanded by remember { mutableStateOf(isMenuOpen) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(modifier = modifier) {
        TextField(
            value = selectedItem,
            onValueChange = onItemChanged,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            singleLine = true,
            placeholder = {
                Text(
                    text = selectedItem
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary
            ),
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(icon, "expand", tint = MaterialTheme.colorScheme.primary)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        DropdownMenu(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .border(
                    MaterialTheme.dimens.borderSize,
                    MaterialTheme.colorScheme.onSurface,
                    MaterialTheme.shapes.medium
                ),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onItemChanged(label)
                    },
                    text = {
                        Text(
                            text = label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }
        }
    }
}