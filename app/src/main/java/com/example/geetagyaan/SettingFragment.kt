package com.example.geetagyaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import com.example.geetagyaan.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private lateinit var binding:FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater)

        statusBarIconColorChange()
        binding.llSaveChapter.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_savedChapterFragment)
        }
        binding.llSaveVerse.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_savedVerseFragment)
        }
        return binding.root
    }


    private fun statusBarIconColorChange() {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (window != null) {
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.splash)
        }
        if (window != null) {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
            }
        }
    }
}