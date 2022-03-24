package com.techswivel.qthemusic.models

class AuthModelBuilder {
    var name: String? = null
    var email: String? = null
    var avatar: String? = null
    var jwt: String? = null
    var dOB: Int? = null
    var phoneNumber: String? = null
    var gender: String? = null
    var isInterestsSet: Boolean? = null
    var address: Address? = null
    var subscription: Subscription? = null
    var notification: Notification? = null

    fun name(name: String?) = apply { this.name = name }
    fun email(email: String?) = apply { this.email = email }
    fun avatar(avatar: String?) = apply { this.avatar = avatar }
    fun jwt(jwt: String?) = apply { this.jwt = jwt }
    fun dOB(dOB: Int?) = apply { this.dOB = dOB }
    fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
    fun gender(gender: String?) = apply { this.gender = gender }
    fun isInterestsSet(isInterestsSet: Boolean?) = apply { this.isInterestsSet = isInterestsSet }
    fun address(address: Address?) = apply { this.address = address }
    fun subscription(subscription: Subscription?) = apply { this.subscription = subscription }
    fun notification(notification: Notification?) = apply { this.notification = notification }


    companion object {
        fun build(builder: AuthModelBuilder) = AuthModel(
            builder.name,
            builder.email,
            builder.avatar,
            builder.jwt,
            builder.dOB,
            builder.phoneNumber,
            builder.gender,
            builder.isInterestsSet,
            builder.address,
            builder.subscription,
            builder.notification
        )
    }
}