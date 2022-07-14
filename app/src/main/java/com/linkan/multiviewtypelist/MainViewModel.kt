package com.linkan.multiviewtypelist

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkan.multiviewtypelist.dto.ItemModel
import com.linkan.multiviewtypelist.util.UtilFunction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val itemListLiveData : MutableLiveData<List<ItemModel>> by lazy {
        MutableLiveData<List<ItemModel>>()
    }

    val mItemListLiveData : LiveData<List<ItemModel>>
        get() = itemListLiveData

    fun loadItemListFromAsset(assets: AssetManager) {
        viewModelScope.launch(Dispatchers.Default) {
            val itemList = UtilFunction.getItemList(assets)

            // mutating original list by adding the option list
            // of type model instead of string
            itemList.forEach {
                itemModel ->  itemModel.dataMapModel?.choiceList =
                UtilFunction.getChoiceList(itemModel.dataMapModel?.choiceOptions)
            }

            itemListLiveData.postValue(itemList)
        }
    }

}