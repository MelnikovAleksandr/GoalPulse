package ru.asmelnikov.utils.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.calendarGold
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun MatchCalendarButton(
    isInCalendar: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        enabled = !isLoading,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.size(dimens.medium2),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(dimens.extraSmall1),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = stringResource(
                        if (isInCalendar) {
                            R.string.calendar_remove_event
                        } else {
                            R.string.calendar_add_event
                        }
                    ),
                    tint = if (isInCalendar) {
                        calendarGold
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            }
        }
    }
}
