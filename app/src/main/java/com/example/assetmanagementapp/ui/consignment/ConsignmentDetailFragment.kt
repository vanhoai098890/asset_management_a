package com.example.assetmanagementapp.ui.consignment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import com.example.assetmanagementapp.databinding.FragmentConsignmentDetailBinding
import com.example.assetmanagementapp.databinding.LayoutItemInfoDeviceBinding
import com.example.assetmanagementapp.ui.edtconsignment.EditConsignmentFragment
import com.example.assetmanagementapp.utils.loadImageUri
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConsignmentDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentConsignmentDetailBinding
    private val viewModel: ConsignmentDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConsignmentDetailBinding.inflate(layoutInflater)
        binding.apply {
            data = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initData()
        initView()
        initObserver()
        return binding.root
    }

    private fun initData() {
        arguments?.apply {
            getParcelable<ConsignmentItem>(CONSIGNMENT_ITEM)?.apply {
                viewModel.dispatchStateConsignment(this)
            }
        }
    }

    private fun initObserver() {
        viewModel.store.observe(
            owner = this,
            selector = { state -> state.stateConsignment },
            observer = { it?.apply { handleShowUI(it) } })
    }

    private fun initView() {
        binding.apply {
            toolbarId.apply {
                tvCenter.text = getString(R.string.detail_consignment)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }
            ivMess.setSafeOnClickListener {
                viewModel.currentState.stateConsignment?.providerPhone?.let { phoneNumber ->
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.fromParts("sms", phoneNumber, null)
                        )
                    )
                }
            }
            ivPhone.setSafeOnClickListener {
                viewModel.currentState.stateConsignment?.providerPhone?.let { phoneNumber ->
                    startActivity(
                        Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel: $phoneNumber")
                        )
                    )
                }
            }
            btnEditConsignment.setSafeOnClickListener {
                if (viewModel.currentState.stateConsignment != null) {
                    addNoNavigationFragment(
                        EditConsignmentFragment.newInstance(viewModel.currentState.stateConsignment!!)
                            .apply {
                                onBackPressResult = {
                                    viewModel.dispatchStateConsignment(it)
                                    it.uriImage?.apply {
                                        ivAvatarAsset.setImageURI(this)
                                    }
                                }
                            })
                }
            }
        }
    }

    private fun handleShowUI(consignmentItem: ConsignmentItem) {
        handleShowLayoutInfoDevice(consignmentItem)
        consignmentItem.uriImage?.apply {
            binding.ivAvatarAsset.setImageURI(this)
        } ?: kotlin.run {
            binding.ivAvatarAsset.loadImageUri(consignmentItem.image)
        }
    }

    private fun handleShowLayoutInfoDevice(deviceItem: ConsignmentItem) {
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
            itemInfo = binding.layoutNumberAssets,
            nameInfo = "${deviceItem.number}",
            icon = R.drawable.ic_outline_spoke_24,
            detail = R.string.number
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

    companion object {

        private const val CONSIGNMENT_ITEM = "CONSIGNMENT_ITEM"

        fun newInstance(consignmentItem: ConsignmentItem): ConsignmentDetailFragment {
            val args = Bundle().apply {
                putParcelable(CONSIGNMENT_ITEM, consignmentItem)
            }
            val fragment = ConsignmentDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
