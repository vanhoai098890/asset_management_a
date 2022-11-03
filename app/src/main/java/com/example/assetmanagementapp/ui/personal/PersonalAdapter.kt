package com.example.assetmanagementapp.ui.personal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.databinding.ItemPersonalContentFunctionBinding
import com.example.assetmanagementapp.databinding.LayoutItemPersonalTitleBinding
import com.example.assetmanagementapp.databinding.LayoutItemTopPersonalBinding

class PersonalAdapter : BaseListAdapter<PersonalFunctionStatic>(diffCallBack = diffUtil) {

    internal var onClickItem: (PersonalFunctionStatic) -> Unit = {}
    internal var onEditProfile: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        return when (viewType) {
            TOP_PERSON -> {
                TopPersonViewHolder(
                    LayoutItemTopPersonalBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            CONTENT -> {
                ContentViewHolder(
                    ItemPersonalContentFunctionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                TitleViewHolder(
                    LayoutItemPersonalTitleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(
        holder: BaseItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(data = getItem(position))
        } else {
            payloads.forEach { payload ->
                when (payload) {
                    TOP_INFO_CHANGE -> {
                        (holder as TopPersonViewHolder).updateInfoChange(
                            currentList[position]
                        )
                    }
                    else -> holder.bind(currentList[position])
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    inner class ContentViewHolder(val binding: ItemPersonalContentFunctionBinding) :
        BaseItemViewHolder(binding.root) {

        override fun bind(data: PersonalFunctionStatic) {
            binding.apply {
                val margin13dp = root.context.resources.getDimension(R.dimen.margin_13dp).toInt()
                val margin16dp = root.context.resources.getDimension(R.dimen.margin_16dp).toInt()
                this.data = data
                when (data) {
                    PersonalFunctionStatic.PASSWORD, PersonalFunctionStatic.REQUEST -> {
                        ivDivider.visibility = View.INVISIBLE
                    }
                    PersonalFunctionStatic.LOGOUT -> {
                        ivDivider.visibility = View.INVISIBLE
                        imgPersonalTabItemMiddleArrowNext.visibility = View.GONE
                        val marginLayoutParams = ViewGroup.MarginLayoutParams(root.layoutParams)
                        marginLayoutParams.setMargins(
                            margin16dp,
                            margin13dp,
                            margin16dp,
                            margin13dp
                        )
                        root.layoutParams = marginLayoutParams
                    }
                    else -> {}
                }
                initOnclickItem(data, binding)
            }
        }

        private fun initOnclickItem(
            name: PersonalFunctionStatic,
            binding: ItemPersonalContentFunctionBinding
        ) {
            when (name) {
                PersonalFunctionStatic.LOGOUT, PersonalFunctionStatic.PERSONAL, PersonalFunctionStatic.REQUEST -> {
                    binding.root.setSafeOnClickListener {
                        onClickItem.invoke(name)
                    }
                }
                else -> {
                    binding.apply {
                        root.setSafeOnClickListener {
                            Toast.makeText(
                                root.context,
                                root.context.getString(data?.stringRes ?: 0),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    inner class TitleViewHolder(val binding: LayoutItemPersonalTitleBinding) :
        BaseItemViewHolder(binding.root) {

        override fun bind(data: PersonalFunctionStatic) {
            binding.data = data
        }
    }

    inner class TopPersonViewHolder(val binding: LayoutItemTopPersonalBinding) :
        BaseItemViewHolder(binding.root) {
        init {
            binding.btnEdit.setSafeOnClickListener {
                onEditProfile.invoke()
            }
        }

        override fun bind(data: PersonalFunctionStatic) {
            (data as? PersonalFunctionStatic.PERSON)?.apply {
                binding.data = UserInfo(avatarId = this.avatarId, username = this.username)
            }
        }

        fun updateInfoChange(data: PersonalFunctionStatic) {
            (data as? PersonalFunctionStatic.PERSON)?.apply {
                binding.data = UserInfo(avatarId = this.avatarId, username = this.username)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).isTopPerson) {
            return TOP_PERSON
        }
        if (getItem(position).title == AppConstant.ZERO) {
            return CONTENT
        }
        return TITLE
    }

    companion object {
        const val CONTENT = 0
        const val TITLE = 1
        const val TOP_PERSON = 2
        val diffUtil = object : BaseDiffUtilItemCallback<PersonalFunctionStatic>() {
            override fun getChangePayload(
                oldItem: PersonalFunctionStatic,
                newItem: PersonalFunctionStatic
            ): Any? {
                if (oldItem is PersonalFunctionStatic.PERSON && newItem is PersonalFunctionStatic.PERSON) {
                    if ((oldItem.avatarId != newItem.avatarId) || (oldItem.username != newItem.username)) {
                        return TOP_INFO_CHANGE
                    }
                }
                return super.getChangePayload(oldItem, newItem)
            }

            override fun areItemsTheSame(
                oldItem: PersonalFunctionStatic,
                newItem: PersonalFunctionStatic
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PersonalFunctionStatic,
                newItem: PersonalFunctionStatic
            ): Boolean {
                if (oldItem is PersonalFunctionStatic.PERSON && newItem is PersonalFunctionStatic.PERSON) {
                    return (oldItem.avatarId == newItem.avatarId) && (oldItem.username == newItem.username)
                }
                return true
            }
        }
        const val TOP_INFO_CHANGE = "TOP_INFO_CHANGE"
    }
}
