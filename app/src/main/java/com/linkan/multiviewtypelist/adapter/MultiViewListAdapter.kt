package com.linkan.multiviewtypelist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.linkan.multiviewtypelist.databinding.ItemCommentTypeRowBinding
import com.linkan.multiviewtypelist.databinding.ItemPhotoTypeRowBinding
import com.linkan.multiviewtypelist.databinding.ItemSingleChoiceRowBinding
import com.linkan.multiviewtypelist.dto.ItemModel
import com.linkan.multiviewtypelist.util.ViewType

class MultiViewListAdapter :
    ListAdapter<ItemModel, MultiViewListAdapter.BaseViewHolder>(ItemModelDiffCallback()) {

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

    class PhotoViewHolder constructor(private val binding: ItemPhotoTypeRowBinding) :
        BaseViewHolder(binding) {

        override fun bind(item: ItemModel) {

            binding.executePendingBindings()
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
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

            binding.executePendingBindings()
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
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

class ItemModelDiffCallback : DiffUtil.ItemCallback<ItemModel>() {
    override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem == newItem
    }
}