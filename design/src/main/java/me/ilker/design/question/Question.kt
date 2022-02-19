package me.ilker.design.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
}

@Composable
private fun OptionRow(
    first: String,
    second: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = first,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        TextButton(
            modifier = Modifier.weight(1f),
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = second,
                textAlign = TextAlign.Center
            )
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
                "Wrong long option-3"
            )
        )
    )
}
