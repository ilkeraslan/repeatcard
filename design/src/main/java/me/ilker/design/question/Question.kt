package me.ilker.design.question

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import me.ilker.business.question.Question
import me.ilker.design.R

@ExperimentalMaterialApi
@Composable
fun Question(
    modifier: Modifier = Modifier,
    question: Question
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            painter = rememberImagePainter(
                data = question.imageUri,
                builder = {
                    placeholder(R.drawable.photography)
                    crossfade(true)
                    transformations(RoundedCornersTransformation(12f))
                }
            ),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(40.dp))

        Options(question)
    }
}

@Composable
private fun Options(question: Question) {
    Column {
        OptionRow(
            first = question.options[0],
            second = question.options[1]
        )

        Spacer(modifier = Modifier.height(20.dp))

        OptionRow(
            first = question.options[2],
            second = question.options[3]
        )
    }
}

@Composable
private fun OptionRow(
    first: String,
    second: String,
    onSelect: (String) -> Unit = {}
) {
    val buttonColors = ButtonDefaults.textButtonColors(
        contentColor = Color.Black
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Option(
            buttonColors = buttonColors,
            text = first,
            onSelect = onSelect
        )

        Spacer(modifier = Modifier.width(20.dp))

        Option(
            buttonColors = buttonColors,
            text = second,
            onSelect = onSelect
        )
    }
}

@Composable
private fun RowScope.Option(
    buttonColors: ButtonColors,
    text: String,
    onSelect: (String) -> Unit
) {
    TextButton(
        modifier = Modifier.Companion.weight(1f),
        colors = buttonColors,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 4.dp,
            color = Color.Gray
        ),
        onClick = { onSelect(text) }
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = text,
            textAlign = TextAlign.Justify,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
private fun QuestionPreview() {
    Question(
        modifier = Modifier.fillMaxSize(),
        question = Question(
            id = 1,
            imageUri = "",
            answer = "Answer",
            options = mutableListOf(
                "Answer",
                "Wrong long option-1",
                "Wrong long option-2",
                "Wrong long option-3 and it's a bit long"
            )
        )
    )
}
