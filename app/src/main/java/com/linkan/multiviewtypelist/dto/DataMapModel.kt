package com.linkan.multiviewtypelist.dto

import com.google.gson.annotations.SerializedName
import com.linkan.multiviewtypelist.util.JsonKeys

data class DataMapModel(

    @SerializedName(JsonKeys.KEY_OPTIONS)
    val choiceOptions : List<String>? = null,
    @SerializedName(JsonKeys.KEY_PHOTO_PATH)
    val photoPath : String? = null,
    @SerializedName(JsonKeys.KEY_COMMENT)
    val commentText : String? = null

)