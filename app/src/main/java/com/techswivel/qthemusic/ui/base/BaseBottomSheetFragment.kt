package com.techswivel.qthemusic.ui.base

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techswivel.qthemusic.utils.Log

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    protected lateinit var mActivityListener: Any

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mActivityListener = context
        } catch (ex: ClassCastException) {
            Log.e(BaseFragment.TAG, "Unable to cast listener ", ex)
        }
    }

}