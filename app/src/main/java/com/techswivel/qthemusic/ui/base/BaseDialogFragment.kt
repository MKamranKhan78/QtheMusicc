package com.techswivel.qthemusic.ui.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    protected lateinit var mActivityListener: Any
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mActivityListener = context
        } catch (ex: ClassCastException) {
            Log.e(BaseFragment.TAG, "Unable to cast listener ", ex)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected fun setDialogStyle() {
        if (dialog != null && dialog?.window != null) {
            val lp: WindowManager.LayoutParams = requireActivity().getWindow().getAttributes()
            lp.dimAmount = 1f
            dialog?.window?.setAttributes(lp)
            dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }


}