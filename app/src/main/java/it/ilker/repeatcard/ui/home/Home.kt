package it.ilker.repeatcard.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.ilker.repeatcard.R
import me.ilker.business.flashcard.Flashcard
import me.ilker.design.Flashcard

@ExperimentalMaterialApi
@Composable
internal fun Home(
    modifier: Modifier = Modifier,
    flashcards: List<Flashcard>,
    onAddCard: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Box(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = flashcards,
                key = { flashcard -> flashcard.id }
            ) { flashcard ->
                Flashcard(
                    modifier = Modifier.fillMaxWidth(),
                    image = flashcard.imageUri,
                    onClick = onClick
                )
            }
        }

        IconButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = onAddCard
        ) {
            Image(
                modifier = Modifier.background(
                    color = Color.Green,
                    shape = CircleShape
                ),
                painter = painterResource(id = R.drawable.ic_add_white_24dp),
                contentDescription = "Add card"
            )
        }
    }
}

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

    @ExperimentalMaterialApi
    @Preview(
        backgroundColor = 0xFFFFFF,
        showBackground = true
    )
    @Composable
    private fun HomeScreenPreview() {
        Home(
            modifier = Modifier.fillMaxSize(),
            flashcards = flashcards
        )
    }
