package com.linkan.multiviewtypelist

import android.content.res.AssetManager
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkan.multiviewtypelist.dto.ItemModel
import com.linkan.multiviewtypelist.util.UtilFunction
import com.linkan.multiviewtypelist.util.ViewType
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

    fun logListItemId(TAG: String, currentList: List<ItemModel>) {

        viewModelScope.launch(Dispatchers.IO) {
            currentList.forEach { itemModel ->
                Log.v(TAG, "Recycler list Item view type ${itemModel.viewType } with Id - ${itemModel.itemId}")

                when(itemModel.viewType){
                    ViewType.StringViewType.TYPE_PHOTO_VIEW ->{
                        if(!TextUtils.isEmpty(itemModel.dataMapModel?.photoPath))
                            Log.v(TAG, "Item photo path - ${itemModel.dataMapModel?.photoPath}")
                    }
                    ViewType.StringViewType.TYPE_COMMENT_VIEW ->{
                        if(itemModel.dataMapModel?.commentModel?.commentEnabled == true){
                            Log.v(TAG, "Comment Switch is - ON")
                        }else{
                            Log.v(TAG, "Comment Switch is - OFF")
                        }

                        if (!TextUtils.isEmpty(itemModel.dataMapModel?.commentModel?.commentText)){
                            Log.v(TAG, "User Commented - ${itemModel.dataMapModel?.commentModel?.commentText}")
                        }else{
                            Log.v(TAG, "User Commented - No Comments")
                        }
                    }
                    ViewType.StringViewType.TYPE_SINGLE_CHOICE_VIEW ->{
                        itemModel.dataMapModel?.choiceList?.forEach { item ->
                            if (item.isSelected)
                                Log.v(TAG, "Single item choosed - ${item.choiceText}")
                        }
                    }
                }

                Log.v(TAG, "---------------------------------")
            }
        }
    }

}