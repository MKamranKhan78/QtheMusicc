package com.techswivel.qthemusic.ui.fragments.yourInterestFragment

import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.models.Category

interface YourInterestImp : BaseInterface {
    fun getCategoriesResponse(lis:List<Category>?)
}