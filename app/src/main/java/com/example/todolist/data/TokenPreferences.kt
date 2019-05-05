package com.example.todolist.data

import android.content.Context
import com.facebook.AccessToken
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object TokenPreferences {

    private const val tokenKey = "facebookTokenKey"

    fun storeToken(context: Context, token: AccessToken){
        val prefs = context.getSharedPreferences("appInfo", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val tokenJson = Gson().toJson(token)

        editor.putString(tokenKey, tokenJson).apply()
    }

    fun getToken(context: Context): AccessToken? {
        val prefs = context.getSharedPreferences("appInfo", Context.MODE_PRIVATE)
        val tokenJson = prefs.getString(tokenKey, null)

        return if(tokenJson != null){
            try {
                Gson().fromJson(tokenJson, AccessToken::class.java)
            }catch (e: JsonSyntaxException){
                null
            }
        }else{
            null
        }
    }

    fun clear(context: Context) {
        val prefs = context.getSharedPreferences("appInfo", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(tokenKey).apply()
    }

}