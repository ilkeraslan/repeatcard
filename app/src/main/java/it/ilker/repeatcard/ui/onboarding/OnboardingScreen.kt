package it.ilker.repeatcard.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.jem.liquidswipe.LiquidSwipeViewPager
import com.rd.PageIndicatorView
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.AppNavigator
import it.ilker.repeatcard.ui.util.KeyValueStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

@ExperimentalUnitApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
class OnboardingScreen : AppCompatActivity() {

    private val navigator: AppNavigator by inject()
    private val prefs: KeyValueStorage by inject()

    private lateinit var adapter: OnboardingAdapter
    private lateinit var next: MaterialButton
    private lateinit var pageIndicator: PageIndicatorView
    private lateinit var previous: MaterialButton
    private lateinit var viewPager: LiquidSwipeViewPager

    companion object {
        fun openScreen(context: Context) {
            val intent = Intent(context, OnboardingScreen::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_onboarding)
        setupViews()
        setupViewPager()
    }

    private fun setupViewPager() {
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                if (viewPager.currentItem == 0) {
                    previous.visibility = INVISIBLE
                    next.visibility = VISIBLE
                } else {
                    previous.visibility = VISIBLE
                    next.visibility = VISIBLE
                }
            }
        })

        viewPager.setCurrentItem(0, true)
    }

    private fun setupViews() {
        next = findViewById(R.id.onboardingNext)
        pageIndicator = findViewById(R.id.pageIndicatorView)
        previous = findViewById(R.id.onboardingPrevious)
        viewPager = findViewById(R.id.onboardingViewpager)
        adapter = OnboardingAdapter(supportFragmentManager, resources.getStringArray(R.array.onboardingTexts))
        viewPager.adapter = adapter

        next.setOnClickListener {
            if (viewPager.currentItem == adapter.count - 1) {
                prefs.putBoolean("NEW USER", false)
                navigator.goToMain()
                finish()
            } else {
                viewPager.currentItem += 1
            }
        }

        previous.setOnClickListener { viewPager.currentItem -= 1 }
    }
}
