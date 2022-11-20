package com.example.assetmanagementapp.ui.department

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentItem
import com.example.assetmanagementapp.databinding.LayoutItemDepartmentBinding

class DepartmentAdapter : BaseListAdapter<DepartmentItem>() {

    var onClick: (DepartmentItem) -> Unit = {}

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

        override fun bind(data: DepartmentItem) {
            binding.apply {
                tvNameRoom.text =
                    root.resources.getString(R.string.name_department, data.departmentName)
                tvNumberOfRooms.text =
                    root.resources.getString(R.string.number_of_rooms_d, data.numberOfRooms)
                tvNumberOfAssets.text =
                    root.resources.getString(R.string.number_of_assets_d, data.numberOfAssets)
            }
        }
    }
}
