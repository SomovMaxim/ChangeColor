package com.example.changecolor.presentation.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.changecolor.R
import com.example.changecolor.databinding.FragmentMainBinding
import com.example.changecolor.presentation.util.NavigationEvent
import com.example.changecolor.presentation.util.collectOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOnClickListeners()
        setUpNavigation()
        setUpBackground()
    }

    private fun setUpOnClickListeners() {
        binding.buttonInputColor.setOnClickListener { viewModel.onInputColorClicked() }
        binding.buttonChange.setOnClickListener { viewModel.onChangeClicked() }
        binding.buttonHelp.setOnClickListener { viewModel.onHelpClicked() }
        binding.buttonExit.setOnClickListener { viewModel.onExitClicked() }
    }

    private fun setUpNavigation() = collectOnLifecycle(viewModel.events) { event ->
        when (event) {
            NavigationEvent.InputColor -> navigateToInputColorFragment()
            NavigationEvent.Help -> navigateToHelpFragment()
            NavigationEvent.ExitApplication -> requireActivity().finish()
        }
    }

    private fun setUpBackground() {
        collectOnLifecycle(viewModel.leftSideColor) {
            binding.layoutLeft.setBackgroundColor(Color.rgb(it.red, it.green, it.blue))
        }

        collectOnLifecycle(viewModel.rightSideColor) {
            binding.layoutRight.setBackgroundColor(Color.rgb(it.red, it.green, it.blue))
        }
    }

    private fun navigateToInputColorFragment() {
        if (findNavController().currentDestination?.id == R.id.mainFragment) {
            findNavController().navigate(R.id.action_mainFragment_to_inputColorDialogFragment)
        }
    }

    private fun navigateToHelpFragment() {
        if (findNavController().currentDestination?.id == R.id.mainFragment) {
//            findNavController().navigate(R.id.action_mainFragment_to_aboutFragment)
        }
    }
}