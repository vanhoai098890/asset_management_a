package com.example.assetmanagementapp.ui.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItem
import com.example.assetmanagementapp.databinding.LayoutItemDepartmentBinding

class RoomAdapter() : BaseListAdapter<RoomItem>() {

    var onClick: (RoomItem) -> Unit = {}
    var onAddNotification: (RoomItem) -> Unit = {}
    var isShowAddNotification: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder =
        ViewHolder(
            LayoutItemDepartmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    inner class ViewHolder(private val binding: LayoutItemDepartmentBinding) :
        BaseItemViewHolder(binding.root) {
        init {
            binding.apply {
                root.setSafeOnClickListener {
                    onClick.invoke(getItem(adapterPosition))
                }
            }
        }

        override fun bind(data: RoomItem) {
            binding.apply {
                tvNameRoom.text = data.roomName
                tvNumberOfRooms.visibility = View.GONE
                tvNumberOfAssets.text =
                    root.resources.getString(R.string.number_of_assets_d, data.numberOfAssets)
                if (isShowAddNotification) {
                    ivAddNotification.visibility = View.VISIBLE
                    ivAddNotification.setSafeOnClickListener {
                        onAddNotification.invoke(getItem(adapterPosition))
                    }
                } else {
                    ivAddNotification.visibility = View.GONE
                }
            }
        }
    }
}
