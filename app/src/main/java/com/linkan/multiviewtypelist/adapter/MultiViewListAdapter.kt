package com.linkan.multiviewtypelist.adapter

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.linkan.multiviewtypelist.R
import com.linkan.multiviewtypelist.databinding.ItemCommentTypeRowBinding
import com.linkan.multiviewtypelist.databinding.ItemPhotoTypeRowBinding
import com.linkan.multiviewtypelist.databinding.ItemSingleChoiceRowBinding
import com.linkan.multiviewtypelist.dto.ItemModel
import com.linkan.multiviewtypelist.util.ViewType
import java.io.File

class MultiViewListAdapter(private val itemClickedCallback: ItemClickedCallback) :
    ListAdapter<ItemModel, MultiViewListAdapter.BaseViewHolder>(ItemModelDiffCallback()) {

    private val selectedItemLiveData : MutableLiveData<Int> = MutableLiveData()

    val mSelectedItemLiveData : MutableLiveData<Int>
        get() = selectedItemLiveData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            ViewType.TYPE_PHOTO_VIEW -> PhotoViewHolder(
                ItemPhotoTypeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            ViewType.TYPE_SINGLE_CHOICE_VIEW -> SingleChoiceViewHolder(
                ItemSingleChoiceRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            ViewType.TYPE_COMMENT_VIEW -> CommentViewHolder(
                ItemCommentTypeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> PhotoViewHolder(
                ItemPhotoTypeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val itemModel : ItemModel = getItem(position)
        holder.bind(itemModel)
    }

    inner class PhotoViewHolder constructor(private val binding: ItemPhotoTypeRowBinding) :
        BaseViewHolder(binding) {

        override fun bind(item: ItemModel) {

            binding.executePendingBindings()
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.

            binding.tvTitle.text = item.title

            val photoPathFile = item.dataMapModel?.photoPath.run { File(this ?: "") }

            Glide.with(itemView)
               // .load(item.dataMapModel?.photoPath) // Uri of the picture
                .load(Uri.fromFile(photoPathFile))
                .centerCrop()
                .placeholder(R.drawable.dummy_profile)
                .into(binding.imgvPhoto)

            binding.imgvClose.visibility = if (!TextUtils.isEmpty(item.dataMapModel?.photoPath))
                View.VISIBLE else View.GONE

            binding.imgvClose.setOnClickListener {
                item.dataMapModel?.photoPath = null
                notifyItemChanged(adapterPosition)
            }

            binding.imgvPhoto.setOnClickListener {

                selectedItemLiveData.postValue(adapterPosition)

                if (!TextUtils.isEmpty(item.dataMapModel?.photoPath))
                    itemClickedCallback.enlargePhoto(item)
                else
                    itemClickedCallback.capturePhoto(adapterPosition, item)

            }
        }
    }

    class SingleChoiceViewHolder constructor(private val binding: ItemSingleChoiceRowBinding) :
        BaseViewHolder(binding) {

        override fun bind(item: ItemModel) {

            binding.executePendingBindings()
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
        }

    }

    class CommentViewHolder constructor(private val binding: ItemCommentTypeRowBinding) :
        BaseViewHolder(binding) {

        override fun bind(item: ItemModel) {

            binding.item = ItemCommentViewModel(item)

            binding.executePendingBindings()
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.

            binding.tvTitle.text = item.title
        }
    }

 /*   abstract class BaseViewHolder constructor(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        open fun bind(item: ItemModel) {

            //  binding.pojo = item

            binding.executePendingBindings()
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.

        }

    }*/

    abstract class BaseViewHolder constructor(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        abstract fun bind(item : ItemModel)

    }

    override fun getItemViewType(position: Int): Int {
        return ViewType.getItemViewType(getItem(position).viewType)
    }

}

interface ItemClickedCallback{

    fun capturePhoto(position : Int, item : ItemModel)

    fun enlargePhoto(item : ItemModel)

}

class ItemModelDiffCallback : DiffUtil.ItemCallback<ItemModel>() {
    override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem == newItem
    }
}