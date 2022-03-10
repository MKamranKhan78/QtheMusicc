package com.techswivel.qthemusic.ui.activities.mainActivity

import android.app.Activity
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.CommonKeys

class MainActivityViewModel : BaseViewModel() {

    fun setDataInSharedPrefrence(activity: Activity) {
        PrefUtils.setString(activity, CommonKeys.KEY_USER_NAME, "kamran khan")
        PrefUtils.setString(activity, CommonKeys.KEY_USER_EMAIL, "techswivel@gmail.com")
        PrefUtils.setString(
            activity,
            CommonKeys.KEY_USER_AVATAR,
            "https://ca.slack-edge.com/TH6CHMP7Z-U01RL9JUJG1-0a9af450bd4f-512"
        )
        PrefUtils.setBoolean(activity, CommonKeys.KEY_USER_ENABLE_NOTIFICATION, true)
        PrefUtils.setBoolean(activity, CommonKeys.KEY_ARTIST_UPDATE, false)
        PrefUtils.setString(activity, CommonKeys.KEY_SUBSRIPTION, null)
        PrefUtils.setString(activity, CommonKeys.KEY_USER_PHONE, "003030223")
        PrefUtils.setInt(activity, CommonKeys.KEY_USER_DOB, 123445)
        PrefUtils.setInt(activity, CommonKeys.KEY_USER_PLAN_ID, 2)
        PrefUtils.setInt(activity, CommonKeys.KEY_USER_PLAN_PRIZE, 234)
        PrefUtils.setInt(activity, CommonKeys.KEY_USER_ZIP_CODE, 2343)
        PrefUtils.setString(activity, CommonKeys.KEY_USER_GENDER, "MALE")
        PrefUtils.setString(activity, CommonKeys.KEY_USER_CITY, "lahore")
        PrefUtils.setString(activity, CommonKeys.KEY_USER_STATE, "punjab")
        PrefUtils.setString(activity, CommonKeys.KEY_USER_COUNTRY, "pakistan")
        PrefUtils.setString(activity, CommonKeys.KEY_USER_PLAN_TITLE, "standard plan")
        PrefUtils.setString(activity, CommonKeys.KEY_USER_PLAN_DURATION, "monthly")
        PrefUtils.setString(activity, CommonKeys.KEY_USER_ADRESS, "st 23 muhalla daras road lahore")
    }
}