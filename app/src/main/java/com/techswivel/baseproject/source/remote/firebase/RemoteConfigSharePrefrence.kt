package com.techswivel.baseproject.helper.RemoteConfigrations

import android.content.Context
import android.content.SharedPreferences

class RemoteConfigSharePrefrence(private val context: Context) {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun setIsSettingFromRemote(isSettingDisplayed: Boolean?) {
        editor = context.getSharedPreferences(SHARED_PREFRENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putBoolean(IS_KEY_SETTING_DISPLAYED, isSettingDisplayed!!)
        editor.apply()
    }

    fun setUrl(url: String, urlKey: String) {
        editor = context.getSharedPreferences(SHARED_PREFRENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(KEY_URL, url)
        editor.putString(KEY_URL_TYPE, urlKey)
        editor.apply()
    }

    fun getUrl(): String? {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFRENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_URL, "")
    }

    fun getUrlKey(): String? {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFRENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_URL_TYPE, "")
    }

    fun isSettingDisplayed(): Boolean {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFRENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_KEY_SETTING_DISPLAYED, false)
    }

    fun setBuildFlavour(buildFlavour: String) {
        editor = context.getSharedPreferences(SHARED_PREFRENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(KEY_BUILD_FLAVOUR, buildFlavour)
        editor.apply()
    }

    fun getBuildFlavour(): String? {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFRENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_BUILD_FLAVOUR, null)
    }

    companion object {
        var SHARED_PREFRENCE_NAME = "URLPref"
        var KEY_URL = "key_url"
        var IS_KEY_SETTING_DISPLAYED = "IS_SETTING_DISPLAYED"
        var KEY_URL_TYPE = "URL_TYPE"
        var KEY_BUILD_FLAVOUR = "KEY_BUILD_FLAVOUR"
    }
}
