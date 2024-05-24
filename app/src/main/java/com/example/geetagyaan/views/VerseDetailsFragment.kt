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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.geetagyaan.R
import com.example.geetagyaan.databinding.FragmentVerseDetailsBinding
import com.example.geetagyaan.models.Commentary
import com.example.geetagyaan.models.Translation
import com.example.geetagyaan.models.VersesItem
import com.example.geetagyaan.roomdb.VersesSave
import com.example.geetagyaan.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class VerseDetailsFragment : Fragment() {
    private lateinit var binding: FragmentVerseDetailsBinding
    private val viewModel: MainViewModel by viewModels()
    private var chapterNumber = 0
    private var verseNumber = 0
    private var verseDetail = MutableLiveData<VersesItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVerseDetailsBinding.inflate(layoutInflater)

        statusBarIconColorChange()
        getAndSetDataOfVerseFragment()
        checkInternet()
        getDataFromSaveVerseFragment()
        onSavedVerseIconClicked()
        return binding.root
    }

    private fun getDataFromSaveVerseFragment() {
        val bundle = arguments
        val showDataFromRoom = bundle?.getBoolean("showRoomData", false)

        if (showDataFromRoom == true) {
            viewModel.getParticularEnglishVerse(chapterNumber, verseNumber)
                .observe(viewLifecycleOwner) { verse ->
                    binding.tvVerseTxt.text = verse.text
                    binding.tvTransliterationEnglish.text = verse.transliteration
                    binding.tvWordMeaning.text = verse.word_meanings

                    val englishTranslationList = arrayListOf<Translation>()
                    for (i in verse.translations) {
                        if (i.language == "english") englishTranslationList.add(i)
                    }

                    val englishTranslationListSize = englishTranslationList.size
                    if (englishTranslationList.isNotEmpty()) {
                        binding.authorName.text = englishTranslationList[0].author_name
                        binding.tvTxt.text = englishTranslationList[0].description

                        if (englishTranslationListSize == 1) binding.fabmoveLeft.visibility =
                            View.GONE

                        var i = 0
                        binding.fabmoveRight.setOnClickListener {
                            if (i < englishTranslationListSize - 1) {
                                i++
                                binding.authorName.text = englishTranslationList[i].author_name
                                binding.tvTxt.text = englishTranslationList[i].description
                                binding.fabmoveLeft.visibility = View.VISIBLE

                                if (i == englishTranslationListSize - 1) binding.fabmoveRight.visibility =
                                    View.GONE
                            }
                        }

                        binding.fabmoveLeft.setOnClickListener {
                            if (i > 0) {
                                i--
                                binding.authorName.text = englishTranslationList[i].author_name
                                binding.tvTxt.text = englishTranslationList[i].description
                                binding.fabmoveRight.visibility = View.VISIBLE

                                if (i == 0) binding.fabmoveLeft.visibility = View.GONE
                            }
                        }
                    }

                    val englishCommentaryList = arrayListOf<Commentary>()
                    for (i in verse.commentaries) {
                        if (i.language == "english") englishCommentaryList.add(i)
                    }

                    val englishCommentaryListSize = englishCommentaryList.size
                    if (englishCommentaryList.isNotEmpty()) {
                        binding.authorName.text = englishCommentaryList[0].author_name
                        binding.tvTxt.text = englishCommentaryList[0].description

                        if (englishCommentaryListSize == 1) binding.floatMoveLeft.visibility =
                            View.GONE

                        var i = 0
                        binding.floatMoveRight.setOnClickListener {
                            if (i < englishCommentaryListSize - 1) {
                                i++
                                binding.authorName.text = englishCommentaryList[i].author_name
                                binding.tvTxt.text = englishCommentaryList[i].description
                                binding.floatMoveLeft.visibility = View.VISIBLE

                                if (i == englishCommentaryListSize - 1) binding.floatMoveLeft.visibility =
                                    View.GONE
                            }
                        }

                        binding.floatMoveLeft.setOnClickListener {
                            if (i > 0) {
                                i--
                                binding.authorName.text = englishCommentaryList[i].author_name
                                binding.tvTxt.text = englishCommentaryList[i].description
                                binding.floatMoveRight.visibility = View.VISIBLE

                                if (i == 0) binding.floatMoveLeft.visibility = View.GONE
                            }
                        }
                    }
                }
        } else {
            checkInternet()
        }
    }

    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it) {
                binding.internetConnectivityWarning.visibility = View.GONE
                getVerseDetailData()
            } else {
                binding.internetConnectivityWarning.visibility = View.VISIBLE
            }
        }
    }

    private fun onSavedVerseIconClicked() {
        binding.ivFavoriteVerseFilled.setOnClickListener {
            binding.ivFavoriteVerse.visibility = View.VISIBLE
            binding.ivFavoriteVerseFilled.visibility = View.GONE
            deleteVerse()
        }
        binding.ivFavoriteVerse.setOnClickListener {
            binding.ivFavoriteVerse.visibility = View.GONE
            binding.ivFavoriteVerseFilled.visibility = View.VISIBLE
            savingVerseDetails()
        }
    }

    private fun deleteVerse() {
//        lifecycleScope.launch {
//            viewModel.deleteParticularEnglishVerse(chapterNum, verseNum)
//        }
    }

    private fun getVerseDetailData() {
        lifecycleScope.launch {
            viewModel.getParticularVerse(chapterNumber, verseNumber).collect { verse ->
                verseDetail.postValue(verse)
                binding.tvVerseTxt.text = verse.text
                binding.tvTransliterationEnglish.text = verse.transliteration
                binding.tvWordMeaning.text = verse.word_meanings

                val englishTranslationList = arrayListOf<Translation>()
                for (i in verse.translations) {
                    if (i.language == "english") englishTranslationList.add(i)
                }

                val englishTranslationListSize = englishTranslationList.size
                if (englishTranslationList.isNotEmpty()) {
                    binding.tvAuthorName.text = englishTranslationList[0].author_name
                    binding.tvTxt.text = englishTranslationList[0].description

                    if (englishTranslationListSize == 1) binding.fabmoveRight.visibility = View.GONE

                    var i = 0
                    binding.fabmoveRight.setOnClickListener {
                        if (i < englishTranslationListSize - 1) {
                            i++
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvTxt.text = englishTranslationList[i].description
                            binding.fabmoveLeft.visibility = View.VISIBLE

                            if (i == englishTranslationListSize - 1) binding.fabmoveRight.visibility = View.GONE
                        }
                    }

                    binding.fabmoveLeft.setOnClickListener {
                        if (i > 0) {
                            i--
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvTxt.text = englishTranslationList[i].description
                            binding.fabmoveRight.visibility = View.VISIBLE

                            if (i == 0) binding.fabmoveLeft.visibility = View.GONE
                        }
                    }
                }

                val englishCommentaryList = arrayListOf<Commentary>()
                for (it in verse.commentaries) {
                    if (it.language == "english") englishCommentaryList.add(it)
                }

                val englishCommentaryListSize = englishCommentaryList.size
                if (englishCommentaryList.isNotEmpty()) {
                    binding.authorName.text = englishCommentaryList[0].author_name
                    binding.tvTxt.text = englishCommentaryList[0].description

                    if (englishCommentaryListSize == 1) binding.floatMoveLeft.visibility = View.GONE

                    var i = 0
                    binding.floatMoveRight.setOnClickListener {
                        if (i < englishCommentaryListSize - 1) {
                            i++
                            binding.authorName.text = englishCommentaryList[i].author_name
                            binding.tvTxt.text = englishCommentaryList[i].description
                            binding.floatMoveLeft.visibility = View.VISIBLE

                            if (i == englishCommentaryListSize - 1) binding.floatMoveLeft.visibility =
                                View.GONE
                        }
                    }

                    binding.floatMoveLeft.setOnClickListener {
                        if (i > 0) {
                            i--
                            binding.authorName.text = englishCommentaryList[i].author_name
                            binding.tvTxt.text = englishCommentaryList[i].description
                            binding.floatMoveRight.visibility = View.VISIBLE

                            if (i == 0) binding.floatMoveLeft.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun savingVerseDetails() {
        verseDetail.observe(viewLifecycleOwner) {

            val englishCommentaryList = arrayListOf<Commentary>()
            for (i in it.commentaries) {
                if (i.language == "english") englishCommentaryList.add(i)
            }

            val englishTranslationList = arrayListOf<Translation>()
            for (i in it.translations) {
                if (i.language == "english") englishTranslationList.add(i)
            }
            val saveVerses = VersesSave(
                it.chapter_number,
                englishCommentaryList,
                it.id,
                it.slug,
                it.text,
                englishTranslationList,
                it.transliteration,
                it.verse_number,
                it.word_meanings
            )
            lifecycleScope.launch {
                viewModel.insertEnglishVerses(saveVerses)
            }
        }
    }

    private fun getAndSetDataOfVerseFragment() {
        val bundle = arguments
        chapterNumber = bundle?.getInt("chapterNum")!!
        verseNumber = bundle.getInt("verseNum")
        binding.verseNumber.text = mergeTwoString(chapterNumber , verseNumber)
    }

    private fun mergeTwoString(chapterNum: Int, verseNum: Int): CharSequence {
        return "||$chapterNum.$verseNum||"
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