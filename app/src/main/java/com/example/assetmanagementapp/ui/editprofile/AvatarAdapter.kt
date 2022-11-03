package com.example.assetmanagementapp.ui.editprofile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.databinding.ItemAvatarBinding
import com.example.assetmanagementapp.utils.bindImageAvatar

class AvatarAdapter : BaseListAdapter<Avatars>() {
    var avatarSelect: Avatars? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        return ItemHolder(
            ItemAvatarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ItemHolder(val binding: ItemAvatarBinding) : BaseItemViewHolder(binding.root) {
        init {
            val height =
                ((binding.root.context?.resources?.displayMetrics?.widthPixels ?: 0) * 0.28).toInt()
            binding.apply {
                mainAvatar.layoutParams.height = height
                mainAvatar.layoutParams.width = height
            }
        }

        override fun bind(data: Avatars) {
            super.bind(data)
            binding.apply {
                binding.avatar.bindImageAvatar(data.numberImage)
                if (data.isSelect) {
                    avatarSelect = data
                    imgCheckAvatar.visibility = View.VISIBLE
                    flAvatar.isSelected = true
                } else {
                    imgCheckAvatar.visibility = View.GONE
                    flAvatar.isSelected = false
                }
                imgCheckAvatar.visibility = if (data.isSelect) View.VISIBLE else View.GONE
                root.setSafeOnClickListener {
                    //display check icon avatar select
                    resetCheckOldAvatar()
                    data.isSelect = true
                    avatarSelect = data
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    private fun resetCheckOldAvatar() {
        currentList.forEachIndexed { index, avatars ->
            avatars.isSelect = false
            notifyItemChanged(index)
        }
    }
}
