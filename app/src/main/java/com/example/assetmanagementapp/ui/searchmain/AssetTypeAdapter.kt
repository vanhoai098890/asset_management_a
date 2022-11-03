package com.example.assetmanagementapp.ui.searchmain

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.data.remote.api.model.device.ItemDeviceData
import com.example.assetmanagementapp.databinding.LayoutItemLoadMoreBinding
import com.example.assetmanagementapp.databinding.LayoutItemRecycleDeviceBinding

class AssetTypeAdapter : BaseListAdapter<ItemDeviceData>() {

    var onItemClick: (ItemDeviceData) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder =
        if (viewType == NORMAL_ITEM) {
            ViewHolder(
                LayoutItemRecycleDeviceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            LoadingViewHolder(
                LayoutItemLoadMoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    inner class ViewHolder(private val binding: LayoutItemRecycleDeviceBinding) :
        BaseItemViewHolder(binding.root) {
        override fun bind(data: ItemDeviceData) {
            binding.data = data
            binding.root.setSafeOnClickListener {
                onItemClick.invoke(data)
            }
        }
    }

    inner class LoadingViewHolder(binding: LayoutItemLoadMoreBinding) :
        BaseItemViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).image.isBlank()) {
            LOADING_TYPE
        } else {
            NORMAL_ITEM
        }
    }

    companion object {
        const val NORMAL_ITEM = 1
        const val LOADING_TYPE = 2
    }
}
