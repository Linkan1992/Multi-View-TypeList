package com.linkan.multiviewtypelist.adapter

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.linkan.multiviewtypelist.dto.ItemModel

class ItemCommentViewModel(private val item: ItemModel) {

    val commentEnable = ObservableBoolean(item.dataMapModel?.commentModel?.commentEnabled == true)

    val commentText = ObservableField<String>(item.dataMapModel?.commentModel?.commentText)

    init {
        commentText.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                item.dataMapModel?.commentModel?.commentText = commentText.get()
            }
        })
    }

    fun onCommentEnableDisable(checked: Boolean) {
        // implementation
        item.dataMapModel?.commentModel?.commentEnabled = checked
        commentEnable.set(checked)
    }

}