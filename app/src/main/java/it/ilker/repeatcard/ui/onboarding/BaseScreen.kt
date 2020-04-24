package it.ilker.repeatcard.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import it.ilker.repeatcard.R
import timber.log.Timber

class BaseScreen : Fragment() {

    private var index: Int = 0
    private var text: String = ""
    private lateinit var screenText: TextView

    companion object {
        private const val ARG_ID = "index"
        private const val ARG_TITLE = "text"
        private const val SCREEN_1 = 0
        private const val SCREEN_2 = 1
        private const val SCREEN_3 = 2
        private const val SCREEN_4 = 3
        private const val SCREEN_5 = 4

        fun newInstance(index: Int, text: String) = BaseScreen().apply {
            arguments = Bundle().apply {
                putInt(ARG_ID, index)
                putString(ARG_TITLE, text)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getInt(ARG_ID, 0)
            text = it.getString(ARG_TITLE, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_onboarding_base, container, false).apply {
            when (index) {
                SCREEN_1 -> Timber.d("SCREEN-1")
                SCREEN_2 -> Timber.d("SCREEN-2")
                SCREEN_3 -> Timber.d("SCREEN-3")
                SCREEN_4 -> Timber.d("SCREEN-4")
                SCREEN_5 -> Timber.d("SCREEN-5")
            }

            findViewById<TextView>(R.id.screenOnboardingBaseText).text = text
        }
    }
}
