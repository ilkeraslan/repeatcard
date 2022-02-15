package it.ilker.repeatcard.ui

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import it.ilker.repeatcard.ui.directory.DirectoryScreen
import it.ilker.repeatcard.ui.main.MainScreen
import it.ilker.repeatcard.ui.onboarding.OnboardingScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

interface Navigator {
    fun goToDirectory(id: Int)
    fun goToMain()
    fun goToOnboarding()
}

@ExperimentalPermissionsApi
@ExperimentalUnitApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
class AppNavigator(private val context: Context) : Navigator {

    override fun goToDirectory(id: Int) {
        DirectoryScreen.openDirectoryScreen(context, id)
    }

    override fun goToMain() {
        MainScreen.openScreen(context)
    }

    override fun goToOnboarding() {
        OnboardingScreen.openScreen(context)
    }
}
