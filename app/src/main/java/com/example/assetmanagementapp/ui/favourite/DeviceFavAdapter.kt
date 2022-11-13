package com.example.assetmanagementapp.ui.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.databinding.ItemLayoutDeviceBinding
import com.example.assetmanagementapp.databinding.LayoutItemLoadMoreBinding

class DeviceFavAdapter : BaseListAdapter<DeviceItem>(diffUtil) {

    var onClick: (DeviceItem) -> Unit = {}
    var onSaveItem: (DeviceItem) -> Unit = {}
    var isHideFavouriteButton = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder =
        if (viewType == NORMAL_ITEM) {
            ViewHolder(
                ItemLayoutDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    override fun onBindViewHolder(
        holder: BaseItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(currentList[position])
        } else {
            payloads.forEach { payload ->
                when (payload) {
                    SAVE_CHANGED -> {
                        (holder as ViewHolder).updateSavedStateFavourite(
                            currentList[position].isFavourite
                        )
                    }
                    else -> holder.bind(currentList[position])
                }
            }
        }
    }

    inner class ViewHolder(private val binding: ItemLayoutDeviceBinding) :
        BaseItemViewHolder(binding.root) {
        init {
            binding.apply {
                root.setSafeOnClickListener {
                    onClick.invoke(getItem(adapterPosition))
                }
                ivFavourite.setSafeOnClickListener {
                    onSaveItem.invoke(getItem(adapterPosition))
                }
            }
        }

        override fun bind(data: DeviceItem) {
            binding.data = data
            if (isHideFavouriteButton) {
                binding.ivFavourite.visibility = View.GONE
            }
            binding.ivFavourite.apply {
                isSelected = data.isFavourite
            }
        }

        fun updateSavedStateFavourite(isFavourite: Boolean) {
            binding.ivFavourite.isSelected = isFavourite
        }
    }

    inner class LoadingViewHolder(binding: LayoutItemLoadMoreBinding) :
        BaseItemViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).id == 0) {
            LOADING_TYPE
        } else {
            NORMAL_ITEM
        }
    }

    companion object {
        const val NORMAL_ITEM = 1
        const val LOADING_TYPE = 2
        val diffUtil = object : BaseDiffUtilItemCallback<DeviceItem>() {
            override fun getChangePayload(oldItem: DeviceItem, newItem: DeviceItem): Any? {
                if (oldItem.isFavourite != newItem.isFavourite) {
                    return SAVE_CHANGED
                }
                return super.getChangePayload(oldItem, newItem)
            }

            override fun areItemsTheSame(oldItem: DeviceItem, newItem: DeviceItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DeviceItem, newItem: DeviceItem): Boolean {
                return oldItem == newItem
            }
        }
        const val SAVE_CHANGED = "SAVE_CHANGED"
    }
}
