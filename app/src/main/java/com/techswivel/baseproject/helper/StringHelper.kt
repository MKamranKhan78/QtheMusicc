package com.techswivel.udeoglobe.helper

import android.content.Context
import android.text.Spanned
import androidx.core.text.HtmlCompat

object StringHelper {

    fun getStringFromId(mContext: Context, mId: Int): Spanned {
        return HtmlCompat.fromHtml(
            mContext.resources.getString(
                mId
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    fun getStringFromId(mContext: Context, mId: Int, input: String): Spanned {
        return HtmlCompat.fromHtml(
            mContext.resources.getString(
                mId, input
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    fun getStringFromId(
        mContext: Context,
        mId: Int,
        stringOne: String,
        stringTwo: String
    ): Spanned {
        return HtmlCompat.fromHtml(
            mContext.resources.getString(
                mId, stringOne, stringTwo
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}