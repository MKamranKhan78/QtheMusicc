package com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_landing_screen

import android.app.Activity
import com.techswivel.qthemusic.models.Address
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.Notification
import com.techswivel.qthemusic.models.Subscription
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.CommonKeys

class ProfileLandingViewModel : BaseViewModel() {
    var userName: String? = null
    var userEmail: String? = null
    var userAvatar: String? = null
    var userSubsription: String? = null
    var userPhone: String? = null
    var userGender: String? = null
    var userCity: String? = null
    var userState: String? = null
    var userCountry: String? = null
    var userPlanTitle: String? = null
    var userDuration: String? = null
    var userAddress: String? = null

    var enableNotification: Boolean? = null
    var enableArtistUpdate: Boolean? = null

    var userdob: Int? = null
    var userZipcode: Int? = null

    var userPlanId: Int? = null
    var userPlanPrize: Int? = null

    var authModel: AuthModel? = null

    fun getPrefrencesData(activity: Activity) {
        userName = PrefUtils.getString(activity, CommonKeys.KEY_USER_NAME)
        userEmail = PrefUtils.getString(activity, CommonKeys.KEY_USER_EMAIL)
        userAvatar = PrefUtils.getString(activity, CommonKeys.KEY_USER_AVATAR)
        userGender = PrefUtils.getString(activity, CommonKeys.KEY_USER_GENDER)
        userCity = PrefUtils.getString(activity, CommonKeys.KEY_USER_CITY)
        userState = PrefUtils.getString(activity, CommonKeys.KEY_USER_STATE)
        userCountry = PrefUtils.getString(activity, CommonKeys.KEY_USER_COUNTRY)
        userPlanTitle = PrefUtils.getString(activity, CommonKeys.KEY_USER_PLAN_TITLE)
        userDuration = PrefUtils.getString(activity, CommonKeys.KEY_USER_PLAN_DURATION)
        userAddress = PrefUtils.getString(activity, CommonKeys.KEY_USER_ADRESS)
        userSubsription = PrefUtils.getString(activity, CommonKeys.KEY_SUBSRIPTION)
        userPhone = PrefUtils.getString(activity, CommonKeys.KEY_USER_PHONE)


        enableNotification = PrefUtils.getBoolean(activity, CommonKeys.KEY_USER_ENABLE_NOTIFICATION)
        enableArtistUpdate = PrefUtils.getBoolean(activity, CommonKeys.KEY_ARTIST_UPDATE)

        userdob = PrefUtils.getInt(activity, CommonKeys.KEY_USER_DOB)
        userZipcode = PrefUtils.getInt(activity, CommonKeys.KEY_USER_ZIP_CODE)

        userPlanId = PrefUtils.getInt(activity, CommonKeys.KEY_USER_PLAN_ID)
        userPlanPrize = PrefUtils.getInt(activity, CommonKeys.KEY_USER_PLAN_PRIZE)

        val address = Address(
            userAddress,
            userCity,
            userState,
            userCountry,
            userZipcode
        )

        val subsription = Subscription(
            userPlanId,
            userPlanTitle,
            userPlanPrize,
            userDuration
        )

        val notification = Notification(
            enableNotification,
            enableArtistUpdate
        )

        authModel = AuthModel(
            userName,
            userEmail,
            userAvatar,
            null,
            userdob,
            userPhone,
            userGender,
            false,
            address,
            subsription,
            notification
        )

    }
}