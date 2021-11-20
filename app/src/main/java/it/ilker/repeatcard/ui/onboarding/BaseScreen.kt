package it.ilker.repeatcard.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jem.liquidswipe.layout.LiquidSwipeConstraintLayout
import it.ilker.repeatcard.R

class BaseScreen : Fragment() {

    private var index: Int = 0
    private var text: String = ""
    private lateinit var screenTextView: TextView
    private lateinit var screenLayout: LiquidSwipeConstraintLayout

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
        return inflater.inflate(R.layout.screen_onboarding_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screenTextView = view.findViewById(R.id.screenOnboardingBaseText)
        screenLayout = view.findViewById(R.id.screenOnboardingBaseLayout)

        when (index) {
            SCREEN_1 -> setView(text, resources.getColor(R.color.colorGreen))
            SCREEN_2 -> setView(text, resources.getColor(R.color.colorBlue))
            SCREEN_3 -> setView(text, resources.getColor(R.color.colorPurple))
            SCREEN_4 -> setView(text, resources.getColor(R.color.colorRed))
            SCREEN_5 -> setView(text, resources.getColor(R.color.colorPrimaryDark))
        }
    }

    private fun setView(text: String, color: Int) {
        screenTextView.text = text
        screenLayout.setBackgroundColor(color)
    }
}
