package it.ilker.repeatcard.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.AppNavigator
import it.ilker.repeatcard.ui.util.KeyValueStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Runnable
import org.koin.android.ext.android.inject

@ExperimentalPermissionsApi
@ExperimentalUnitApi
@SuppressLint("CustomSplashScreen")
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
class SplashScreen : AppCompatActivity() {

    private val navigator: AppNavigator by inject()
    private val prefs: KeyValueStorage by inject()

    companion object {
        private const val DELAY = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_splash)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Handler().postDelayed(Runnable {
            if (prefs.getBoolean("NEW USER", true)) {
                navigator.goToOnboarding()
            } else {
                navigator.goToMain()
            }
            finish()
        }, DELAY)
    }
}
