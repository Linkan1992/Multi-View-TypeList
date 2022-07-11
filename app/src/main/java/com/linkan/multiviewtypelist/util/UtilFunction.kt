package com.linkan.multiviewtypelist.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.linkan.multiviewtypelist.dto.ItemModel
import java.io.IOException

object UtilFunction {

    fun getItemList(context: Context): List<ItemModel> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("itemlist.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        val listCountryType = object : TypeToken<List<ItemModel>>() {}.type
        return Gson().fromJson(jsonString, listCountryType)
    }

}