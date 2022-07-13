package com.linkan.multiviewtypelist


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.linkan.multiviewtypelist.adapter.ItemClickedCallback
import com.linkan.multiviewtypelist.adapter.MultiViewListAdapter
import com.linkan.multiviewtypelist.databinding.ActivityMainBinding
import com.linkan.multiviewtypelist.dto.ItemModel
import com.linkan.multiviewtypelist.util.PermissionUtil
import com.linkan.multiviewtypelist.util.UtilFunction
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1

     lateinit var mDataBinding : ActivityMainBinding
     lateinit var listItemAdapter : MultiViewListAdapter

     var selectedListItemPosition : Int = -1

     var isPhotoEnlarged = false

     private val mViewModel : MainViewModel by lazy {
         ViewModelProvider(this).get(MainViewModel::class.java)
     }

    private val itemClickCallback = object : ItemClickedCallback {

        override fun capturePhoto(position: Int, item: ItemModel) {
            selectedListItemPosition = position

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                val hasPermission = PermissionUtil.checkPermission(this@MainActivity, PermissionUtil.permissionArray)

                if(!hasPermission){
                    // request permission
                    PermissionUtil.requestPermission(this@MainActivity, PermissionUtil.permissionArray, PermissionUtil.REQUEST_CAMERA)
                }else{
                    capturePicture()
                }
            }else{
                capturePicture()
            }
        }

        override fun enlargePhoto(item: ItemModel) {

            isPhotoEnlarged = true

            val photoPathFile = item.dataMapModel?.photoPath.run { File(this ?: "") }

            Glide.with(this@MainActivity)
                .load(Uri.fromFile(photoPathFile))
                .into(mDataBinding.imgvEnlarge)

            mDataBinding.imgvEnlarge.visibility = View.VISIBLE
           // mDataBinding.imgvEnlarge.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.zoom_in))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mDataBinding.vm = mViewModel

        initActionBar()
        initClickListener()
        initRecyclerView()
        subscribeToLiveData()

        mViewModel.loadItemListFromAsset(this@MainActivity.assets)
    }

    private fun initClickListener() {
        mDataBinding.imgvEnlarge.setOnClickListener {
            resetEnlargePhotoVisibility()
        }
    }

    private fun resetEnlargePhotoVisibility(){
        if (isPhotoEnlarged) {
            mDataBinding.imgvEnlarge.visibility = View.GONE
            isPhotoEnlarged = false
        }
    }

    private fun subscribeToLiveData() {
        mViewModel.mItemListLiveData.observe(this@MainActivity, Observer { itemList ->
            listItemAdapter.submitList(itemList.toMutableList())
        })

        listItemAdapter.mSelectedItemLiveData.observe(this@MainActivity, Observer { item ->

           /* selectedListItemPosition = item

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                val hasPermission = PermissionUtil.checkPermission(this, PermissionUtil.permissionArray)

                if(!hasPermission){
                    // request permission
                    PermissionUtil.requestPermission(this@MainActivity, PermissionUtil.permissionArray, PermissionUtil.REQUEST_CAMERA)
                }else{
                    capturePicture()
                }
            }else{
                capturePicture()
            }*/
           // listItemAdapter.submitList(itemList.toMutableList())
        })
    }


    private fun initActionBar() {
        setSupportActionBar(mDataBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        title = getString(R.string.item_list)
    }

    private fun initRecyclerView() {
        mDataBinding.rviewItem.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            //   val dividerItemDecoration = DividerItemDecoration(context,DividerItemDecoration.VERTICAL)
            //   addItemDecoration(dividerItemDecoration)
            addItemDecoration(object : DividerItemDecoration(context, LinearLayoutManager.VERTICAL){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State,
                ) {
                    val position = parent.getChildAdapterPosition(view)
                    // if layout reverse == true, hide the divider for the last child
                    // since layout is reversed hence last item is at first hence position == 0
                    // if layout reverse == false, position == state.itemCount - 1
                    if(position == state.itemCount - 1){
                        outRect.setEmpty()
                    }else{
                        super.getItemOffsets(outRect, view, parent, state)
                    }
                }
            }.apply {
                setDrawable(ContextCompat.getDrawable(context, R.drawable.divider)!!)
            })
        }
        listItemAdapter = MultiViewListAdapter(itemClickCallback)
        mDataBinding.rviewItem.adapter = listItemAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.multi_view_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        else if (item.itemId == R.id.action_print_id) {
                Toast.makeText(this, "Print Id Clicked", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun capturePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    UtilFunction.createImageFile(this)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.linkan.multiviewtypelist.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                val photoPathFile = File(UtilFunction.currentPhotoPath)
                val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(photoPathFile))
               // imageView.setImageBitmap(imageBitmap)
                if (selectedListItemPosition > -1){
                    val updatedList = listItemAdapter.currentList.apply {
                        this[selectedListItemPosition].dataMapModel?.photoPath = UtilFunction.currentPhotoPath
                    }
                    listItemAdapter.submitList(null)
                    listItemAdapter.submitList(ArrayList<ItemModel>().apply { addAll(updatedList) })
                }
            }
        } catch (ex: IOException) {
            // Error occurred while creating the File
            ex.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            PermissionUtil.REQUEST_CAMERA -> {
                if(grantResults.isNotEmpty()){
                     if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                         grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                         grantResults[2] == PackageManager.PERMISSION_GRANTED){

                         capturePicture()

                     }else{
                         // permission denied
                         // to do rationale dialog
                     }
                }
            }
        }

    }

    override fun onBackPressed() {
        if (isPhotoEnlarged){
            resetEnlargePhotoVisibility()
            return
        }

        super.onBackPressed()

    }

}