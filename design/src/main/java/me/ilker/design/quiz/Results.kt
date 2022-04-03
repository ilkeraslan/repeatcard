package me.ilker.design.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.ilker.business.quiz.QuizResult

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
            Text(text = result.questions.size.toString())

            Text(text = result.correctAnswers.size.toString())

            Text(text = result.wrongAnswers.size.toString())
        }
    }
}
