package com.example.assetmanagementapp.ui.requestfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.data.remote.api.model.request.ReceivedRequestItem
import com.example.assetmanagementapp.databinding.LayoutItemRequestBinding

class RequestAdapter : BaseListAdapter<ReceivedRequestItem>() {

    var onClickItem: (ReceivedRequestItem) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        return ReceivedRequestViewHolder(
            LayoutItemRequestBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    inner class ReceivedRequestViewHolder(val binding: LayoutItemRequestBinding) :
        BaseItemViewHolder(binding.root) {
        override fun bind(data: ReceivedRequestItem) {
            binding.data = data
            binding.root.setSafeOnClickListener {
                onClickItem.invoke(data)
            }
        }
    }
}
