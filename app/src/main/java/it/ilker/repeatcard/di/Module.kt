package it.ilker.repeatcard.di

import it.ilker.repeatcard.ui.AppNavigator
import it.ilker.repeatcard.ui.directories.DirectoriesViewModel
import it.ilker.repeatcard.ui.directory.DirectoryViewModel
import it.ilker.repeatcard.ui.flashcarddetail.FlashcardDetailViewModel
import it.ilker.repeatcard.ui.flashcardedit.EditFlashcardViewModel
import it.ilker.repeatcard.ui.home.HomeViewModel
import it.ilker.repeatcard.ui.logs.LogsViewModel
import it.ilker.repeatcard.ui.question.QuestionViewModel
import it.ilker.repeatcard.ui.quiz.QuizViewModel
import it.ilker.repeatcard.ui.results.ResultsViewModel
import it.ilker.repeatcard.ui.util.KeyValueStorageFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidComponents = module {
    single { androidContext().resources }
    single { KeyValueStorageFactory.build(context = androidContext(), name = "repeatcard_prefs") }
}

val appComponents = module {
    single { AppNavigator(get()) }
}

val viewModels = module {
    viewModel { DirectoriesViewModel(get()) }
    viewModel { DirectoryViewModel(get()) }
    viewModel { FlashcardDetailViewModel(get()) }
    viewModel { EditFlashcardViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LogsViewModel(get()) }
    viewModel { QuestionViewModel(get()) }
    viewModel { QuizViewModel(get()) }
    viewModel { ResultsViewModel() }
}
