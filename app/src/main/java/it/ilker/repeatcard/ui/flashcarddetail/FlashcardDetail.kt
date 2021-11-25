package it.ilker.repeatcard.ui.flashcarddetail

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FlashcardDetail(
    modifier: Modifier = Modifier
) {
    Column() {
        Text(text = "Foo")
    }
}

@Preview(
    backgroundColor = 0xFFFFFF
)
@Composable
private fun FlashcardDetailPreview() {
    FlashcardDetail()
}
