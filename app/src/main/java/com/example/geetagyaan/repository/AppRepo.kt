package com.example.geetagyaan.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.geetagyaan.db.api.ApiUtilities
import com.example.geetagyaan.db.sharedPref.SharedPreferencesManager
import com.example.geetagyaan.models.ChaptersItem
import com.example.geetagyaan.models.VersesItem
import com.example.geetagyaan.roomdb.SavedChapters
import com.example.geetagyaan.roomdb.SavedChaptersDao
import com.example.geetagyaan.roomdb.VersesSave
import com.example.geetagyaan.roomdb.SavedVersesDao
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepo(
    private val savedChaptersDao: SavedChaptersDao,
    private val savedVerseDao: SavedVersesDao,
    private val sharedPreferencesManager: SharedPreferencesManager
) {

    fun getAllChapter(): Flow<List<ChaptersItem>> = callbackFlow {
        val callback = object : Callback<List<ChaptersItem>> {
            override fun onResponse(
                call: Call<List<ChaptersItem>>,
                response: Response<List<ChaptersItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<List<ChaptersItem>>, t: Throwable) {
                close(t)
            }

        }
        ApiUtilities.api.getAllChapter().enqueue(callback)
        awaitClose { }
    }

    fun getAllVerses(chapterNumber: Int): Flow<List<VersesItem>> = callbackFlow {
        val callback = object : Callback<List<VersesItem>> {
            override fun onResponse(
                call: Call<List<VersesItem>>,
                response: Response<List<VersesItem>>
            ) {
                if (response.isSuccessful) {
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<List<VersesItem>>, t: Throwable) {
                close(t)
            }
        }
        ApiUtilities.api.getAllVerses(chapterNumber).enqueue(callback)
        awaitClose { }
    }

    fun getParticularVerse(chapterNumber: Int, verseNumber: Int): Flow<VersesItem> = callbackFlow {
        val callback = object : Callback<VersesItem> {
            override fun onResponse(call: Call<VersesItem>, response: Response<VersesItem>) {
                if (response.isSuccessful) {
                    trySend(response.body()!!)
                    close()
                }
            }

            override fun onFailure(call: Call<VersesItem>, t: Throwable) {
                close(t)
            }
        }
        ApiUtilities.api.getParticularVerse(chapterNumber, verseNumber).enqueue(callback)
        awaitClose { }
    }


    //  RoomDatabase for CHAPTERS

    suspend fun insertChapters(savedChapters: SavedChapters) = savedChaptersDao.insertChapters(savedChapters)
    fun getSavedChapters(): LiveData<List<SavedChapters>> = savedChaptersDao.getSavedChapters()
    fun getParticularChapter(chapter_number: Int): LiveData<SavedChapters> = savedChaptersDao.getParticularChapter(chapter_number)
    suspend fun deleteChapter(id: Int) = savedChaptersDao.deleteChapter(id)


    //RoomDatabase for VERSES
    suspend fun insertEnglishVerses(versesInEnglish: VersesSave) = savedVerseDao.insertEnglishVerses(versesInEnglish)
    fun getAllEnglishVerses(): LiveData<List<VersesSave>> = savedVerseDao.getAllEnglishVerses()
    fun getParticularEnglishVerse(chapterNumber: Int, verseNumber: Int): LiveData<VersesSave> =
        savedVerseDao.getParticularEnglishVerse(chapterNumber, verseNumber)

//     suspend fun deleteParticularEnglishVerse(chapterNumber: Int, verseNumber: Int) =
//        savedVerseDao.deleteParticularEnglishVerse(chapterNumber, verseNumber)


    // Saved Chapters in Shared Preference

    fun getAllSavedChaptersKeySP(): Set<String> = sharedPreferencesManager.getAllSavedChaptersKeySP()
    fun putSavedChaptersSP(key: String, value: Int) = sharedPreferencesManager.putSavedChaptersSP(key, value)
    fun deleteSavedChaptersFromSP(key: String) = sharedPreferencesManager.deleteSavedChaptersFromSP(key)

}