package com.techswivel.qthemusic.models


data class Payment(
    var payment: String?,
    var clickImage: String?
)


/*data class Payment(
    var payment: String?,
    var clickImage: String?
) : BaseObservable(), Serializable {
    @Ignore
    private var mSelectedButtonVisibility: ObservableField<Int>? = null
    constructor() : this(null, null) {
    }
    @Bindable
    fun getSelectedButtonVisibility(): ObservableField<Int>? {
        return mSelectedButtonVisibility
    }
    fun setDownloadButtonVisibility(tickVisibility: ObservableField<Int>) {
        this.mSelectedButtonVisibility = tickVisibility
        notifyPropertyChanged(BR.selectedButtonVisibility)
    }
}*/
