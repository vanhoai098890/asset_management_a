package com.example.assetmanagementapp.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.databinding.LayoutItemAdminCategoryBinding

class CategoryAdminAdapter : BaseListAdapter<TypeAsset>(diffUtil) {

    var onItemClick: (TypeAsset) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        return ViewHolder(
            LayoutItemAdminCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BaseItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            (holder as ViewHolder).updateTextChange(getItem(position).typeName)
        }
    }


    inner class ViewHolder(private val binding: LayoutItemAdminCategoryBinding) :
        BaseItemViewHolder(binding.root) {
        override fun bind(data: TypeAsset) {
            binding.apply {
                tvNameCategory.text = data.typeName
                ivEditCategory.setSafeOnClickListener {
                    onItemClick.invoke(getItem(adapterPosition))
                }
            }
        }

        fun updateTextChange(categoryName: String) {
            binding.tvNameCategory.text = categoryName
        }
    }

    companion object {
        val diffUtil = object : BaseDiffUtilItemCallback<TypeAsset>() {
            override fun getChangePayload(oldItem: TypeAsset, newItem: TypeAsset): Any? {
                if (oldItem.typeName != newItem.typeName) {
                    return SAVE_CHANGED
                }
                return super.getChangePayload(oldItem, newItem)
            }

            override fun areItemsTheSame(oldItem: TypeAsset, newItem: TypeAsset): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TypeAsset, newItem: TypeAsset): Boolean {
                return oldItem == newItem
            }
        }
        const val SAVE_CHANGED = "SAVE_CHANGED"
    }
}
