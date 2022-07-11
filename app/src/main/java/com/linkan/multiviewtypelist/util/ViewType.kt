package com.linkan.multiviewtypelist.util

object ViewType {

    final const val TYPE_PHOTO_VIEW = 1
    final const val TYPE_SINGLE_CHOICE_VIEW = 2
    final const val TYPE_COMMENT_VIEW = 3

    object StringViewType{
        final const val TYPE_PHOTO_VIEW = "PHOTO"
        final const val TYPE_SINGLE_CHOICE_VIEW = "SINGLE_CHOICE"
        final const val TYPE_COMMENT_VIEW = "COMMENT"
    }

    fun getItemViewType(stringViewType : String) : Int = when(stringViewType){
        StringViewType.TYPE_PHOTO_VIEW -> TYPE_PHOTO_VIEW
        StringViewType.TYPE_SINGLE_CHOICE_VIEW -> TYPE_SINGLE_CHOICE_VIEW
        StringViewType.TYPE_COMMENT_VIEW -> TYPE_COMMENT_VIEW
        else -> TYPE_PHOTO_VIEW
    }

}