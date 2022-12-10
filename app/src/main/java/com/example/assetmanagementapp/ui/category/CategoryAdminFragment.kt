package com.example.assetmanagementapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.ui.snackbar.CustomSnackBar
import com.example.app_common.utils.ScreenUtils
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentCategoryAdminBinding
import com.example.assetmanagementapp.ui.editcategorydialog.EditCategoryDialog
import com.example.assetmanagementapp.ui.typeassetsheet.TypeAssetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CategoryAdminFragment : BaseFragment() {

    private lateinit var binding: FragmentCategoryAdminBinding
    private val viewModel: TypeAssetViewModel by viewModels()
    private val editCategoryDialog: EditCategoryDialog by lazy {
        EditCategoryDialog()
    }
    private val categoryAdminAdapter: CategoryAdminAdapter by lazy {
        CategoryAdminAdapter().apply {
            onItemClick = { typeAsset ->
                editCategoryDialog.apply {
                    category = typeAsset
                    titleText = this@CategoryAdminFragment.getString(R.string.edit_category)
                    submitOnClick = { cateName ->
                        viewModel.editCategory(typeAsset.id, cateName)
                    }
                }.show(parentFragmentManager, null)
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
        binding = FragmentCategoryAdminBinding.inflate(layoutInflater)
        initData()
        initView()
        initObserver()
        return binding.root
    }

    private fun initData() {

    }

    private fun initView() {
        binding.apply {
            layoutToolbar.apply {
                tvCenter.text = getString(R.string.v1_category)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }
            rvCategory.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = categoryAdminAdapter
            }
            btnAddCategory.setSafeOnClickListener {
                editCategoryDialog.apply {
                    titleText = this@CategoryAdminFragment.getString(R.string.add_category)
                    submitOnClick = { cateName ->
                        viewModel.addCategory(cateName)
                    }
                    category = null
                }.show(parentFragmentManager, null)
            }
        }
    }

    private fun initObserver() {
        viewModel.store.apply {
            observe(
                owner = this@CategoryAdminFragment,
                selector = { state -> state.listCategory },
                observer = {
                    if (it.isNotEmpty()) {
                        categoryAdminAdapter.submitList(it)
                    }
                })
            observe(
                owner = this@CategoryAdminFragment,
                selector = { state -> state.stateSuccess },
                observer = {
                    it?.apply {
                        showCustomSnackBar(it)
                    }
                })
        }
        viewModel.loadingState().onEach {
            handleShowLoadingDialog(it)
        }.launchIn(lifecycleScope)
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

}
