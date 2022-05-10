package com.techswivel.qthemusic.ui.activities.buyingHistoryActivity

interface BuyingHistoryActivityImpl {
    fun openPaymentTypeBottomSheetDialogFragment()
    fun onCancelCallBack()
    fun onItemClickCallBack(paymentType: String?)

}