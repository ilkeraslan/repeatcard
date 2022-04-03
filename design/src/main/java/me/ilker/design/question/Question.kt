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
import me.ilker.business.answer.Answer
import me.ilker.business.question.Question
import me.ilker.design.R

@ExperimentalMaterialApi
@Composable
fun Question(
    modifier: Modifier = Modifier,
    question: Question,
    answersEnabled: Boolean = true,
    onAnswerSelected: (Question, Answer) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
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

        Options(
            question = question,
            answersEnabled = answersEnabled,
            onAnswerSelected = onAnswerSelected
        )
    }
}

@Composable
private fun Options(
    question: Question,
    answersEnabled: Boolean = true,
    onAnswerSelected: (Question, Answer) -> Unit = { _, _ -> }
) {
    Column {
        OptionRow(
            question = question,
            first = question.options[0],
            second = question.options[1],
            answersEnabled = answersEnabled,
            onSelect = onAnswerSelected
        )

        Spacer(modifier = Modifier.height(20.dp))

        OptionRow(
            question = question,
            first = question.options[2],
            second = question.options[3],
            answersEnabled = answersEnabled,
            onSelect = onAnswerSelected
        )
    }
}

@Composable
private fun OptionRow(
    question: Question,
    first: Answer,
    second: Answer,
    answersEnabled: Boolean = true,
    onSelect: (Question, Answer) -> Unit = { _, _ -> }
) {
    val buttonColors = ButtonDefaults.textButtonColors(
        contentColor = Color.Black
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Option(
            question = question,
            buttonColors = buttonColors,
            option = first,
            answersEnabled = answersEnabled,
            onSelect = onSelect
        )

        Spacer(modifier = Modifier.width(20.dp))

        Option(
            question = question,
            buttonColors = buttonColors,
            option = second,
            answersEnabled = answersEnabled,
            onSelect = onSelect
        )
    }
}

@Composable
private fun RowScope.Option(
    question: Question,
    option: Answer,
    buttonColors: ButtonColors,
    answersEnabled: Boolean = true,
    onSelect: (Question, Answer) -> Unit = { _, _ -> }
) {
    TextButton(
        modifier = Modifier.Companion.weight(1f),
        colors = buttonColors,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 4.dp,
            color = Color.Gray
        ),
        enabled = answersEnabled,
        onClick = { onSelect(question, option) },
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = option.text,
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
            answer = Answer("Answer"),
            options = mutableListOf(
                Answer("Answer"),
                Answer("Wrong long option-1"),
                Answer("Wrong long option-2"),
                Answer("Wrong long option-3 and it's a bit long")
            )
        )
    )
}
