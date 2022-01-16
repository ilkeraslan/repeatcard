package me.ilker.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ilker.business.flashcard.Flashcard

@ExperimentalMaterialApi
@Composable
fun Flashcard(
    modifier: Modifier = Modifier,
    flashcard: Flashcard,
    onClick: () -> Unit = {}
) {
    val indication = rememberRipple()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
        indication = indication
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            flashcard.imageUri?.let {

            } ?: Image(
                modifier = Modifier
                    .width(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .aspectRatio(16f / 9f),
                painter = painterResource(id = R.drawable.photography),
                contentDescription = stringResource(id = R.string.flashcard)
            )

            Column {
                Text(
                    text = flashcard.title,
                    fontWeight = FontWeight.Bold
                )

                flashcard.description?.let { _description ->
                    Text(text = _description)
                }
            }
        }
    }
}

/**
 * Previews
 */
@ExperimentalMaterialApi
@Preview(
    backgroundColor = 0xFFFFFF,
    showBackground = true
)
@Composable
private fun FlashcardPreview() {
    Flashcard(
        modifier = Modifier.fillMaxWidth(),
        flashcard = Flashcard(
            id = 5,
            title = "Orange",
            description = "A delicious fruit which has orange color and a circle shape"
        )
    )
}
