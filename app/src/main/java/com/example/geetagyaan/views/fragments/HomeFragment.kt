package com.example.geetagyaan.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geetagyaan.R
import com.example.geetagyaan.databinding.FragmentHomeBinding
import com.example.geetagyaan.models.ChaptersItem
import com.example.geetagyaan.roomdb.SavedChapters
import com.example.geetagyaan.viewmodels.MainViewModel
import com.example.geetagyaan.views.NetworkManager
import com.example.geetagyaan.views.adapters.ChAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ChAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        statusBarIconColorChange()
        checkInternetConnectivity()
        getAndSetRandomVerseOfTheDayFromApi()

        binding.settingIcon.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }
        return binding.root
    }

    private fun getAndSetRandomVerseOfTheDayFromApi() {
        val chapterNumber = (1..18).random()
        val verseNumber = (1..20).random()

        lifecycleScope.launch {
            viewModel.getParticularVerse(chapterNumber, verseNumber).collect{
                for(i in it.translations){
                    if(i.language == "english"){
                        binding.tvVerseOfDay.text = i.description
                        break
                    }
                }
            }
        }
    }

    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it) {
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.rvChapters.visibility = View.VISIBLE
                binding.internetNotice.visibility = View.GONE
                binding.internetNoticeImage.visibility = View.GONE
                getAllChapter()
            } else {
                binding.shimmerLayout.visibility = View.GONE
                binding.rvChapters.visibility = View.GONE
                binding.internetNotice.visibility = View.VISIBLE
                binding.internetNoticeImage.visibility = View.VISIBLE
            }
        }
    }

    private fun clickedToSaveFavouriteChapterItem(chaptersItem: ChaptersItem) {
        lifecycleScope.launch {
            viewModel.putSavedChaptersSP(chaptersItem.chapter_number.toString() , chaptersItem.id)
            viewModel.getAllVerses(chaptersItem.chapter_number).collect {

                val verseList = arrayListOf<String>()
                for (currVerse in it) {
                    for (verses in currVerse.translations) {
                        if (verses.language == "english") {
                            verseList.add(verses.description)
                            break
                        }
                    }
                }
                val savedChapters = SavedChapters(
                    chapter_number = chaptersItem.chapter_number,
                    chapter_summary = chaptersItem.chapter_summary,
                    chapter_summary_hindi = chaptersItem.chapter_summary_hindi,
                    id = chaptersItem.id,
                    name = chaptersItem.name,
                    name_meaning = chaptersItem.name_meaning,
                    name_translated = chaptersItem.name_translated,
                    name_transliterated = chaptersItem.name_transliterated,
                    slug = chaptersItem.slug,
                    verses_count = chaptersItem.verses_count,
                    verses = verseList
                )
                viewModel.insertChapters(savedChapters)
            }
        }
    }
    private fun onFavoriteFilledClicked(chaptersItem: ChaptersItem){
        viewModel.deleteSavedChaptersFromSP(chaptersItem.chapter_number.toString())
        lifecycleScope.launch {
            viewModel.deleteChapter(chaptersItem.id)
        }
    }

    private fun getAllChapter() {
        lifecycleScope.launch {
            viewModel.getAllChapter().collect {
                adapter = ChAdapter(
                    ::onChapterIVClicked,
                    ::clickedToSaveFavouriteChapterItem,
                    true , ::onFavoriteFilledClicked , viewModel
                )
                binding.rvChapters.layoutManager = LinearLayoutManager(requireContext())
                binding.rvChapters.adapter = adapter
                adapter.differ.submitList(it)
                binding.shimmerLayout.visibility = View.GONE
            }
        }
    }

    private fun onChapterIVClicked(chaptersItem: ChaptersItem) {
        val bundle = Bundle()
        bundle.putInt("chapterNumber", chaptersItem.chapter_number)
        bundle.putString("chapterTitle", chaptersItem.name_translated)
        bundle.putString("chapterDesc", chaptersItem.chapter_summary)
        bundle.putInt("verseCount", chaptersItem.verses_count)
        findNavController().navigate(R.id.action_homeFragment_to_verseFragment, bundle)
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