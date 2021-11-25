package it.ilker.repeatcard.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ilker.business.flashcard.Flashcard
import me.ilker.design.Flashcard

@ExperimentalMaterialApi
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    flashcards: List<Flashcard>,
    onClick: () -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = flashcards,
            key = { flashcard -> flashcard.id }
        ) { flashcard ->
            Flashcard(
                modifier = modifier,
                image = flashcard.imageUri,
                onClick = onClick
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
    HomeScreen(flashcards = flashcards)
}
