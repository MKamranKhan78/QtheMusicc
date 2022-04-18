package com.techswivel.qthemusic.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.BuildConfig
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.dataManager.LocalDataManager
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.Address
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.Notification
import com.techswivel.qthemusic.models.Subscription
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.rxjava.DisposableManager
import com.techswivel.qthemusic.ui.activities.splashActivity.SplashActivity
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys


abstract class BaseViewModel : ViewModel() {
    val mRemoteDataManager = RemoteDataManager
    val mLocalDataManager = LocalDataManager

    fun setServerName(textView: TextView) {
        if (!BuildConfig.FLAVOR.equals(Constants.PRODUCTION)) {
            textView.visibility = View.VISIBLE
            textView.text = BuildConfig.FLAVOR.toUpperCase()
        } else {
            textView.visibility = View.GONE
        }
    }

    fun cancelServerRequest() {
        DisposableManager.dispose()
    }

    fun getDummyData(): AuthModel {
        return mRemoteDataManager.getAuthDetails()
    }

    fun setDataInSharedPrefrence(authModel: AuthModel?) {

        PrefUtils.setString(QTheMusicApplication.getContext(), CommonKeys.KEY_USER_NAME, authModel?.name)
        PrefUtils.setString(QTheMusicApplication.getContext(), CommonKeys.KEY_USER_EMAIL, authModel?.email)
        PrefUtils.setBoolean(QTheMusicApplication.getContext(),CommonKeys.KEY_IS_INTEREST_SET,false)
        PrefUtils.setBoolean(QTheMusicApplication.getContext(),CommonKeys.KEY_IS_LOGGED_IN,true)
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_NAME,
            authModel?.name
        )
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_EMAIL,
            authModel?.email
        )
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_AVATAR,
            authModel?.avatar
        )
        PrefUtils.setBoolean(
            QTheMusicApplication.getContext(), CommonKeys.KEY_USER_ENABLE_NOTIFICATION,
            authModel?.notification?.isNotificationEnabled ?: true
        )
        PrefUtils.setBoolean(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_ARTIST_UPDATE,
            authModel?.notification?.isArtistUpdateEnabled ?: true
        )
        PrefUtils.setString(QTheMusicApplication.getContext(), CommonKeys.KEY_USER_PHONE, authModel?.phoneNumber)
        authModel?.dOB?.let { doB -> PrefUtils.setInt(QTheMusicApplication.getContext(), CommonKeys.KEY_USER_DOB, doB) }
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_PHONE,
            authModel?.phoneNumber
        )
        authModel?.dOB?.let { doB ->
            PrefUtils.setLong(
                QTheMusicApplication.getContext(),
                CommonKeys.KEY_USER_DOB,
                doB
            )
        }
        authModel?.subscription?.planId?.let { planId ->
            PrefUtils.setInt(
                QTheMusicApplication.getContext(), CommonKeys.KEY_USER_PLAN_ID,
                planId
            )
        }
        authModel?.subscription?.planPrice?.let { planPrice ->
            PrefUtils.setFloat(
                QTheMusicApplication.getContext(), CommonKeys.KEY_USER_PLAN_PRIZE,
                planPrice
            )
        }
        authModel?.address?.zipCode?.let { zipCode ->
            PrefUtils.setInt(
                QTheMusicApplication.getContext(), CommonKeys.KEY_USER_ZIP_CODE,
                zipCode
            )
        }
        PrefUtils.setString(QTheMusicApplication.getContext(), CommonKeys.KEY_USER_GENDER, authModel?.gender)
        PrefUtils.setString(QTheMusicApplication.getContext(), CommonKeys.KEY_USER_CITY, authModel?.address?.city)
        PrefUtils.setString(QTheMusicApplication.getContext(), CommonKeys.KEY_USER_STATE, authModel?.address?.state)
        PrefUtils.setString(QTheMusicApplication.getContext(), CommonKeys.KEY_USER_COUNTRY, authModel?.address?.country)
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_GENDER,
            authModel?.gender
        )
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_CITY,
            authModel?.address?.city
        )
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_STATE,
            authModel?.address?.state
        )
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_COUNTRY,
            authModel?.address?.country
        )
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_PLAN_TITLE,
            authModel?.subscription?.planTitle
        )
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_PLAN_DURATION,
            authModel?.subscription?.planDuration
        )
        PrefUtils.setString(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_USER_ADRESS,
            authModel?.address?.completeAddress
        )
    }


    fun getPrefrencesData(context: Context): AuthModel {

        var userName: String? = null
        var userEmail: String? = null
        var userAvatar: String? = null
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

        var userdob: Long? = null
        var userZipcode: Int? = null

        var userPlanId: Int? = null
        var userPlanPrize: Float? = null

        userName = PrefUtils.getString(context, CommonKeys.KEY_USER_NAME)
        userEmail = PrefUtils.getString(context, CommonKeys.KEY_USER_EMAIL)
        userAvatar = PrefUtils.getString(context, CommonKeys.KEY_USER_AVATAR)
        userGender = PrefUtils.getString(context, CommonKeys.KEY_USER_GENDER)
        userCity = PrefUtils.getString(context, CommonKeys.KEY_USER_CITY)
        userState = PrefUtils.getString(context, CommonKeys.KEY_USER_STATE)
        userCountry = PrefUtils.getString(context, CommonKeys.KEY_USER_COUNTRY)
        userPlanTitle = PrefUtils.getString(context, CommonKeys.KEY_USER_PLAN_TITLE)
        userDuration = PrefUtils.getString(context, CommonKeys.KEY_USER_PLAN_DURATION)
        userAddress = PrefUtils.getString(context, CommonKeys.KEY_USER_ADRESS)
        userPhone = PrefUtils.getString(context, CommonKeys.KEY_USER_PHONE)
        enableNotification = PrefUtils.getBoolean(context, CommonKeys.KEY_USER_ENABLE_NOTIFICATION)
        enableArtistUpdate = PrefUtils.getBoolean(context, CommonKeys.KEY_ARTIST_UPDATE)
        userdob = PrefUtils.getLong(context, CommonKeys.KEY_USER_DOB)
        userZipcode = PrefUtils.getInt(context, CommonKeys.KEY_USER_ZIP_CODE)
        userPlanId = PrefUtils.getInt(context, CommonKeys.KEY_USER_PLAN_ID)
        userPlanPrize = PrefUtils.getFloat(context, CommonKeys.KEY_USER_PLAN_PRIZE)


        val subsription = Subscription(userPlanId, userPlanTitle, userPlanPrize, userDuration)
        val notification = Notification(enableNotification, enableArtistUpdate)
        val address = Address(userAddress, userCity, userState, userCountry, userZipcode)
        val authModel = AuthModel(
            userName,
            userEmail,
            userAvatar,
            null, userdob, userPhone, userGender, false, address, subsription, notification
        )
        return authModel
    }


    fun clearAppSession(activity: Activity) {

        PrefUtils.clearAllPrefData(QTheMusicApplication.getContext())
        ActivityUtils.startNewActivity(
            activity,
            SplashActivity::class.java,
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
    }
}