package com.example.geetagyaan.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.geetagyaan.db.sharedPref.SharedPreferencesManager
import com.example.geetagyaan.models.ChaptersItem
import com.example.geetagyaan.models.VersesItem
import com.example.geetagyaan.repository.AppRepo
import com.example.geetagyaan.roomdb.AppRoomDB
import com.example.geetagyaan.roomdb.SavedChapters
import com.example.geetagyaan.roomdb.VersesSave
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val savedChaptersDao = AppRoomDB.getDatabaseInstance(application)?.savedChaptersDao()
    private val savedVersesDao = AppRoomDB.getDatabaseInstance(application)?.savedVerseDao()
    private val sharedPreferencesManager = SharedPreferencesManager(application)
    private val appRepo = AppRepo(savedChaptersDao!!, savedVersesDao!!, sharedPreferencesManager)

    fun getAllChapter(): Flow<List<ChaptersItem>> = appRepo.getAllChapter()
    fun getAllVerses(chapterNumber: Int): Flow<List<VersesItem>> = appRepo.getAllVerses(chapterNumber)
    fun getParticularVerse(chapterNumber: Int, verseNumber: Int): Flow<VersesItem> = appRepo.getParticularVerse(chapterNumber, verseNumber)

    //Saved Data in RoomDB
    suspend fun insertChapters(savedChapters: SavedChapters) = appRepo.insertChapters(savedChapters)
    fun getSavedChapters(): LiveData<List<SavedChapters>> = appRepo.getSavedChapters()
    fun getParticularChapter(chapter_number: Int): LiveData<SavedChapters> = appRepo.getParticularChapter(chapter_number)
    suspend fun deleteChapter(id: Int) = appRepo.deleteChapter(id)


    //Saved Verses in RoomDB

    suspend fun insertEnglishVerses(versesInEnglish: VersesSave) =
        appRepo.insertEnglishVerses(versesInEnglish)

    fun getAllEnglishVerses(): LiveData<List<VersesSave>> = appRepo.getAllEnglishVerses()
    fun getParticularEnglishVerse(chapterNumber: Int, verseNumber: Int): LiveData<VersesSave> =
        appRepo.getParticularEnglishVerse(chapterNumber, verseNumber)

//     suspend fun deleteParticularEnglishVerse(chapterNumber: Int, verseNumber: Int) =
//        appRepo.deleteParticularEnglishVerse(chapterNumber, verseNumber)


    // Saved Chapters in Shared Preference

    fun getAllSavedChaptersKeySP(): Set<String> = appRepo.getAllSavedChaptersKeySP()
    fun putSavedChaptersSP(key: String, value: Int) = appRepo.putSavedChaptersSP(key, value)
    fun deleteSavedChaptersFromSP(key: String) = appRepo.deleteSavedChaptersFromSP(key)
}