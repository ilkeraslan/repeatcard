package it.ilker.repeatcard.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.ilker.repeatcard.R

class BaseScreen : Fragment() {

    private var index: Int = 0
    private var text: String = ""

    companion object {
        private const val ARG_ID = "index"
        private const val ARG_TITLE = "text"

        fun newInstance(index: Int, text: String) = BaseScreen().apply {
            arguments = Bundle().apply {
                putInt(ARG_ID, index)
                putString(ARG_TITLE, text)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            index = it.getInt(ARG_ID, 0)
            text = it.getString(ARG_TITLE, "")
        }
        return inflater.inflate(R.layout.screen_onboarding_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
