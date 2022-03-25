package com.techswivel.qthemusic.ui.fragments.addGenderDialogFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentAddGenderDialogBinding
import com.techswivel.qthemusic.ui.base.BaseDialogFragment


class AddGenderDialogFragment : BaseDialogFragment(), BaseInterface {

    companion object {
        fun newInstance() = AddGenderDialogFragment()
    }

    private lateinit var mBinding: FragmentAddGenderDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setDialogStyle()
        mBinding = FragmentAddGenderDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()

        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            Toast.makeText(
                QTheMusicApplication.getContext(), " On checked change : ${radio.text}",
                Toast.LENGTH_SHORT
            ).show()
        }


        // Get radio group selected status and text using button click event
        mBinding.updateButton.setOnClickListener {
            // Get the checked radio button id from radio group
            var id: Int = mBinding.radioGroup.checkedRadioButtonId
            if (id != -1) { // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio: RadioButton = view.findViewById(id)
                Toast.makeText(
                    QTheMusicApplication.getContext(), "On button click : ${radio.text}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // If no radio button checked in this radio group
                Toast.makeText(
                    QTheMusicApplication.getContext(), "On button click : nothing selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }

    protected fun setDialogStyle() {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    private fun clickListener() {
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }
    }

    fun radioButtonClick(view: View) {
        // Get the clicked radio button instance
        val radio: RadioButton = view.findViewById(mBinding.radioGroup.checkedRadioButtonId)
        Toast.makeText(
            QTheMusicApplication.getContext(), "On click : ${radio.text}",
            Toast.LENGTH_SHORT
        ).show()
    }


}