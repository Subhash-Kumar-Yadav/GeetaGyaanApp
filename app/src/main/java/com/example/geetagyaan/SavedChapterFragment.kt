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
import com.example.geetagyaan.databinding.FragmentSavedChapterBinding
import com.example.geetagyaan.models.ChaptersItem
import com.example.geetagyaan.viewmodels.MainViewModel
import com.example.geetagyaan.views.adapters.ChAdapter

class SavedChapterFragment : Fragment() {
    private lateinit var binding: FragmentSavedChapterBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter:ChAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedChapterBinding.inflate(layoutInflater)
        statusBarIconColorChange()
        getSaveChapterFromRoomDB()
        return binding.root
    }

    private fun getSaveChapterFromRoomDB() {
        viewModel.getSavedChapters().observe(viewLifecycleOwner) {
            val chapterList = arrayListOf<ChaptersItem>()

            for (i in it) {
                val chaptersItem = ChaptersItem(
                    i.chapter_number,
                    i.chapter_summary,
                    i.chapter_summary_hindi,
                    i.id,
                    i.name,
                    i.name_meaning,
                    i.name_translated,
                    i.name_transliterated,
                    i.slug,
                    i.verses_count
                )
                chapterList.add(chaptersItem)
            }

            if(chapterList.isEmpty()){
                binding.shimmerLayout.visibility = View.GONE
                binding.rvChapters.visibility = View.GONE

            }

            adapter = ChAdapter(
                ::onChapterItemVClicked,
                ::onFavClicked,
                true,
                ::onFavoriteFilledClicked,
                viewModel
            )
            binding.rvChapters.layoutManager = LinearLayoutManager(requireContext())
            binding.rvChapters.adapter = adapter
            adapter.differ.submitList(chapterList)

            binding.shimmerLayout.visibility = View.GONE
            binding.rvChapters.visibility = View.VISIBLE
        }
    }

   private fun onChapterItemVClicked(chaptersItem: ChaptersItem){
       val bundle = Bundle()
       bundle.putInt("chapterNumber", chaptersItem.chapter_number)
       bundle.putBoolean("showRoomData" , true)
       findNavController().navigate(R.id.action_savedChapterFragment_to_verseFragment , bundle)
   }
    private fun onFavClicked(chaptersItem: ChaptersItem){

    }
    private fun onFavoriteFilledClicked(chaptersItem: ChaptersItem){

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