package com.techswivel.qthemusic.utils

import android.util.Log
import com.techswivel.qthemusic.BuildConfig
import com.techswivel.qthemusic.constant.Constants

/**
 * Mock Log implementation for testing on non android host.
 */
object Log {
    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun v(tag: String?, msg: String?) {
        if (!isProduction) Log.v(tag, msg ?: "")
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun v(tag: String?, msg: String?, tr: Throwable?) {
        if (!isProduction) Log.v(tag, msg, tr)
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun d(tag: String?, msg: String?) {
        if (!isProduction) Log.d(tag, msg ?: "")
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun d(tag: String?, msg: String?, tr: Throwable?) {
        if (!isProduction) Log.d(tag, msg, tr)
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun i(tag: String?, msg: String?) {
        if (!isProduction) Log.i(tag, msg ?: "")
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun i(tag: String?, msg: String?, tr: Throwable?) {
        if (!isProduction) Log.i(tag, msg, tr)
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun w(tag: String?, msg: String?) {
        if (!isProduction) Log.w(tag, msg ?: "")
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun w(tag: String?, msg: String?, tr: Throwable?) {
        if (!isProduction) Log.w(tag, msg, tr)
    }

    /*
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    fun w(tag: String?, tr: Throwable?) {
        if (!isProduction) Log.w(tag, tr)
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun e(tag: String?, msg: String?) {
        if (!isProduction) Log.e(tag, msg ?: "")
    }

    /**
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun e(tag: String?, msg: String?, tr: Throwable?) {
        if (!isProduction) Log.e(tag, msg, tr)
    }

    private val isProduction: Boolean
        private get() = BuildConfig.FLAVOR.equals(Constants.PRODUCTION)
}