package com.techswivel.baseproject.helper.RemoteConfigrations

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.util.*

class RemoteConfigration(private val context: Context) {
    private var isSettingDisplayed: Boolean? = null

    fun FetchRemoteValues() {
        val remoteConfigSharePrefrence = RemoteConfigSharePrefrence(context)
        val map = HashMap<String, Any>()
        map["isSettingDisplayed"] = false
        val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setDefaults(map)
        val configSettings =
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0)
                .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isSettingDisplayed =
                    java.lang.Boolean.valueOf(firebaseRemoteConfig.getString("isSettingDisplayed"))
                remoteConfigSharePrefrence.SetIsSettingFromRemote(isSettingDisplayed)
                /*     if (remoteConfigSharePrefrence.GetUrl() == null) {
                        remoteConfigSharePrefrence.SetUrl(Constants.STAGING_SERVER_URL, Constants.STAGING);
                    }*/
            } else {
                task.exception!!.toString()
            }
        }
    }

}
