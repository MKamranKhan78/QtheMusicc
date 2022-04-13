package com.techswivel.qthemusic.models

import java.io.Serializable

class AuthRequestBuilder : Serializable {
    var email: String? = null
    var name: String? = null
    var dob: Int? = null
    var gender: String? = null
    var completeAddress: String? = null
    var city: String? = null
    var state: String? = null
    var country: String? = null
    var zipCode: Int? = null
    var profile: String? = null
    var socialId: String? = null
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
    fun name(name: String?) = apply { this.name = name }
    fun dob(dob: Int?) = apply { this.dob = dob }
    fun completeAddress(address: String?) = apply { this.completeAddress = address }
    fun gender(gender: String?) = apply { this.gender = gender }
    fun city(city: String?) = apply { this.city= city }
    fun state(state: String?) = apply { this.state = state }
    fun country(country: String?) = apply { this.country= country }
    fun zipCode(zipCode: Int?) = apply { this.zipCode = zipCode }
    fun socialId(socialId: String?) = apply { this.socialId = socialId }
    fun profile(profile: String?) = apply { this.profile = profile }
    fun loginType(loginType: String?) = apply { this.loginType = loginType }
    fun socialSite(socialSite: String?) = apply { this.socialSite = socialSite }
    fun fcmToken(fcmToken: String?) = apply { this.fcmToken = fcmToken }
    fun accessToken(accessToken: String?) = apply { this.accessToken = accessToken }
    fun deviceIdentifier(deviceIdentifier: String?) =
        apply { this.deviceIdentifier = deviceIdentifier }

    fun otpType(otpType: String?) = apply { this.otpType = otpType }
    fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
    fun otp(otpType: String?) = apply { this.otp = otp }

    companion object {

        fun builder(builder: AuthRequestBuilder) = AuthRequestModel(
            builder.name,
            builder.email,
            builder.dob,
            builder.gender,
            builder.completeAddress,
            builder.city,
            builder.state,
            builder.country,
            builder.zipCode,
            builder.profile,
            builder.socialId,
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