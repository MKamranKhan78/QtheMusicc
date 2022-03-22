package com.techswivel.qthemusic.models

import java.io.Serializable

class AuthRequestBuilder : Serializable {
    var email: String? = null
    var password: String? = null
    var loginType: String? = null
    var socialSite: String? = null
    var fcmToken: String? = null
    var accessToken: String? = null
    var deviceIdentifier: String? = null
    var otpType: String? = null
    var phoneNumber: String? = null
    var otp: Int? = null

    fun productId(password: String?) = apply { this.password = password }
    fun email(email: String?) = apply { this.email = email }
    fun loginType(loginType: String?) = apply { this.loginType = loginType }
    fun socialSite(socialSite: String?) = apply { this.socialSite = socialSite }
    fun fcmToken(fcmToken: String?) = apply { this.fcmToken = fcmToken }
    fun accessToken(accessToken: String?) = apply { this.accessToken = accessToken }
    fun deviceIdentifier(deviceIdentifier: String?) = apply { this.deviceIdentifier = deviceIdentifier }
    fun otpType(otpType: String?) = apply { this.otpType = otpType }
    fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
    fun otp(otpType: String?) = apply { this.otp = otp }

    companion object {

        fun builder(builder: AuthRequestBuilder) = AuthRequestModel(
            builder.email,
            builder.password,
            builder.loginType,
            builder.socialSite,
            builder.fcmToken,
            builder.accessToken,
            builder.deviceIdentifier,
            builder.otpType,
            builder.phoneNumber,
            builder.otp
        )
    }

}