package com.example.assetmanagementapp.ui.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.notification.NotificationItem
import com.example.assetmanagementapp.data.remote.api.model.notification.StateDownload
import com.example.assetmanagementapp.databinding.LayoutItemNotificationBinding

class NotificationAdapter : BaseListAdapter<NotificationItem>(diffUtil) {

    var onDownloadClick: (NotificationItem) -> Unit = {}
    var onEditClick: (NotificationItem) -> Unit = {}
    var isAdmin: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        return ViewHolder(
            LayoutItemNotificationBinding.inflate(
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
            super.onBindViewHolder(holder, position, payloads)
        } else {
            (holder as ViewHolder).apply {
                updateStateLoading(getItem(position))
                updateStateUploading(getItem(position))
            }
        }
    }

    inner class ViewHolder(private val binding: LayoutItemNotificationBinding) :
        BaseItemViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bind(data: NotificationItem) {
            binding.apply {
                if (data.typeNotification == 0) {
                    tvTitleNotification.text =
                        root.resources.getString(R.string.property_assessment)
                    tvRoomName.text = root.resources.getString(
                        R.string.title_detail_notification,
                        data.departmentName,
                        data.roomName
                    )
                    tvRoomName.visibility = View.VISIBLE
                } else {
                    tvTitleNotification.text = root.resources.getString(
                        R.string.asset_liquidation
                    )
                    tvRoomName.visibility = View.INVISIBLE
                }
                tvTime.text = data.time.substring(0, 10)
                ivDownload.setSafeOnClickListener {
                    onDownloadClick.invoke(getItem(adapterPosition))
                }
                updateStateUploading(data)
                updateStateLoading(data)
                if (data.isUpdated) {
                    ivEdit.visibility = View.GONE
                }
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun updateStateUploading(data: NotificationItem) {
            binding.apply {
                when (data.stateUpdating) {
                    null -> {
                        ivEdit.setImageDrawable(
                            root.resources.getDrawable(
                                R.drawable.ic_baseline_edit_24,
                                null
                            )
                        )
                        ivEdit.visibility = View.VISIBLE
                        progressbarUploading.visibility = View.GONE
                        ivEdit.isClickable = true
                    }
                    StateDownload.DEFAULT -> {
                        ivEdit.setImageDrawable(
                            root.resources.getDrawable(
                                R.drawable.ic_baseline_edit_24,
                                null
                            )
                        )
                        ivEdit.visibility = View.VISIBLE
                        progressbarUploading.visibility = View.GONE
                        ivEdit.isClickable = true
                    }
                    StateDownload.UPLOADING -> {
                        ivEdit.visibility = View.INVISIBLE
                        ivEdit.isClickable = false
                        progressbarUploading.visibility = View.VISIBLE
                    }
                    StateDownload.ERROR -> {
                        ivEdit.setImageDrawable(
                            root.resources.getDrawable(
                                R.drawable.ic_close_red,
                                null
                            )
                        )
                        ivEdit.visibility = View.VISIBLE
                        progressbarUploading.visibility = View.GONE
                        ivEdit.isClickable = true
                    }
                    StateDownload.SUCCESS -> {
                        ivEdit.setImageDrawable(
                            root.resources.getDrawable(
                                R.drawable.ic_baseline_check_24,
                                null
                            )
                        )
                        ivEdit.visibility = View.VISIBLE
                        progressbarUploading.visibility = View.GONE
                        ivEdit.isClickable = false
                    }
                    else -> {}
                }
                when {
                    !isAdmin -> {
                        ivEdit.visibility = View.GONE
                    }
                    else -> {
                        ivEdit.visibility = View.VISIBLE
                        ivEdit.setSafeOnClickListener {
                            onEditClick.invoke(getItem(adapterPosition))
                        }
                    }
                }
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun updateStateLoading(data: NotificationItem) {
            binding.apply {
                when (data.stateLoading) {
                    null -> {
                        ivDownload.setImageDrawable(
                            root.resources.getDrawable(
                                R.drawable.ic_baseline_file_download_24,
                                null
                            )
                        )
                        ivDownload.visibility = View.VISIBLE
                        progressbarDownload.visibility = View.GONE
                        ivDownload.isClickable = true
                    }
                    StateDownload.DEFAULT -> {
                        ivDownload.setImageDrawable(
                            root.resources.getDrawable(
                                R.drawable.ic_baseline_file_download_24,
                                null
                            )
                        )
                        ivDownload.visibility = View.VISIBLE
                        progressbarDownload.visibility = View.GONE
                        ivDownload.isClickable = true
                    }
                    StateDownload.LOADING -> {
                        ivDownload.visibility = View.INVISIBLE
                        ivDownload.isClickable = false
                        progressbarDownload.visibility = View.VISIBLE
                    }
                    StateDownload.ERROR -> {
                        ivDownload.setImageDrawable(
                            root.resources.getDrawable(
                                R.drawable.ic_close_red,
                                null
                            )
                        )
                        ivDownload.visibility = View.VISIBLE
                        progressbarDownload.visibility = View.GONE
                        ivDownload.isClickable = true
                    }
                    StateDownload.SUCCESS -> {
                        ivDownload.setImageDrawable(
                            root.resources.getDrawable(
                                R.drawable.ic_baseline_check_24,
                                null
                            )
                        )
                        ivDownload.visibility = View.VISIBLE
                        progressbarDownload.visibility = View.GONE
                        ivDownload.isClickable = false
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        private const val STATE_LOADING_CHANGE = "STATE_LOADING_CHANGE"
        private const val STATE_UP_LOADING_CHANGE = "STATE_UP_LOADING_CHANGE"
        val diffUtil = object : BaseDiffUtilItemCallback<NotificationItem>() {
            override fun getChangePayload(
                oldItem: NotificationItem,
                newItem: NotificationItem
            ): Any? {
                if (oldItem.stateLoading != newItem.stateLoading) {
                    return STATE_LOADING_CHANGE
                } else if (oldItem.stateUpdating != newItem.stateUpdating) {
                    return STATE_UP_LOADING_CHANGE
                }
                return super.getChangePayload(oldItem, newItem)
            }

            override fun areItemsTheSame(
                oldItem: NotificationItem,
                newItem: NotificationItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotificationItem,
                newItem: NotificationItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
