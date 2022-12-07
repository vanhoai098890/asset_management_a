package com.example.assetmanagementapp.ui.detaildevice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.constant.AppConstant.ERROR_BITMAP
import com.example.app_common.constant.AppConstant.LOADING_BITMAP
import com.example.app_common.constant.AppConstant.SHOW_BITMAP
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.ui.snackbar.CustomSnackBar
import com.example.app_common.utils.ScreenUtils
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.databinding.DeviceDetailFragmentBinding
import com.example.assetmanagementapp.databinding.LayoutItemInfoDeviceBinding
import com.example.assetmanagementapp.ui.detaildevicedialog.DetailDeviceDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailDeviceFragment private constructor() : BaseFragment() {

    private lateinit var binding: DeviceDetailFragmentBinding
    private val viewModel: DetailDeviceViewModel by viewModels()
    var onBackPress: (Int, Boolean) -> Unit = { _, _ -> }
    private val editAssetDialog: DetailDeviceDialog by lazy {
        DetailDeviceDialog()
    }

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
    private val qrcodeDialog: QrcodeDialog by lazy {
        QrcodeDialog()
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
            viewModel.dispatchStateIsAdmin(getBoolean(IS_ADMIN))
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
                btnGenerateQr.setSafeOnClickListener {
                    customSnackBar.dismiss()
                    viewModel.showDialogQrcode()
                }
                btnEditAsset.setSafeOnClickListener {
                    editAssetDialog.apply {
                        roomName = viewModel.currentState.deviceItem?.roomName ?: ""
                        statusName = viewModel.currentState.deviceItem?.status ?: ""
                        handleBackPress = { roomName, statusName ->
                            viewModel.currentState.deviceItem?.apply {
                                viewModel.dispatchStateItem(
                                    this.copy(
                                        roomName = roomName,
                                        status = statusName
                                    )
                                )
                            }
                        }
                        deviceId = viewModel.currentState.deviceItem?.id ?: 0
                    }.show(parentFragmentManager, null)
                }
            }
        }
    }


    private fun initEvent() {
        viewModel.getDetailDevice()
        viewModel.getQrcodeAsset()
        viewModel.loadingState().onEach {
            handleShowLoadingDialog(it)
        }.launchIn(lifecycleScope)
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
            observe(
                owner = this@DetailDeviceFragment,
                selector = { currentState -> currentState.stateShowDialogBitmap },
                observer = { stateDialog ->
                    if (stateDialog != 0) {
                        showSnackBarDialog(stateDialog)
                        viewModel.dispatchResetStateDialog()
                    }
                }
            )
        }
    }

    private fun handleShowUI(deviceItem: DeviceItem) {
//        handleShowButton(deviceItem)
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
            icon = R.drawable.ic_amount,
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
        handleShowItemInfo(
            itemInfo = binding.itemNameRoom,
            nameInfo = deviceItem.roomName,
            icon = R.drawable.ic_baseline_meeting_room_24,
            detail = R.string.v1_room
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

    private fun showSnackBarDialog(stateDialog: Int) {
        if (stateDialog == SHOW_BITMAP) {
            qrcodeDialog.apply {
                bitmap = viewModel.currentState.qrCodeBitmap
            }.show(parentFragmentManager, null)
            viewModel.dispatchResetSnackBar()
            return
        }
        val message = when (stateDialog) {
            LOADING_BITMAP -> {
                getString(R.string.qrcode_is_generating)
            }
            ERROR_BITMAP -> {
                getString(R.string.qrcode_is_error)
            }
            else -> ""
        }

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
        private const val DEVICE_ID = "DEVICE_ID"
        private const val IS_ADMIN = "IS_ADMIN"

        fun newInstance(deviceId: Int, stateIsAdmin: Boolean = false): DetailDeviceFragment {
            val args = Bundle().apply {
                putInt(DEVICE_ID, deviceId)
                putBoolean(IS_ADMIN, stateIsAdmin)
            }
            val fragment = DetailDeviceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
