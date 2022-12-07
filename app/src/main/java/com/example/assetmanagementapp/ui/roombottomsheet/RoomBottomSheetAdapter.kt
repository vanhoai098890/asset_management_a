package com.example.assetmanagementapp.ui.roombottomsheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.provider.ProviderItem
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItem
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.databinding.LayoutItemCategoryBinding

class RoomBottomSheetAdapter : BaseListAdapter<RoomItem>() {
    var onClick: (RoomItem) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        return ViewHolder(
            LayoutItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(private val binding: LayoutItemCategoryBinding) :
        BaseItemViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(data: RoomItem) {
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
            binding.data = TypeAsset(typeName = data.roomName, id = 0)
            binding.tvNumberOfAssets.visibility = View.GONE
            binding.root.setSafeOnClickListener {
                onClick.invoke(data)
            }
        }
    }
}
