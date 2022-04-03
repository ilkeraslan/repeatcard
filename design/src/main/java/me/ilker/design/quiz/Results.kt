package me.ilker.design.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ilker.business.quiz.QuizResult
import me.ilker.design.R

@Composable
fun Results(
    modifier: Modifier = Modifier,
    result: QuizResult
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(
                    id = R.string.results,
                    result.questions.size.toString()
                )
            )

            Text(
                text = stringResource(
                    id = R.string.correct_answers,
                    result.correctAnswers.size.toString()
                )
            )

            Text(
                text = stringResource(
                    id = R.string.wrong_answers,
                    result.wrongAnswers.size.toString()
                )
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
private fun ResultsPreview() {
    Results(
        result = QuizResult(
            id = "5",
            questions = listOf(),
            correctAnswers = listOf(),
            wrongAnswers = listOf()
        )
    )
}
