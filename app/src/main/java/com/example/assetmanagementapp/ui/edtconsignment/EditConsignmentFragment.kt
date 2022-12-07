package com.example.assetmanagementapp.ui.edtconsignment

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.ui.snackbar.CustomSnackBar
import com.example.app_common.utils.ScreenUtils
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import com.example.assetmanagementapp.databinding.FragmentEditConsignmentBinding
import com.example.assetmanagementapp.ui.imagepicker.BottomSheetUploadAvatarFragment
import com.example.assetmanagementapp.ui.providersheet.ProviderBottomSheet
import com.example.assetmanagementapp.ui.typeassetsheet.AssetTypeBottomSheet
import com.example.assetmanagementapp.utils.loadImageUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.util.Calendar

@AndroidEntryPoint
class EditConsignmentFragment : BaseFragment() {

    private lateinit var binding: FragmentEditConsignmentBinding
    private val viewModel: EditConsignmentViewModel by viewModels()
    public var onBackPressResult: (ConsignmentItem) -> Unit = {}

    private val providerBottomSheet: ProviderBottomSheet by lazy {
        ProviderBottomSheet().apply {
            categoryOnClick = {
                viewModel.dispatchProvider(it)
            }
        }
    }

    private val typeAssetBottomSheet: AssetTypeBottomSheet by lazy {
        AssetTypeBottomSheet().apply {
            categoryOnClick = {
                viewModel.dispatchCategory(it)
            }
        }
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
                bottom = ScreenUtils.toPx(context, 16f)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditConsignmentBinding.inflate(layoutInflater)
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
                viewModel.dispatchStateConsignmentItem(this)
            }
        }
    }

    private fun initView() {
        binding.apply {
            toolbarId.apply {
                tvCenter.text = getString(R.string.edit_consignment)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }
            btnUpdateAvatar.setSafeOnClickListener {
                pickAvatar()
            }
            edtAssetType.setSafeOnClickListener {
                typeAssetBottomSheet.apply {
                    currentCategory = viewModel.currentState.currentCategory?.id ?: 0
                    currentNameCategory = viewModel.currentState.currentCategory?.typeName ?: ""
                }.show(parentFragmentManager, null)
            }
            edtProvider.setSafeOnClickListener {
                providerBottomSheet.apply {
                    currentProvider = viewModel.currentState.currentProvider?.providerId ?: 0
                    currentProviderName = viewModel.currentState.currentProvider?.name ?: ""
                }.show(parentFragmentManager, null)
            }
            val myCalendar = Calendar.getInstance()
            edtDateIn.setSafeOnClickListener {
                val tempYear = myCalendar.get(Calendar.YEAR)
                val tempMonth = myCalendar.get(Calendar.MONTH)
                val tempDate = myCalendar.get(Calendar.DAY_OF_MONTH)
                DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        edtDateIn.setText(
                            String.format(
                                "%04d-%02d-%02d",
                                year,
                                month,
                                dayOfMonth
                            )
                        )
                    },
                    tempYear,
                    tempMonth,
                    tempDate
                ).show()
            }
            edtDateManufacture.setSafeOnClickListener {
                val tempYear = myCalendar.get(Calendar.YEAR)
                val tempMonth = myCalendar.get(Calendar.MONTH)
                val tempDate = myCalendar.get(Calendar.DAY_OF_MONTH)
                DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        edtDateManufacture.setText(
                            String.format(
                                "%04d-%02d-%02d",
                                year,
                                month,
                                dayOfMonth
                            )
                        )
                    },
                    tempYear,
                    tempMonth,
                    tempDate
                ).show()
            }
            edtDateWarranty.setSafeOnClickListener {
                val tempYear = myCalendar.get(Calendar.YEAR)
                val tempMonth = myCalendar.get(Calendar.MONTH)
                val tempDate = myCalendar.get(Calendar.DAY_OF_MONTH)
                DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        edtDateWarranty.setText(
                            String.format(
                                "%04d-%02d-%02d",
                                year,
                                month,
                                dayOfMonth
                            )
                        )
                    },
                    tempYear,
                    tempMonth,
                    tempDate
                ).show()
            }
            btnSubmit.setSafeOnClickListener {
                try {
                    viewModel.edtConsignment(
                        consignmentName = edtNameConsignment.text.toString(),
                        assetName = edtNameAsset.text.toString(),
                        numberOfAsset = edtNumberAsset.text.toString().toInt(),
                        unitPrice = edtUnitPrice.text.toString().toInt(),
                        brand = edtBrandName.text.toString(),
                        dateIn = edtDateIn.text.toString(),
                        dateManufacture = edtDateManufacture.text.toString(),
                        dateWarranty = edtDateWarranty.text.toString(),
                        description = edtDescription.text.toString(),
                        imageFile = File(
                            getRealPathFromURI(viewModel.currentState.currentUri) ?: ""
                        )
                    )
                } catch (ex: Exception) {
                    showCustomSnackBar(false)
                }
            }
        }
    }

    private fun pickAvatar() {
        showBottomSheet(BottomSheetUploadAvatarFragment().apply {
            onPhotoSelected = { uri, _ ->
                viewModel.currentState.currentUri = uri
                binding.ivAvatarAsset.setImageURI(uri)
                binding.tvTitleImageAsset.visibility = View.GONE
                dismiss()
            }
        })
    }

    private fun showCustomSnackBar(isSuccess: Boolean) {
        val message = getString(
            if (isSuccess) R.string.v1_success else R.string.have_something_wrong_with_your_input
        )

        customSnackBar.setText(message)
        if (customSnackBar.isShown) {
            customSnackBar.refreshCounting()
        } else {
            customSnackBar.show()
        }
        viewModel.dispatchResetSnackBar()
    }


    private fun getRealPathFromURI(contentUri: Uri?): String? {
        if (contentUri == null) return null
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(contentUri, proj, null, null, null)
        cursor?.apply {
            if (cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = cursor.getString(index)
            }
            cursor.close()
        } ?: kotlin.run {
            res = contentUri.path
        }
        return res
    }


    private fun initObserver() {
        viewModel.store.apply {
            observe(
                owner = this@EditConsignmentFragment,
                selector = { state -> state.currentCategory },
                observer = {
                    it?.apply {
                        binding.edtAssetType.setText(it.typeName)
                    }
                }
            )
            observe(
                owner = this@EditConsignmentFragment,
                selector = { state -> state.stateConsignmentItem },
                observer = {
                    it?.apply {
                        handleShowUi(it)
                    }
                }
            )
            observe(
                owner = this@EditConsignmentFragment,
                selector = { state -> state.currentProvider },
                observer = {
                    it?.apply {
                        binding.edtProvider.setText(it.name)
                    }
                }
            )
            observe(
                owner = this@EditConsignmentFragment,
                selector = { state -> state.stateShowSnackBarSuccess },
                observer = {
                    it?.apply {
                        showCustomSnackBar(it)
                    }
                }
            )
        }
        viewModel.loadingState().onEach {
            handleShowLoadingDialog(it)
        }.launchIn(lifecycleScope)
    }

    private fun handleShowUi(item: ConsignmentItem) {
        binding.apply {
            item.uriImage?.apply {
                ivAvatarAsset.setImageURI(this)
            } ?: kotlin.run {
                ivAvatarAsset.loadImageUri(item.image)
            }
            edtNameAsset.setText(item.name)
            edtNameConsignment.setText(item.consignmentName)
            edtBrandName.setText(item.brand)
            edtNumberAsset.setText("${item.number}")
            edtUnitPrice.setText("${item.unitPrice.toInt()}")
            edtDateIn.setText(item.dateIn.substring(0, 10))
            edtDateManufacture.setText(item.dateManufacture.substring(0, 10))
            edtDateWarranty.setText(item.dateWarranty.substring(0, 10))
            edtDescription.setText(item.description)
        }
    }

    override fun handleBackPressed(tagNameBackStack: String?) {
        viewModel.currentState.stateConsignmentItem?.apply { onBackPressResult.invoke(this) }
        super.handleBackPressed(tagNameBackStack)
    }

    companion object {
        private const val CONSIGNMENT_ITEM = "CONSIGNMENT_ITEM"

        fun newInstance(consignmentItem: ConsignmentItem): EditConsignmentFragment {
            val args = Bundle().apply {
                putParcelable(CONSIGNMENT_ITEM, consignmentItem)
            }
            val fragment = EditConsignmentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
