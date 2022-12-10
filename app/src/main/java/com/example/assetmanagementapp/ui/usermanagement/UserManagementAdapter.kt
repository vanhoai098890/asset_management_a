package com.example.assetmanagementapp.ui.usermanagement

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.databinding.LayoutItemUserManagementBinding
import com.example.assetmanagementapp.utils.bindImageAvatar

class UserManagementAdapter : BaseListAdapter<UserInfo>() {

    var onItemClick: (UserInfo) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        return ViewHolder(
            LayoutItemUserManagementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(private val binding: LayoutItemUserManagementBinding) :
        BaseItemViewHolder(binding.root) {
        override fun bind(data: UserInfo) {
            binding.apply {
                tvNameUser.text = data.username
                tvPhoneNumber.text = data.phoneNumber
                ivEditUser.setSafeOnClickListener {
                    onItemClick.invoke(getItem(adapterPosition))
                }
                ivAvatar.bindImageAvatar(data.avatarId)
            }
        }
    }
}
