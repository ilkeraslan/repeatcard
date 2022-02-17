package it.ilker.repeatcard.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.ilker.repeatcard.R
import me.ilker.business.flashcard.Flashcard
import me.ilker.design.flashcard.Flashcard

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Composable
internal fun Home(
    modifier: Modifier = Modifier,
    flashcards: List<Flashcard>,
    onAddCard: () -> Unit = {},
    onDelete: (Flashcard) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Box(modifier = modifier) {
        if (flashcards.isEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                text = stringResource(id = R.string.no_flashcard_yet),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = TextUnit(
                    value = 18.sp.value,
                    type = TextUnitType.Sp
                )
            )
        } else {
            Flashcards(
                flashcards = flashcards,
                onDelete = onDelete,
                onClick = onClick
            )
        }

        AddButton(onAddCard = onAddCard)
    }
}

@ExperimentalMaterialApi
@Composable
private fun Flashcards(
    flashcards: List<Flashcard>,
    onDelete: (Flashcard) -> Unit,
    onClick: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = flashcards,
            key = { flashcard -> flashcard.id }
        ) { flashcard ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    val dismissed = it == DismissValue.DismissedToStart
                    if (dismissed) {
                        onDelete(flashcard)
                    }
                    dismissed
                }
            )

            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 4.dp),
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = { FractionalThreshold(0.25f) },
                background = { SwipeBackground(dismissState = dismissState) },
                dismissContent = {
                    Flashcard(
                        modifier = Modifier.fillMaxWidth(),
                        flashcard = flashcard,
                        elevation = animationState(dismissState).value,
                        onClick = onClick
                    )
                }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun SwipeBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        if (dismissState.targetValue == DismissValue.DismissedToStart) {
            Color.Red
        } else Color.LightGray
    )
    val alignment = Alignment.CenterEnd
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(
                color = color,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete),
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
private fun BoxScope.AddButton(
    modifier: Modifier = Modifier,
    onAddCard: () -> Unit = {},
) {
    IconButton(
        modifier = modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 10.dp, end = 10.dp)
            .background(
                color = Color.Green,
                shape = CircleShape
            )
            .size(36.dp),
        onClick = onAddCard
    ) {
        Icon(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.ic_add_white_24dp),
            contentDescription = stringResource(id = R.string.add),
            tint = Color.Unspecified
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun animationState(dismissState: DismissState) = animateDpAsState(
    if (dismissState.dismissDirection != null) {
        4.dp
    } else {
        0.dp
    }
)

/**
 * Previews
 */
private val flashcards = listOf(
    Flashcard(
        id = 1,
        title = "Apple",
        description = "A nice and delicious red fruit",
        creationDate = null,
        lastModified = null,
        directoryId = null,
        imageUri = null
    ),
    Flashcard(
        id = 2,
        title = "Orange",
        description = null,
        creationDate = null,
        lastModified = null,
        directoryId = null,
        imageUri = null
    )
)

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(
    backgroundColor = 0xFFFFFF,
    showBackground = true
)
@Composable
private fun HomeScreenPreview() {
    Home(
        modifier = Modifier.fillMaxWidth(),
        flashcards = flashcards
    )
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(
    backgroundColor = 0xFFFFFF,
    showBackground = true
)
@Composable
private fun HomeScreenWithoutFlashcardsPreview() {
    Home(
        modifier = Modifier.fillMaxWidth(),
        flashcards = listOf()
    )
}
