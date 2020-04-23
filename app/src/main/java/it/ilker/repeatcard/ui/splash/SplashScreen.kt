package it.ilker.repeatcard.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import androidx.appcompat.app.AppCompatActivity
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.AppNavigator
import kotlinx.coroutines.Runnable
import org.koin.android.ext.android.inject

class SplashScreen : AppCompatActivity() {

    private val navigator: AppNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.screen_splash)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Handler().postDelayed(Runnable {
            navigator.goToMain()
            finish()
        }, 2000L)
    }
}
