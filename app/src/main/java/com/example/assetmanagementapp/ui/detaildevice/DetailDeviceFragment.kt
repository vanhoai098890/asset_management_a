package com.example.assetmanagementapp.ui.detaildevice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.ui.snackbar.CustomSnackBar
import com.example.app_common.utils.LogUtils
import com.example.app_common.utils.ScreenUtils
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.data.remote.api.model.favourite.StatusDevice
import com.example.assetmanagementapp.data.remote.api.model.favourite.StatusTicket
import com.example.assetmanagementapp.databinding.DeviceDetailFragmentBinding
import com.example.assetmanagementapp.databinding.LayoutItemInfoDeviceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailDeviceFragment private constructor() : BaseFragment() {

    private lateinit var binding: DeviceDetailFragmentBinding
    private val viewModel: DetailDeviceViewModel by viewModels()
    var onBackPress: (Int, Boolean) -> Unit = { _, _ -> }

    private val customSnackBar: CustomSnackBar by lazy {
        CustomSnackBar.make(
            parent = binding.layoutParent,
            message = ""
        ).apply {
            setContentMargin(
                activityContext = context,
                left = null,
                right = null,
                bottom = ScreenUtils.toPx(context, 80f)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeviceDetailFragmentBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            data = viewModel
        }
        initData()
        initEvent()
        return binding.root
    }

    private fun initData() {
        arguments?.apply {
            viewModel.currentState.deviceId = getInt(DEVICE_ID)
        }
        binding.apply {
            toolbarId.apply {
                tvCenter.text = getString(R.string.v1_device_details)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
                endIcon.apply {
                    setImageResource(R.drawable.bg_selector_bookmark)
                    visibility = View.VISIBLE
                    setSafeOnClickListener {
                        viewModel.currentState.deviceItem?.apply {
                            viewModel.saveDevices(id, !viewModel.currentState.stateIsFavourite)
                        }
                    }
                }
                ivMess.setSafeOnClickListener {
                    viewModel.currentState.deviceItem?.providerPhone?.let { phoneNumber ->
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.fromParts("sms", phoneNumber, null)
                            )
                        )
                    }
                }
                ivPhone.setSafeOnClickListener {
                    viewModel.currentState.deviceItem?.providerPhone?.let { phoneNumber ->
                        startActivity(
                            Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel: $phoneNumber")
                            )
                        )
                    }
                }
            }
        }
    }


    private fun initEvent() {
        viewModel.getDetailDevice()
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.loadingState().collect {
                    handleShowLoadingDialog(it)
                }
            }
        }
        viewModel.store.apply {
            observe(
                owner = this@DetailDeviceFragment,
                selector = { state -> state.stateIsFavourite },
                observer = {
                    binding.toolbarId.endIcon.isSelected = it
                }
            )
            observe(
                owner = this@DetailDeviceFragment,
                selector = { currentState -> currentState.stateIsShowSnackBar },
                observer = {
                    it?.apply { showCustomSnackBar(it) }
                }
            )
            observe(
                owner = this@DetailDeviceFragment,
                selector = { currentState -> currentState.stateShowSomethingWrong },
                observer = { showSnackBarSomethingWrong(it) }
            )
            observe(
                owner = this@DetailDeviceFragment,
                selector = { currentState -> currentState.deviceItem },
                observer = {
                    it?.apply { handleShowUI(it) }
                }
            )
        }
    }

    private fun handleShowUI(deviceItem: DeviceItem) {
        handleShowButton(deviceItem)
        handleShowLayoutInfoDevice(deviceItem)
    }

    private fun handleShowLayoutInfoDevice(deviceItem: DeviceItem) {
        handleShowItemInfo(
            itemInfo = binding.itemCategory,
            nameInfo = deviceItem.typeAssetName,
            icon = R.drawable.ic_outline_category_24,
            detail = R.string.v1_category
        )
        handleShowItemInfo(
            itemInfo = binding.itemPrice,
            nameInfo = deviceItem.unitPrice.toString(),
            icon = R.drawable.icon_amount,
            detail = R.string.v1_unit_price
        )
        handleShowItemInfo(
            itemInfo = binding.itemNameConsignment,
            nameInfo = deviceItem.consignmentName,
            icon = R.drawable.ic_consigment,
            detail = R.string.v1_name_consignment
        )
        handleShowItemInfo(
            itemInfo = binding.itemDateWarranty,
            nameInfo = deviceItem.dateWarranty,
            icon = R.drawable.ic_warrenty,
            detail = R.string.v1_date_warranty
        )
        handleShowItemInfo(
            itemInfo = binding.itemDateManufacture,
            nameInfo = deviceItem.dateManufacture,
            icon = R.drawable.ic_manufacture,
            detail = R.string.v1_date_manufacture
        )
    }

    private fun handleShowItemInfo(
        itemInfo: LayoutItemInfoDeviceBinding,
        nameInfo: String,
        icon: Int,
        detail: Int
    ) {
        itemInfo.apply {
            tvService.text = nameInfo
            ivService.setImageResource(icon)
            ivService.contentDescription = getString(detail)
        }
    }

    private fun handleShowButton(deviceItem: DeviceItem) {
        binding.apply {
            deviceItem.apply {
                when {
                    (status == StatusDevice.FREE.statusName) && (statusTicket == StatusTicket.NEW.ordinal) -> {
                        btnRequest.visibility = View.VISIBLE
                    }
                    (status == StatusDevice.FREE.statusName) && (statusTicket == StatusTicket.IN_PROGRESS.ordinal) && isOwnTicket -> {
                        btnClose.visibility = View.VISIBLE
                    }
                    (status == StatusDevice.FREE.statusName) && (statusTicket == StatusTicket.IN_PROGRESS.ordinal) -> {
                        btnRequest.visibility = View.VISIBLE
                        btnRequest.isEnabled = false
                        tvMessage.text = getString(R.string.v1_ticket_is_in_progress)
                        tvMessage.visibility = View.VISIBLE
                    }
                    status == StatusDevice.BORROWED.statusName && isOwnTicket -> {
                        btnReturn.visibility = View.VISIBLE
                        btnReport.visibility = View.VISIBLE
                    }
                    status == StatusDevice.BORROWED.statusName -> {
                        btnReturn.visibility = View.VISIBLE
                        btnReturn.isEnabled = false
                        btnReport.visibility = View.VISIBLE
                        btnReport.isEnabled = false
                        tvMessage.text = getString(R.string.v1_ticket_was_borrowed)
                        tvMessage.visibility = View.VISIBLE
                    }
                    status == StatusDevice.WARRANTY.statusName || status == StatusDevice.DAMAGED.statusName -> {
                        btnRequest.visibility = View.VISIBLE
                        btnRequest.isEnabled = false
                        tvMessage.text = getString(R.string.v1_device_is_damaged_or_is_repairing)
                        tvMessage.visibility = View.VISIBLE
                    }
                    else -> {
                        LogUtils.d("Item have status not match with StatusDevice enum")
                    }
                }
            }
        }
    }

    private fun showCustomSnackBar(isFavourite: Boolean) {
        val message = getString(
            if (isFavourite) R.string.my_list_favorite_toast else R.string.my_list_un_favorite_toast
        )

        customSnackBar.setText(message)
        if (customSnackBar.isShown) {
            customSnackBar.refreshCounting()
        } else {
            customSnackBar.show()
        }
        viewModel.dispatchResetSnackBar()
    }

    private fun showSnackBarSomethingWrong(isWrong: Boolean) {
        if (isWrong) {
            val message = getString(
                R.string.v1_failed_confirmation
            )
            customSnackBar.setText(message)
            if (customSnackBar.isShown) {
                customSnackBar.refreshCounting()
            } else {
                customSnackBar.show()
            }
            viewModel.dispatchResetSnackBar()
        }
    }

    override fun onPause() {
        super.onPause()
        customSnackBar.dismiss()
    }

    override fun handleBackPressed(tagNameBackStack: String?) {
        onBackPress.invoke(
            viewModel.currentState.deviceId ?: 0,
            viewModel.currentState.stateIsFavourite
        )
        super.handleBackPressed(tagNameBackStack)
    }

    companion object {
        const val DEVICE_ID = "DEVICE_ID"

        fun newInstance(deviceId: Int): DetailDeviceFragment {
            val args = Bundle().apply {
                putInt(DEVICE_ID, deviceId)
            }
            val fragment = DetailDeviceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
