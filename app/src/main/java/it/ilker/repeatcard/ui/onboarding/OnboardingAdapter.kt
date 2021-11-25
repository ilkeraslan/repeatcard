package it.ilker.repeatcard.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class OnboardingAdapter(fm: FragmentManager, private val strings: Array<String>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private const val SCREEN_SIZE = 5
    }

    override fun getItem(position: Int): Fragment {
        return BaseScreen.newInstance(position, strings[position])
    }

    override fun getCount(): Int {
        return SCREEN_SIZE
    }
}
