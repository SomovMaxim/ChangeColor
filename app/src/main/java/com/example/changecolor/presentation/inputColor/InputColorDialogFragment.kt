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
import dagger.hilt.android.AndroidEntryPoint

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
        setUpRedColorEditText()
        setUpGreenColorEditText()
        setUpBlueColorEditText()
        setUpSaveButton()
        setUpEvents()
    }

    private fun setUpCheckBoxes() {
        binding.checkboxLeft.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onLeftCheckBoxChecked(isChecked)
        }

        binding.checkboxRight.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onRightCheckBoxChecked(isChecked)
        }
    }

    private fun setUpRedColorEditText() = binding.layoutRed.apply {
        editText?.doAfterTextChanged { viewModel.onRedColorChanged(it.toString()) }
        this@InputColorDialogFragment.collectOnLifecycle(viewModel.isRedColorValid) {
            error = if (!it) requireContext().getString(R.string.error) else ""
        }
    }

    private fun setUpGreenColorEditText() = binding.layoutGreen.apply {
        editText?.doAfterTextChanged { viewModel.onGreenColorChanged(it.toString()) }
        this@InputColorDialogFragment.collectOnLifecycle(viewModel.isGreenColorValid) {
            error = if (!it) requireContext().getString(R.string.error) else ""
        }
    }

    private fun setUpBlueColorEditText() = binding.layoutBlue.apply {
        editText?.doAfterTextChanged { viewModel.onBlueColorChanged(it.toString()) }
        this@InputColorDialogFragment.collectOnLifecycle(viewModel.isBlueColorValid) {
            error = if (!it) requireContext().getString(R.string.error) else ""
        }
    }

    private fun setUpSaveButton() = binding.buttonSave.apply {
        setOnClickListener { viewModel.onSaveClicked() }
        this@InputColorDialogFragment.collectOnLifecycle(viewModel.isSaveButtonEnabled) {
            isEnabled = it
        }
    }

    private fun setUpEvents() = collectOnLifecycle(viewModel.dismiss) { if (it) dismiss() }
}