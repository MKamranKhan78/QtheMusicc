package com.techswivel.qthemusic.ui.fragments.paymentTypesBottomSheetFragment

import com.techswivel.qthemusic.models.Payment
import com.techswivel.qthemusic.ui.base.BaseViewModel

class PaymentTypeBottomSheetViewModel : BaseViewModel() {
    var paymentTypeList: ArrayList<Any> = ArrayList()


    private lateinit var mSelectedItem: Payment
    private lateinit var mDataList: MutableList<Payment>

    var mSelectedPlayListItem: Payment?
        set(value) {
            mSelectedItem = value ?: Payment()
        }
        get() {
            return if (::mSelectedItem.isInitialized) {
                mSelectedItem
            } else {
                null
            }
        }

    var mData: MutableList<Payment>
        set(value) {
            mDataList = value
        }
        get() {
            return if (::mDataList.isInitialized) {
                mDataList
            } else {
                mDataList = mutableListOf()
                return mDataList
            }
        }


/*    private lateinit var mSelectedItem: Payment
    private lateinit var mDataList: MutableList<Payment>
    var mSelectedPlayListItem: Payment?
        set(value) {
            mSelectedItem = value ?: Payment()
        }
        get() {
            return if (::mSelectedItem.isInitialized) {
                mSelectedItem
            } else {
                null
            }
        }*/
/*    var mData: MutableList<Payment>
        set(value) {
            mDataList = value
        }
        get() {
            return if (::mDataList.isInitialized) {
                mDataList
            } else {
                mDataList = mutableListOf()
                return mDataList
            }
        }*/

}