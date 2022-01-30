package it.ilker.repeatcard.ui.quiz

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import it.ilker.repeatcard.navigation.NavFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.ilker.design.Error
import me.ilker.design.Loading
import me.ilker.design.quiz.Quiz
import me.ilker.design.quiz.Results
import org.koin.androidx.compose.viewModel

@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
object QuizFactory : NavFactory {
    @Composable
    override fun Create(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
        val vm by viewModel<QuizViewModel>()
        val state = vm.state.collectAsState()

        when (val quizState = state.value) {
            is QuizState.Error -> Error(
                modifier = Modifier.fillMaxSize()
            )
            QuizState.Loading -> Loading(
                modifier = Modifier.fillMaxSize()
            )
            is QuizState.Results -> Results()
            is QuizState.Success -> Quiz(
                modifier = Modifier.fillMaxSize(),
                questions = quizState.questions
            )
        }
    }
}
