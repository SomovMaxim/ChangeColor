package com.example.changecolor.presentation.inputColor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.changecolor.R
import com.example.changecolor.databinding.FragmentInputColorDialogBinding
import com.example.changecolor.presentation.util.collectOnLifecycle
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class InputColorDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentInputColorDialogBinding
    private val viewModel: InputColorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputColorDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCheckBoxes()
        setUpInputLayouts()
        setUpSaveButton()
        setUpEvents()
    }

    private fun setUpCheckBoxes() {
        binding.checkboxLeft.setOnCheckedChangeListener { _, _ -> viewModel.onLeftCheckBoxChecked() }
        binding.checkboxRight.setOnCheckedChangeListener { _, _ -> viewModel.onRightCheckBoxChecked() }
    }

    private fun setUpInputLayouts() {
        binding.layoutRed.setUp(viewModel::onRedColorChanged, viewModel.isRedColorValid)
        binding.layoutGreen.setUp(viewModel::onGreenColorChanged, viewModel.isGreenColorValid)
        binding.layoutBlue.setUp(viewModel::onBlueColorChanged, viewModel.isBlueColorValid)
    }

    private fun setUpSaveButton() = binding.buttonSave.apply {
        setOnClickListener { viewModel.onSaveClicked() }
        collectOnLifecycle(viewModel.isSaveButtonEnabled) { isEnabled = it }
    }

    private fun setUpEvents() = collectOnLifecycle(viewModel.dismiss) { if (it) dismiss() }

    private fun TextInputLayout.setUp(onChanged: (String) -> Unit, flow: Flow<Boolean>) {
        editText?.doAfterTextChanged { onChanged(it.toString()) }
        collectOnLifecycle(flow) {
            error = if (!it) requireContext().getString(R.string.error) else ""
        }
    }
}