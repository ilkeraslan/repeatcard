package it.ilker.repeatcard.ui.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.ilker.design.Flashcard

@ExperimentalMaterialApi
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Flashcard()
}

/**
 * Previews
 */
@ExperimentalMaterialApi
@Preview(
    backgroundColor = 0xFFFFFF
)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}
