package com.repeatcard.app.di

import com.repeatcard.app.ui.directories.DirectoriesViewModel
import com.repeatcard.app.ui.directory.DirectoryViewModel
import com.repeatcard.app.ui.flashcarddetail.FlashcardDetailViewModel
import com.repeatcard.app.ui.flashcardedit.EditFlashcardViewModel
import com.repeatcard.app.ui.home.HomeViewModel
import com.repeatcard.app.ui.notifications.NotificationsViewModel
import com.repeatcard.app.ui.question.QuestionViewModel
import com.repeatcard.app.ui.quiz.QuizViewModel
import com.repeatcard.app.ui.results.ResultsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidComponents = module {
    single { androidContext().resources }
}

val appComponents = module {

}

@ExperimentalCoroutinesApi
val viewModels = module {
    viewModel { DirectoriesViewModel(get()) }
    viewModel { (id: Int) -> DirectoryViewModel(get(), id) }
    viewModel { FlashcardDetailViewModel(get()) }
    viewModel { EditFlashcardViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { NotificationsViewModel(get()) }
    viewModel { QuestionViewModel(get()) }
    viewModel { QuizViewModel(get()) }
    viewModel { ResultsViewModel(get()) }
}
