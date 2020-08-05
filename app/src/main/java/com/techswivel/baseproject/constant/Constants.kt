package com.techswivel.baseproject.constant


object Constants {

    const val STAGING = "STAGING"
    const val PRODUTION = "PRODUTION"
    const val ACCEPTANCE = "ACCEPTANCE"
    const val DEVELOPMENT = "DEVELOPMENT"
    const val STAGING_SERVER_URL = "https://staging.udeoglobe.com/api/v1/"
    const val PRODUTION_SERVER_URL = "https://udeoglobe.com/api/v1/"
    const val ACCEPTANCE_SERVER_URL = "https://acceptance.udeoglobe.com/api/v1/"
    const val DEVELOPMENT_SERVER_URL = "https://www.udeoglobe.com/development/"

    val NO_INTERNET_TITLE = "NO INTERNET"
    val NO_INTERNET_MESSAGE = "Please connect to a stable internet"
    val LOCATION_PERMISSION_TITLE = "Location permission denied!"
    val LOCATION_PERMISSION_MESSAGE = "Location permission requires to continue"
    val DIRECTION_API = "https://maps.googleapis.com/maps/api/directions/json?origin="


    const val LOCATION_UPDATE_TIME = 0 //in miliSeconds like miliSeconds X seconds X mint X hours
    const val LOCATION_UPDATE_DISTANCE = 20 //in meter


    const val STRIPE_KEY = "abc"
    const val SPALSHDELAY = 1000 * 3 * 1 // in milliSeconds
    const val KEY_REQUEST_PLACE: Int = 1000
    const val GOOGLE_REQUEST_CODE: Int = 1001
    const val EARTHRADIUS = 6366198.0

    const val DEFAULT_DATE_FORMATE = "dd MMMM,yyyy"


}
