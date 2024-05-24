package com.example.geetagyaan.views

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
import com.example.geetagyaan.databinding.FragmentVerseBinding

import com.example.geetagyaan.viewmodels.MainViewModel
import com.example.geetagyaan.views.adapters.VerseAdapter
import kotlinx.coroutines.launch


class VerseFragment : Fragment() {

    private lateinit var binding: FragmentVerseBinding
    private val viewModel : MainViewModel by viewModels()
    private lateinit var verseAdapter: VerseAdapter
    private var chapterNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVerseBinding.inflate(layoutInflater)

        statusBarIconColorChange()
        getAndSetChapterDetails()
        onReadMoreClicked()
        getData()
        return binding.root
    }

    private fun getData() {
        val bundle = arguments
        val showDataFromRoom = bundle?.getBoolean("showRoomData" , false)

        if(showDataFromRoom == true){
            getDataFromRoom()
        }
        else{
            internetConnectivityCheck()
        }
    }

    private fun getDataFromRoom() {
        viewModel.getParticularChapter(chapterNumber).observe(viewLifecycleOwner){
            binding.chapterNumber.text = "Chapter ${it.chapter_number}"
            binding.translatorName.text = it.name_translated
            binding.tvDescription.text = it.chapter_summary
            binding.totalViews.text = it.verses_count.toString()

            showChapterListInAdapter(it.verses , false)
        }
    }

    private fun internetConnectivityCheck() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if(it){
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.rvVerse.visibility = View.VISIBLE
                binding.internetConnectivityWarning.visibility = View.GONE
                getAllVerseData()
            }
            else{
                binding.shimmerLayout.visibility = View.GONE
                binding.rvVerse.visibility = View.GONE
                binding.internetConnectivityWarning.visibility = View.VISIBLE
            }
        }
    }

    private fun onReadMoreClicked() {
        var isExpanded = false
        binding.readMore.setOnClickListener {
            if(!isExpanded){
                binding.tvDescription.maxLines = 50
                isExpanded = true
            }
            else{
                binding.tvDescription.maxLines = 4
                isExpanded = false
            }
        }
    }

    private fun getAndSetChapterDetails() {
        val bundle = arguments
        chapterNumber = bundle?.getInt("chapterNumber")!!
        binding.chapterNumber.text = "Chapter ${bundle.getInt("chapterNumber")}"
        binding.translatorName.text = bundle.getString("chapterTitle")
        binding.tvDescription.text = bundle.getString("chapterDesc")
        binding.totalViews.text = bundle.getInt("verseCount").toString()

    }

    private fun getAllVerseData() {
        lifecycleScope.launch {
            viewModel.getAllVerses(chapterNumber).collect{

                val verseList = arrayListOf<String>()
                for(currVerse in it){
                    for(verses in currVerse.translations){
                        if(verses.language == "english"){
                            verseList.add(verses.description)
                            break
                        }
                    }
                }
                showChapterListInAdapter(verseList , true)
            }
        }
    }

    private fun showChapterListInAdapter(verseList: List<String> , onClick:Boolean) {
        verseAdapter = VerseAdapter(::onVerseItemClicked , true)
        binding.rvVerse.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVerse.adapter = verseAdapter
        verseAdapter.differ.submitList(verseList)
        binding.shimmerLayout.visibility = View.GONE
    }

    private fun onVerseItemClicked(verse:String , verseNumber:Int){
        val bundle = Bundle()
        bundle.putInt("chapterNum" , chapterNumber)
        bundle.putInt("verseNum" , verseNumber)
        findNavController().navigate(R.id.action_verseFragment_to_verseDetailsFragment , bundle)
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

