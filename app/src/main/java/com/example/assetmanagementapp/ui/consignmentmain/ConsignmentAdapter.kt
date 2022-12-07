package com.example.assetmanagementapp.ui.consignmentmain

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import com.example.assetmanagementapp.databinding.ItemLayoutConsignmentBinding

class ConsignmentAdapter : BaseListAdapter<ConsignmentItem>() {

    var onClick: (ConsignmentItem) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder =
        ViewHolder(
            ItemLayoutConsignmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    inner class ViewHolder(private val binding: ItemLayoutConsignmentBinding) :
        BaseItemViewHolder(binding.root) {
        init {
            binding.apply {
                root.setSafeOnClickListener {
                    onClick.invoke(getItem(adapterPosition))
                }
            }
        }

        override fun bind(data: ConsignmentItem) {
            binding.data = data
            binding.tvNumberOfAssets.text = "${data.number}"
        }
    }
}
