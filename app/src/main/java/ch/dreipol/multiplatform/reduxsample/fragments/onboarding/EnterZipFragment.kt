package ch.dreipol.multiplatform.reduxsample.fragments.onboarding

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import ch.dreipol.dreimultiplatform.reduxkotlin.rootDispatch
import ch.dreipol.multiplatform.reduxsample.R
import ch.dreipol.multiplatform.reduxsample.databinding.FragmentOnboardingEnterZipBinding
import ch.dreipol.multiplatform.reduxsample.shared.redux.actions.ZipUpdatedAction
import ch.dreipol.multiplatform.reduxsample.shared.ui.BaseOnboardingSubState
import ch.dreipol.multiplatform.reduxsample.shared.ui.EnterZipState
import ch.dreipol.multiplatform.reduxsample.utils.getString
import ch.dreipol.multiplatform.reduxsample.utils.setNewText

class EnterZipFragment : OnboardingFragment() {

    private var textWatcher: TextWatcher? = null

    private lateinit var enterZipBinding: FragmentOnboardingEnterZipBinding
    private lateinit var possibleZipsAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        enterZipBinding = viewBinding.fragmentOnboardingEnterZip
        possibleZipsAdapter = ArrayAdapter(requireContext(), R.layout.view_dropdown_item, R.id.text, mutableListOf())
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeTextWatcher()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterZipBinding.root.visibility = View.VISIBLE
        enterZipBinding.zip.setAdapter(possibleZipsAdapter)
        enterZipBinding.zip.setOnClickListener { enterZipBinding.zip.showDropDown() }
        enterZipBinding.zip.setDropDownBackgroundResource(R.drawable.round_dropdown_background)
    }

    override fun render(onboardingSubState: BaseOnboardingSubState) {
        if (onboardingSubState !is EnterZipState) return
        super.render(onboardingSubState)
        removeTextWatcher()
        enterZipBinding.label.text = context?.getString(onboardingSubState.enterZipLabel)
        enterZipBinding.zip.setNewText(onboardingSubState.selectedZip?.toString())
        enterZipBinding.zip.visibility = View.VISIBLE
        textWatcher = enterZipBinding.zip.addTextChangedListener(
            afterTextChanged = { text ->
                val zip = text?.toString()?.toIntOrNull()
                rootDispatch(ZipUpdatedAction(zip))
            }
        )
        possibleZipsAdapter.clear()
        possibleZipsAdapter.addAll(onboardingSubState.possibleZips.map { it.toString() })
        possibleZipsAdapter.notifyDataSetChanged()
    }

    private fun removeTextWatcher() {
        textWatcher?.let {
            enterZipBinding.zip.removeTextChangedListener(textWatcher)
            textWatcher = null
        }
    }
}