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


            binding.choiceIcon.setOnClickListener {

                /*
                 * Below commented code is working solution - 1
                 * It's a Naive solution which iterates over entire child list
                 * make other item selection false other than current
                 */

                /*  callback.getDataMapModel()?.also { dataMapModel ->
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
                  }*/


                /*
                * Below commented code is working solution - 2
                * It's a Naive solution which iterates over entire child list
                * make other item selection false other than current
                */

                callback.getDataMapModel()?.also { dataMapModel ->
                    // updating old position
                    dataMapModel.oldItemSelectedPosition = dataMapModel.newItemSelectedPosition
                    // updating new position
                    dataMapModel.newItemSelectedPosition = adapterPosition

                    val itemList = currentList

                    if (dataMapModel.oldItemSelectedPosition > -1){

                        itemList[dataMapModel.oldItemSelectedPosition].isSelected = false

                        notifyItemChanged(dataMapModel.oldItemSelectedPosition)
                    }

                    itemList[dataMapModel.newItemSelectedPosition].isSelected = true
                    notifyItemChanged(dataMapModel.newItemSelectedPosition)
                }

            }

            binding.choiceIcon.isChecked = choiceModel.isSelected
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