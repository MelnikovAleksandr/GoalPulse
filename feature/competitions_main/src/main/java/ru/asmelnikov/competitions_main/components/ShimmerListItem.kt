package ru.asmelnikov.competitions_main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kyant.backdrop.backdrops.LayerBackdrop
import ru.asmelnikov.utils.composables.shimmerEffect
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun ShimmerListItem(
    backdrop: LayerBackdrop,
    modifier: Modifier = Modifier,
) {
    LiquidCompetitionCard(
        backdrop = backdrop,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.medium1),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(dimens.champLogoDefaultSize)
                    .aspectRatio(1f)
                    .shimmerEffect(),
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(dimens.medium1)
                            .shimmerEffect(),
                    )

                    Spacer(modifier = Modifier.width(dimens.small1))

                    Box(
                        modifier = Modifier
                            .size(dimens.medium1)
                            .clip(CircleShape)
                            .shimmerEffect(),
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(dimens.medium1)
                        .shimmerEffect(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(dimens.medium1)
                        .shimmerEffect(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(dimens.medium1)
                        .shimmerEffect(),
                )
            }
        }
    }
}
