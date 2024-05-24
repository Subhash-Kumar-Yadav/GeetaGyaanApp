package com.example.geetagyaan.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedChaptersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(savedChapters: SavedChapters)

    @Query("SELECT * FROM SavedChapters")
    fun getSavedChapters(): LiveData<List<SavedChapters>>

    @Query("DELETE FROM SavedChapters WHERE id = :id")
    suspend fun deleteChapter(id: Int)

    @Query("SELECT * FROM savedchapters WHERE chapter_number = :chapter_number")
    fun getParticularChapter(chapter_number: Int): LiveData<SavedChapters>

}

@Dao
interface SavedVersesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnglishVerses(versesInEnglish: VersesSave)

    @Query("SELECT * FROM SavedVerses")
    fun getAllEnglishVerses(): LiveData<List<VersesSave>>

    @Query("SELECT * FROM SavedVerses WHERE chapter_number = :chapterNumber AND verse_number = :verseNumber")
    fun getParticularEnglishVerse(chapterNumber: Int, verseNumber: Int): LiveData<VersesSave>

//    @Query("SELECT * FROM SavedVerses WHERE chapter_number = :chapterNumber AND verse_number = :verseNumber")
//     suspend fun deleteParticularEnglishVerse(chapterNumber: Int, verseNumber: Int)
}