package me.ilker.design.snaptofit

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.ilker.design.R

data class ImageModel(
    val images: List<String>,
    val selected: Int
)

@ExperimentalCoilApi
@Composable
fun SnapToFit(
    modifier: Modifier = Modifier,
    model: ImageModel,
    onSelect: (Int) -> Unit = {},
    editMode: Boolean = false
) {
    val selectorSize = 90.dp
    val itemSize = 60.dp
    val spacingSize = 20.dp
    val maxOffset = with(LocalDensity.current) {
        ((selectorSize / 2) + (itemSize / 2) + (spacingSize / 2)).toPx() / 2f
    }

    val width = with(LocalConfiguration.current) {
        this.screenWidthDp
    }

    val horizontalPadding = width.dp.div(2).minus(selectorSize - itemSize)

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = model.selected
    )
    val coroutineScope = rememberCoroutineScope()

    val isBeingDragged = listState.interactionSource.collectIsDraggedAsState().value

    LaunchedEffect(isBeingDragged) {
        if (!isBeingDragged) {
            listState.layoutInfo.visibleItemsInfo.firstOrNull { elm ->
                elm.offset >= -maxOffset && elm.offset < maxOffset
            }?.let {
                listState.animateScrollToItem(it.index)
                onSelect(it.index)
            }
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Images(
            spacingSize = spacingSize,
            horizontalPadding = horizontalPadding,
            listState = listState,
            model = model,
            itemSize = itemSize,
            coroutineScope = coroutineScope,
            onSelect = onSelect,
            editMode = editMode
        )
    }
}

@ExperimentalCoilApi
@Composable
private fun BoxScope.Images(
    spacingSize: Dp,
    horizontalPadding: Dp,
    listState: LazyListState,
    model: ImageModel,
    itemSize: Dp,
    coroutineScope: CoroutineScope,
    onSelect: (Int) -> Unit,
    editMode: Boolean
) {
    LazyRow(
        modifier = Modifier.Companion.align(Alignment.Center),
        horizontalArrangement = Arrangement.spacedBy(spacingSize),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        state = listState
    ) {
        itemsIndexed(
            items = model.images,
            key = { index, _ -> index }
        ) { index, icon ->
            IconButton(
                modifier = Modifier.size(itemSize),
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index)
                        onSelect(index)
                    }
                }
            ) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(itemSize),
                    painter = rememberImagePainter(
                        data = icon,
                        builder = {
                            placeholder(R.drawable.photography)
                        }
                    ),
                    contentDescription = "image"
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
internal fun ScrollableIconPickerPreview() {
    SnapToFit(
        modifier = Modifier.fillMaxWidth(),
        model = ImageModel(
            images = listOf(
                "https://i.picsum.photos/id/1054/200/200.jpg?hmac=7qtHUdgOyKxMVpcUELySqbknGm7xI76LbA9CE0uag_o",
                "https://i.picsum.photos/id/43/200/200.jpg?hmac=gMoEYpdjrHoRnKoyIdtTknuqyCQDTC8exwLaKHpMv6E",
                "https://i.picsum.photos/id/907/200/200.jpg?hmac=SdeLZNONJ3CX-OB15hSXsCheWDC6yYac5N5VUJM7FIQ",
                "https://i.picsum.photos/id/453/200/200.jpg?hmac=IO3u3eOcKSOUCe8J1IlvctdxPKLTh5wFXvBT4O3BNs4",
                "https://i.picsum.photos/id/188/200/200.jpg?hmac=TipFoTVq-8WOmIswCmTNEcphuYngcdkCBi4YR7Hv6Cw",
                "https://i.picsum.photos/id/572/200/200.jpg?hmac=YFsNUCQc2Dfz_5O0HY8HmDfquz04XrdcpJ0P4Z7plRY",
                "https://i.picsum.photos/id/861/200/200.jpg?hmac=UJSK-tjn1gjzSmwHWZhjpaGahNSBDQWpMoNvg8Bxy8k"
            ),
            selected = 0
        )
    )
}
