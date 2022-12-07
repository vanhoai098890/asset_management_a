package com.example.assetmanagementapp.ui.categorysheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.databinding.LayoutItemCategoryBinding

class CategoryAdapter : BaseListAdapter<TypeAsset>(diffUtil) {
    var onClick: (TypeAsset) -> Unit = {}
    var isShowNumberAsset: Boolean = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        return ViewHolder(
            LayoutItemCategoryBinding.inflate(
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
            (holder as ViewHolder).updateSelected(getItem(position).isSelected)
        }
    }

    inner class ViewHolder(private val binding: LayoutItemCategoryBinding) :
        BaseItemViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(data: TypeAsset) {
            if (data.isSelected) {
                binding.tvCategory.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    binding.root.context.resources.getDrawable(
                        R.drawable.ic_baseline_check_24,
                        null
                    ),
                    null
                )
            }
            binding.data = data
            if (isShowNumberAsset) {
                binding.tvNumberOfAssets.visibility = View.VISIBLE
                binding.tvNumberOfAssets.text = "${data.numberOfAssets}"
            } else {
                binding.tvNumberOfAssets.visibility = View.GONE
            }
            binding.root.setSafeOnClickListener {
                onClick.invoke(data)
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun updateSelected(isSelected: Boolean) {
            if (isSelected) {
                binding.tvCategory.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    binding.root.context.resources.getDrawable(
                        R.drawable.ic_baseline_check_24,
                        null
                    ),
                    null
                )
            } else {
                binding.tvCategory.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }
        }
    }

    companion object {
        val diffUtil = object : BaseDiffUtilItemCallback<TypeAsset>() {
            override fun getChangePayload(oldItem: TypeAsset, newItem: TypeAsset): Any? {
                if (oldItem.isSelected != newItem.isSelected) {
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
