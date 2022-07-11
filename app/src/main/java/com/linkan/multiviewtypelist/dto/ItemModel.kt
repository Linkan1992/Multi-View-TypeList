package com.linkan.multiviewtypelist.dto

import com.google.gson.annotations.SerializedName
import com.linkan.multiviewtypelist.util.JsonKeys

data class ItemModel (
    @SerializedName(JsonKeys.KEY_TYPE)
    val viewType : String,
    @SerializedName(JsonKeys.KEY_ID)
    val itemId : String,
    @SerializedName(JsonKeys.KEY_TITLE)
    val title : String? = null,
    @SerializedName(JsonKeys.KEY_DATAMAP)
    val dataMapModel : DataMapModel? = null
)
