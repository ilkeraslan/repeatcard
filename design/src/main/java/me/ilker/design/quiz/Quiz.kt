package me.ilker.design.quiz

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ilker.business.question.Question
import me.ilker.design.question.Question

@ExperimentalMaterialApi
@Composable
fun Quiz(
    modifier: Modifier = Modifier,
    question: Question,
    progress: Float
) {
    BottomSheetScaffold(
        modifier = modifier,
        sheetContent = {}
    ) {
        Question(
            modifier = Modifier.fillMaxWidth(),
            question = question
        )

        Spacer(modifier = Modifier.height(20.dp))

        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = progress,
            color = Color.Green
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
private fun QuizPreview() {
    Quiz(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 25.dp,
                vertical = 20.dp
            ),
        question = Question(
            id = 1,
            imageUri = "https://i.picsum.photos/id/477/1280/720.jpg?hmac=RgAXMExbzpP2c7qZKGmkABjQE_pPsGH0DBEJGrzLrik",
            answer = "Answer",
            description = "Description",
            options = mutableListOf(
                "Answer",
                "Wrong option-1",
                "Wrong option-2",
                "Wrong option-3"
            )
        ),
        progress = 0.3f
    )
}
