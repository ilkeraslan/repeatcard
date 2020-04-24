package it.ilker.repeatcard.ui.onboarding

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jem.liquidswipe.LiquidSwipeViewPager
import com.rd.PageIndicatorView
import it.ilker.repeatcard.R

class OnboardingScreen : AppCompatActivity() {

    private lateinit var adapter: OnboardingAdapter
    private lateinit var next: Button
    private lateinit var pageIndicator: PageIndicatorView
    private lateinit var previous: Button
    private lateinit var viewPager: LiquidSwipeViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_onboarding)
        setupViews()
    }

    private fun setupViews() {
        next = findViewById(R.id.onboardingNext)
        pageIndicator = findViewById(R.id.pageIndicatorView)
        previous = findViewById(R.id.onboardingPrevious)
        viewPager = findViewById(R.id.onboardingViewpager)
    }
}
