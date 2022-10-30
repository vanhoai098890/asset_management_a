package com.example.assetmanagementapp.ui.searchmain

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.data.remote.api.model.device.ItemDeviceData
import com.example.assetmanagementapp.databinding.LayoutItemRecycleDeviceBinding

class AssetTypeAdapter : BaseListAdapter<ItemDeviceData>() {

    var onItemClick: (ItemDeviceData) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder =
        ViewHolder(
            LayoutItemRecycleDeviceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class ViewHolder(private val binding: LayoutItemRecycleDeviceBinding) :
        BaseItemViewHolder(binding.root) {
        override fun bind(data: ItemDeviceData) {
            binding.data = data
            binding.root.setSafeOnClickListener {
                onItemClick.invoke(data)
            }
        }
    }
}
