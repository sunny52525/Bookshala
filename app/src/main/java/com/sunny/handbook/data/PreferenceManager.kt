package com.sunny.handbook.data

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson

object PreferenceManager {

    val PREFS_NAME = "prefs"
    private var prefs: SharedPreferences? = null
    val GSON = Gson()
    private const val login = "login"
    private const val TOKEN = "token"
    private const val USER = "user"
    val STORAGE = Firebase.storage

    fun clear(){
        prefs?.edit()?.clear()?.apply()
    }
    @JvmStatic
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun SharedPreferences.putString(key: String, value: String?) {
        edit().putString(key, value).apply()
    }

    var isLogin: Boolean
        get() = prefs?.getBoolean(login, false) == true
        set(value) {
            prefs?.edit()?.putBoolean(login, value)?.apply()
        }

    var token: String?
        get() = prefs?.getString(TOKEN, "")
        set(value) {
            prefs?.putString(TOKEN, value = value)
        }

    var user: User
        get() = run {
            GSON.fromJson(prefs?.getString(USER, ""), User::class.java)
        }
        set(value) = kotlin.run {
            prefs?.putString(
                USER, GSON.toJson(value)
            )

        }

}