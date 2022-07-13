package com.linkan.multiviewtypelist.util

import android.content.Context

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat




object PermissionUtil {

    val permissionArray = arrayOf("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA")

    val REQUEST_CAMERA = 101

     fun checkPermission(context : Context, permissionArg : Array<String>): Boolean {
        val permissionGrantList = permissionArg.filter { permission -> ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED }
       /* val result =
            ContextCompat.checkSelfPermission(context,
                permissionArg[0])
        val result1 =
            ContextCompat.checkSelfPermission(context,
                permissionArg[1])*/
        return permissionGrantList.size == permissionArg.size
    }

     fun requestPermission(activity : AppCompatActivity, permissionArg : Array<String>, requestCode : Int) {
        ActivityCompat.requestPermissions(activity, permissionArg,
            requestCode)
    }

}