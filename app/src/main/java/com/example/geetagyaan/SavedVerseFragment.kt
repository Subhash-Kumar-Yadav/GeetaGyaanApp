package com.example.geetagyaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geetagyaan.databinding.FragmentSavedVerseBinding
import com.example.geetagyaan.roomdb.VersesSave
import com.example.geetagyaan.viewmodels.MainViewModel
import com.example.geetagyaan.views.adapters.SavedVersesAdapter


class SavedVerseFragment : Fragment() {
    private lateinit var binding:FragmentSavedVerseBinding
    private val viewModel:MainViewModel by viewModels()
    private lateinit var savedVersesAdapter: SavedVersesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedVerseBinding.inflate(layoutInflater)
        statusBarIconColorChange()
        getVersesFromRoomDB()
        return binding.root
    }

    private fun getVersesFromRoomDB() {
        viewModel.getAllEnglishVerses().observe(viewLifecycleOwner){

            savedVersesAdapter = SavedVersesAdapter(::onVerseItemClicked)
            binding.rvVerse.layoutManager = LinearLayoutManager(requireContext())
            binding.rvVerse.adapter = savedVersesAdapter
            savedVersesAdapter.differ.submitList(it)
            binding.shimmerLayout.visibility = View.GONE

        }
    }
    private fun onVerseItemClicked(verse:VersesSave){
        val bundle = Bundle()
        bundle.putBoolean("showRoomData" , true)
        bundle.putInt("chapterNum" , verse.chapter_number)
        bundle.putInt("verseNum" , verse.verse_number)
        findNavController().navigate(R.id.action_savedVerseFragment_to_verseDetailsFragment , bundle)
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