package com.techswivel.baseproject.utils


object DialogUtils {

//    fun NoInterNetMessage(pContext: Context) {
//        val noInterNetMessage = AlertDialog.Builder(pContext).create()
//        noInterNetMessage.setTitle(pContext.resources.getString(R.string.noInternetTitle))
//        noInterNetMessage.setCancelable(false)
//        noInterNetMessage.setMessage(pContext.resources.getString(R.string.checkInternetConnection))
//        noInterNetMessage.setButton(
//            AlertDialog.BUTTON_POSITIVE, pContext.resources.getString(R.string.ok)
//        ) { dialog, which -> dialog.dismiss() }
//        noInterNetMessage.show()
//    }
//
//    fun SuretyDialog(pContext: Context, mMessage: String, pTitle: String, pCallBack: CallBack) {
//        val suretyDialog = AlertDialog.Builder(pContext).create()
//        suretyDialog.setTitle(pTitle)
//        suretyDialog.setCancelable(false)
//        suretyDialog.setMessage(mMessage)
//        suretyDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE,
//            pContext.resources.getString(R.string.yes)
//        ) { dialog, which ->
//            pCallBack.OnPostivieCallBack()
//            dialog.dismiss()
//        }
//        suretyDialog.setButton(
//            AlertDialog.BUTTON_NEGATIVE,
//            pContext.resources.getString(R.string.no)
//        ) { dialog, which -> dialog.dismiss() }
//        suretyDialog.show()
//    }
//
//    fun PlaceOrderAlerts(pContext: Context, mMessage: String, pCallBack: CallBack) {
//        val placeOrderAlerts = AlertDialog.Builder(pContext).create()
//        placeOrderAlerts.setTitle(pContext.resources.getString(R.string.alert))
//        placeOrderAlerts.setCancelable(false)
//        placeOrderAlerts.setMessage(mMessage)
//        placeOrderAlerts.setButton(
//            AlertDialog.BUTTON_POSITIVE,
//            pContext.resources.getString(R.string.ok)
//        ) { dialog, which ->
//            pCallBack.OnPostivieCallBack()
//            dialog.dismiss()
//        }
//        placeOrderAlerts.show()
//
//    }
//
//    fun RetryDialog(pContext: Context, message: String, pCallBack: CallBack) {
//        val retryAlertDialog = AlertDialog.Builder(pContext).create()
//        retryAlertDialog.setTitle(pContext.resources.getString(R.string.alert))
//        retryAlertDialog.setCancelable(false)
//        retryAlertDialog.setMessage(message)
//        retryAlertDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE, pContext.resources.getString(R.string.retry)
//        ) { dialog, which ->
//            pCallBack.OnPostivieCallBack()
//            dialog.dismiss()
//        }
//        retryAlertDialog.show()
//    }
//
//    interface CallBack {
//        fun OnPostivieCallBack()
//        fun onNegativeCallBack()
//    }
//
//    fun SplashDialog(
//        pContext: Context, mMessage: String, pTitle: String,
//        buttonText: String, dataAvailable: Boolean?, pCallBack: CallBack
//    ) {
//        val splashDialog = AlertDialog.Builder(pContext).create()
//        splashDialog.setTitle(pTitle)
//        splashDialog.setCancelable(false)
//        splashDialog.setMessage(mMessage)
//        splashDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText) { dialog, which ->
//            if (dataAvailable!!) {
//                pCallBack.OnPostivieCallBack()
//            } else {
//                pCallBack.OnPostivieCallBack()
//            }
//            dialog.dismiss()
//        }
//        splashDialog.show()
//    }
//
//    fun ErrorAlert(pContext: Context, pTitle: String, pMessage: String) {
//        val errorDialog = AlertDialog.Builder(pContext).create()
//        //  errorDialog.setTitle(pContext.getString(R.string.error_code).plus(pTitle))
//        errorDialog.setCancelable(false)
//        errorDialog.setMessage(pMessage)
//        errorDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE,
//            pContext.resources.getString(R.string.ok)
//        ) { dialog, which -> dialog.dismiss() }
//        errorDialog.show()
//    }
//
//    fun ErrorAlertWithCallBack(
//        pContext: Context,
//        pTitle: String,
//        pMessage: String,
//        pCallBack: CallBack
//    ) {
//        val errorDialog = AlertDialog.Builder(pContext).create()
//        //  errorDialog.setTitle(pContext.getString(R.string.error_code).plus(pTitle))
//        errorDialog.setCancelable(false)
//        errorDialog.setMessage(pMessage)
//        errorDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE,
//            pContext.resources.getString(R.string.ok)
//        ) { dialog, which ->
//            pCallBack.OnPostivieCallBack()
//            dialog.dismiss()
//        }
//        errorDialog.show()
//    }
//    fun RetryErrorAlert(pContext: Context, pTitle: String, pMessage: String, pCallBack: CallBack) {
//        val errorDialog = AlertDialog.Builder(pContext).create()
//        errorDialog.setTitle(pContext.getString(R.string.error_code).plus(pTitle))
//        errorDialog.setCancelable(false)
//        errorDialog.setMessage(pMessage)
//        errorDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE,
//            pContext.resources.getString(R.string.retry)
//        ) { dialog, which ->
//            pCallBack.OnPostivieCallBack()
//            dialog.dismiss()
//        }
//        errorDialog.show()
//    }
//
//    fun SessionExpireAlert(pContext: Context, pCallBack: CallBack) {
//        val errorDialog = AlertDialog.Builder(pContext).create()
//        errorDialog.setTitle(pContext.getString(R.string.session_expire_title))
//        errorDialog.setCancelable(false)
//        errorDialog.setMessage(pContext.getString(R.string.session_expire_message))
//        errorDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE,
//            pContext.resources.getString(R.string.ok)
//        ) { dialog, which ->
//            pCallBack.OnPostivieCallBack()
//            dialog.dismiss()
//        }
//        errorDialog.show()
//    }
//    fun RunTimeAlert(
//        pContext: Context,
//        pTitle: String,
//        pMessage: String,
//        pPosBtnText: String,
//        pNegBtnText: String,
//        pCallBack: CallBack
//    ) {
//        val callDialoge = AlertDialog.Builder(pContext).create()
//        callDialoge.setTitle(pTitle)
//        callDialoge.setCancelable(false)
//        callDialoge.setMessage(pMessage)
//        callDialoge.setButton(
//            AlertDialog.BUTTON_POSITIVE,
//            pPosBtnText
//        ) { dialog, which ->
//            pCallBack.OnPostivieCallBack()
//            dialog.dismiss()
//        }
//        callDialoge.setButton(AlertDialog.BUTTON_NEGATIVE, pNegBtnText)
//        { dialog, which ->
//            dialog.dismiss()
//        }
//        callDialoge.show()
//        callDialoge.getButton(AlertDialog.BUTTON_NEGATIVE)
//            .setTextColor(ContextCompat.getColor(pContext, R.color.colorBlue));
//        callDialoge.getButton(AlertDialog.BUTTON_POSITIVE)
//            .setTextColor(ContextCompat.getColor(pContext, R.color.colorBlue));
//    }
//
//    fun customConfirmationDialog(
//        pContext: Context,
//        pTitle: String,
//        pMessage: String,
//        pPosBtnText: String,
//        pNegBtnText: String,
//        pCallBack: CallBack
//    ) {
//        val dialog = Dialog(pContext)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.custom_dialog_confirmation)
//        val title = dialog.findViewById(R.id.customDialogTitle) as TextView
//        val message = dialog.findViewById(R.id.customDialogMessage) as TextView
//        val yesBtn = dialog.findViewById(R.id.cutomDialogBtnYes) as Button
//        val noBtn = dialog.findViewById(R.id.cutomDialogBtnNo) as TextView
//        title.text = pTitle
//        message.text = pMessage
//        yesBtn.text = pPosBtnText
//        noBtn.text = pNegBtnText
//        yesBtn.setOnClickListener {
//            pCallBack.OnPostivieCallBack()
//            dialog.dismiss()
//        }
//        noBtn.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.show()
//
//    }
//
//    fun scheduleOrderPlacedDialog(pContext: Context, pCallBack: CallBack) {
//        val dialog = Dialog(pContext)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.custom_dialog_order_placed)
//        val okBtn = dialog.findViewById(R.id.customDialogBtnOk) as Button
//        okBtn.setOnClickListener {
//            pCallBack.OnPostivieCallBack()
//            dialog.dismiss()
//        }
//        dialog.show()
//    }
}
