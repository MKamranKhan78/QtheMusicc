package com.techswivel.qthemusic.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.techswivel.qthemusic.R


object DialogUtils {


    fun suretyDialog(pContext: Context, mMessage: String, pTitle: String, pCallBack: CallBack) {
        val suretyDialog = AlertDialog.Builder(pContext).create()
        suretyDialog.setTitle(pTitle)
        suretyDialog.setCancelable(false)
        suretyDialog.setMessage(mMessage)
        suretyDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            pContext.resources.getString(R.string.yes)
        ) { dialog, _ ->
            pCallBack.onPositiveCallBack()
            dialog.dismiss()
        }
        suretyDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            pContext.resources.getString(R.string.no)
        ) { dialog, _ ->
            pCallBack.onNegativeCallBack()
            dialog.dismiss()
        }
        suretyDialog.show()
    }


    fun retryDialog(pContext: Context, message: String, pCallBack: CallBack) {
        val retryAlertDialog = AlertDialog.Builder(pContext).create()
        retryAlertDialog.setTitle(pContext.resources.getString(R.string.alert))
        retryAlertDialog.setCancelable(false)
        retryAlertDialog.setMessage(message)
        retryAlertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, pContext.resources.getString(R.string.retry)
        ) { dialog, _ ->
            pCallBack.onPositiveCallBack()
            dialog.dismiss()
        }
        retryAlertDialog.show()
    }


    fun errorAlert(pContext: Context, pTitle: String, pMessage: String) {
        val errorDialog = AlertDialog.Builder(pContext).create()
        //  errorDialog.setTitle(pContext.getString(R.string.error_code).plus(pTitle))
        errorDialog.setCancelable(false)
        errorDialog.setMessage(pMessage)
        errorDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            pContext.resources.getString(R.string.ok)
        ) { dialog, _ -> dialog.dismiss() }
        errorDialog.show()
    }

    fun errorAlertWithCallBack(
        pContext: Context,
        pTitle: String,
        pMessage: String,
        pCallBack: CallBack
    ) {
        val errorDialog = AlertDialog.Builder(pContext).create()
        //  errorDialog.setTitle(pContext.getString(R.string.error_code).plus(pTitle))
        errorDialog.setCancelable(false)
        errorDialog.setMessage(pMessage)
        errorDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            pContext.resources.getString(R.string.ok)
        ) { dialog, _ ->
            pCallBack.onPositiveCallBack()
            dialog.dismiss()
        }
        errorDialog.show()
    }

    fun retryErrorAlert(pContext: Context, pTitle: String, pMessage: String, pCallBack: CallBack) {
        val errorDialog = AlertDialog.Builder(pContext).create()
        errorDialog.setTitle(pContext.getString(R.string.error))
        errorDialog.setCancelable(false)
        errorDialog.setMessage(pMessage)
        errorDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            pContext.resources.getString(R.string.retry)
        ) { dialog, _ ->
            pCallBack.onPositiveCallBack()
            dialog.dismiss()
        }
        errorDialog.show()
    }

    fun sessionExpireAlert(pContext: Context, pCallBack: CallBack) {
        val errorDialog = AlertDialog.Builder(pContext).create()
        errorDialog.setTitle(pContext.getString(R.string.session_expire_title))
        errorDialog.setCancelable(false)
        errorDialog.setMessage(pContext.getString(R.string.session_expire_message))
        errorDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            pContext.resources.getString(R.string.ok)
        ) { dialog, _ ->
            pCallBack.onPositiveCallBack()
            dialog.dismiss()
        }
        errorDialog.show()
    }

    fun runTimeAlert(
        pContext: Context,
        pTitle: String,
        pMessage: String,
        pPosBtnText: String,
        pNegBtnText: String,
        pCallBack: CallBack
    ) {
        val callDialog = AlertDialog.Builder(pContext).create()
        callDialog.setTitle(pTitle)
        callDialog.setCancelable(false)
        callDialog.setMessage(pMessage)
        callDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            pPosBtnText
        ) { dialog, _ ->
            pCallBack.onPositiveCallBack()
            dialog.dismiss()
        }
        callDialog.setButton(AlertDialog.BUTTON_NEGATIVE, pNegBtnText)
        { dialog, _ ->
            pCallBack.onNegativeCallBack()
            dialog.dismiss()
        }
        callDialog.show()
        callDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(pContext, R.color.colorPrimary));
        callDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(pContext, R.color.colorPrimary));
    }

    fun customConfirmationDialog(
        pContext: Context,
        pTitle: String,
        pMessage: String,
        pPosBtnText: String,
        pNegBtnText: String,
        pCallBack: CallBack
    ) {
        val dialog = Dialog(pContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_confirmation)
        val title = dialog.findViewById(R.id.customDialogTitle) as TextView
        val message = dialog.findViewById(R.id.customDialogMessage) as TextView
        val yesBtn = dialog.findViewById(R.id.cutomDialogBtnYes) as Button
        val noBtn = dialog.findViewById(R.id.cutomDialogBtnNo) as TextView
        title.text = pTitle
        message.text = pMessage
        yesBtn.text = pPosBtnText
        noBtn.text = pNegBtnText
        yesBtn.setOnClickListener {
            pCallBack.onPositiveCallBack()
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            pCallBack.onNegativeCallBack()
            dialog.dismiss()
        }
        dialog.show()

    }

    fun goToSystemLocationSetting(activity: Activity, permissionMessage: String) {
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(activity).create()
        alertDialog.setTitle(activity.getString(R.string.alert))
        alertDialog.setMessage(
            permissionMessage
        )
        alertDialog.setCancelable(false)
        alertDialog.setButton(
            androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE,
            activity.getString(R.string.go_to_setting)
        ) { dialog: DialogInterface, which: Int ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivity(intent)
            dialog.dismiss()
        }
        alertDialog.setButton(
            androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, activity.getString(R.string.cancel)
        ) { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    interface CallBack {
        fun onPositiveCallBack()
        fun onNegativeCallBack()
    }

}
