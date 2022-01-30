package me.ilker.design.question

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                .width(256.dp)
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

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            question.options.forEach {
                Text(text = it)
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
private fun QuestionPreview() {
    Question(
        modifier = Modifier.fillMaxSize(),
        question = Question(
            id = 1,
            imageUri = "",
            answer = "Answer",
            options = mutableListOf(
                "Answer",
                "Wrong option-1",
                "Wrong option-2",
                "Wrong option-3"
            )
        )
    )
}
