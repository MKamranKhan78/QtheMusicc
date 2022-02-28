package com.techswivel.qthemusic.source.remote.rxjava

/**
 * This class is model class to hold error code and error message
 */
class CustomError
/**
 * Construstor of CustomError class
 *
 * @param message String Error message
 * @param code    int Error Code like 400,404 etc
 */
    (
    /**
     * @return String message or getter of message attribute
     */
    /**
     * Setter of Error Message
     *
     * @param message String message of error
     */
    var message: String,
    /**
     * getter of error code
     *
     * @return error code
     */
    /**
     * setter of error code
     *
     * @param code int error code
     */
    var code: Int
)
