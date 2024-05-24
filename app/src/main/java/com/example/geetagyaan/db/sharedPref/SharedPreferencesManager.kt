package com.example.geetagyaan.db.sharedPref

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(val context: Context) {

    private val sharedPreferences : SharedPreferences by lazy {
        context.getSharedPreferences("saved_chapters" , Context.MODE_PRIVATE)
    }

    fun getAllSavedChaptersKeySP() : Set<String>{
        return sharedPreferences.all.keys
    }

    fun putSavedChaptersSP(key : String , value : Int){
        sharedPreferences.edit().putInt(key , value).apply()
    }

    fun deleteSavedChaptersFromSP(key : String){
        sharedPreferences.edit().remove(key).apply()
    }
}