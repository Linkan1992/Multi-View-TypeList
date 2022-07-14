package com.linkan.multiviewtypelist.util

import android.content.Context
import android.content.res.AssetManager
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.linkan.multiviewtypelist.dto.ChoiceModel
import com.linkan.multiviewtypelist.dto.ItemModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object UtilFunction {

    fun getItemList(assets: AssetManager): List<ItemModel> {

        lateinit var jsonString: String
        try {
            jsonString = assets.open("itemlist.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        val listItemType = object : TypeToken<List<ItemModel>>() {}.type
        return Gson().fromJson(jsonString, listItemType)
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    fun createImageFile(context : Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun getChoiceList(choiceOptions : List<String>?) : ArrayList<ChoiceModel> {
        return ArrayList<ChoiceModel>().apply{
            choiceOptions?.forEach { choice ->
                this.add(ChoiceModel(isSelected = false, choiceText = choice))
            }
        }
    }

}