package it.ilker.repeatcard.di

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import it.ilker.repeatcard.ui.AppNavigator
import it.ilker.repeatcard.ui.addcard.AddCardViewModel
import it.ilker.repeatcard.ui.directories.DirectoriesViewModel
import it.ilker.repeatcard.ui.directory.DirectoryViewModel
import it.ilker.repeatcard.ui.flashcarddetail.FlashcardDetailViewModel
import it.ilker.repeatcard.ui.flashcardedit.EditFlashcardViewModel
import it.ilker.repeatcard.ui.home.HomeViewModel
import it.ilker.repeatcard.ui.question.QuestionViewModel
import it.ilker.repeatcard.ui.quiz.QuizViewModel
import it.ilker.repeatcard.ui.results.ResultsViewModel
import it.ilker.repeatcard.ui.util.KeyValueStorageFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidComponents = module {
    single { androidContext().resources }
    single {
        KeyValueStorageFactory.build(
            context = androidContext(),
            name = "repeatcard_prefs"
        )
    }
}

@ExperimentalUnitApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
val appComponents = module {
    single { AppNavigator(get()) }
}

@ExperimentalCoroutinesApi
val viewModels = module {
    viewModel { DirectoriesViewModel(get()) }
    viewModel { DirectoryViewModel(get()) }
    viewModel { FlashcardDetailViewModel(get()) }
    viewModel { AddCardViewModel(get()) }
    viewModel { EditFlashcardViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { QuestionViewModel() }
    viewModel { QuizViewModel(get()) }
    viewModel { ResultsViewModel() }
}
