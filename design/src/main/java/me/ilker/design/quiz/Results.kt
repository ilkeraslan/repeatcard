package me.ilker.design.quiz

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import me.ilker.business.quiz.QuizResult
import me.ilker.design.R

@ExperimentalUnitApi
@Composable
fun Results(
    modifier: Modifier = Modifier,
    result: QuizResult
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Title()

            QuestionsCount(result)

            CorrectAnswersCount(result)

            WrongAnswersCount(result)
        }
    }
}

@ExperimentalUnitApi
@Composable
private fun QuestionsCount(result: QuizResult) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(
            id = R.string.questions,
            result.questions.size.toString()
        ),
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = TextUnit(
                value = 18f,
                type = TextUnitType.Sp
            )
        )
    )
}

@ExperimentalUnitApi
@Composable
private fun CorrectAnswersCount(result: QuizResult) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(
            id = R.string.correct_answers,
            result.correctAnswers.size.toString()
        ),
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = TextUnit(
                value = 18f,
                type = TextUnitType.Sp
            )
        )
    )
}

@ExperimentalUnitApi
@Composable
private fun WrongAnswersCount(result: QuizResult) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(
            id = R.string.wrong_answers,
            result.wrongAnswers.size.toString()
        ),
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = TextUnit(
                value = 18f,
                type = TextUnitType.Sp
            )
        )
    )
}

@ExperimentalUnitApi
@Composable
private fun Title() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.Green,
                shape = RoundedCornerShape(12.dp)
            ),
        text = stringResource(id = R.string.results),
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(
                value = 24f,
                type = TextUnitType.Sp
            )
        )
    )
}

/**
 * Previews
 */
@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(
    backgroundColor = 0xFFFFFF,
    showBackground = true
)
@Composable
private fun ResultsPreview() {
    Results(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        result = QuizResult(
            id = "5",
            questions = listOf(),
            correctAnswers = listOf(),
            wrongAnswers = listOf()
        )
    )
}
