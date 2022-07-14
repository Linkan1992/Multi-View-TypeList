package com.linkan.multiviewtypelist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.linkan.multiviewtypelist.adapter.SingleChoiceChildAdapter.*
import com.linkan.multiviewtypelist.databinding.ItemChildSingleChoiceBinding
import com.linkan.multiviewtypelist.dto.ChoiceModel
import com.linkan.multiviewtypelist.dto.DataMapModel

class SingleChoiceChildAdapter(private val callback: SingleChoiceCallback) : ListAdapter<ChoiceModel, SingleChoiceViewHolder>(ChoiceModelDiffCallback())  {

    var oldItemSelectedPosition = -1
    var newItemSelectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): SingleChoiceViewHolder =
        SingleChoiceViewHolder(
            ItemChildSingleChoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SingleChoiceViewHolder, position: Int, ) {
        val choiceModel : ChoiceModel = getItem(position)
        holder.bind(choiceModel)
    }

    inner class SingleChoiceViewHolder constructor(private val binding: ItemChildSingleChoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(choiceModel: ChoiceModel) {

            binding.executePendingBindings()
            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.

            // single choice select logic is pending

         /*   binding.choiceIcon.setOnCheckedChangeListener { button, checked ->

                if (checked){
                    // updating new selected position
                    callback.getDataMapModel()?.newItemSelectedPosition = adapterPosition

                    for (item in currentList){
                        item.isSelected = false

                        if(item == choiceModel) {
                            callback.getDataMapModel()?.newItemSelectedPosition = adapterPosition
                            item.isSelected = !item.isSelected
                        }
                    }

                    callback.notifyItemChangedToParent()
                }
            }*/

                binding.choiceIcon.setOnClickListener {

                   // itemView.setTag(binding.choiceIcon.id, true)

                    // updating new selected position
                    val dataModel = callback.getDataMapModel()
                    val list = dataModel?.choiceList
                    val oldSelectedPosition = dataModel?.newItemSelectedPosition

                    dataModel?.newItemSelectedPosition = adapterPosition

                    list?.let{ choiceList ->
                        for (item in choiceList){
                            item.isSelected = false

                            if(item.choiceText == choiceModel.choiceText) {
                                callback.getDataMapModel()?.newItemSelectedPosition = adapterPosition
                                item.isSelected = true
                            }
                        }
                    }

                    notifyDataSetChanged()

                //    callback.notifyItemChangedToParent()

                }

       /*     binding.choiceIcon.setOnClickListener {

                    *//*oldItemSelectedPosition = newItemSelectedPosition

                    for (item in currentList){
                        item.isSelected = false

                        if(item == choiceModel) {
                            newItemSelectedPosition = adapterPosition
                            item.isSelected = !item.isSelected
                        }
                    }

                    notifyItemChanged(oldItemSelectedPosition)
                    notifyItemChanged(newItemSelectedPosition)*//*

                  *//*callback.getDataMapModel()?.also { dataMapModel ->
                      dataMapModel.oldItemSelectedPosition = dataMapModel.newItemSelectedPosition

                      for (item in currentList){
                          item.isSelected = false

                          if(item == choiceModel) {
                              dataMapModel.newItemSelectedPosition = adapterPosition
                              item.isSelected = !item.isSelected
                          }
                      }

                      notifyItemChanged(dataMapModel.oldItemSelectedPosition)
                      notifyItemChanged(dataMapModel.newItemSelectedPosition)
                  }*//*

                  binding.choiceIcon.isChecked
                  callback.getDataMapModel()?.newItemSelectedPosition

                  callback.notifyItemChangedToParent()
            }*/

            binding.choiceIcon.isChecked = choiceModel.isSelected
           // binding.choiceIcon.isChecked = adapterPosition == callback.getDataMapModel()?.newItemSelectedPosition
            binding.choiceTitle.text = choiceModel.choiceText
        }
    }

}

interface SingleChoiceCallback{

    fun notifyItemChangedToParent()

    fun getDataMapModel() : DataMapModel?

}

class ChoiceModelDiffCallback : DiffUtil.ItemCallback<ChoiceModel>() {
    override fun areItemsTheSame(oldItem: ChoiceModel, newItem: ChoiceModel): Boolean {
        return oldItem.choiceText == newItem.choiceText
    }

    override fun areContentsTheSame(oldItem: ChoiceModel, newItem: ChoiceModel): Boolean {
        return oldItem == newItem
    }
}