package com.example.assetmanagementapp.ui.admin

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentAdminBinding
import com.example.assetmanagementapp.ui.category.CategoryAdminFragment
import com.example.assetmanagementapp.ui.consignmentmain.ConsignmentFragment
import com.example.assetmanagementapp.ui.department.DepartmentFragment
import com.example.assetmanagementapp.ui.room.DetailDepartmentFragment
import com.example.assetmanagementapp.ui.searchresult.SearchResultFragment
import com.example.assetmanagementapp.ui.usermanagement.UserManagementFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminFragment : BaseFragment() {

    private val viewModel: AdminViewModel by viewModels()
    private lateinit var binding: FragmentAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(layoutInflater)
        initData()
        initView()
        initObserver()
        return binding.root
    }

    private fun initObserver() {

    }

    private fun initView() {
        binding.apply {
            layoutToolbar.apply {
                tvCenter.text = getString(R.string.admin)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }
            layoutAddConsignment.apply {
                root.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.admin_color_1, null))
                tvItemAdmin.text = getString(R.string.add_consignment)
                ivItemAdmin.setImageResource(R.drawable.ic_baseline_add_24)
                root.setSafeOnClickListener {
                    addNoNavigationFragment(ConsignmentFragment())
                }
            }
            layoutAddAsset.apply {
                root.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.dairyCream, null))
                tvItemAdmin.text = getString(R.string.add_asset)
                ivItemAdmin.setImageResource(R.drawable.ic_setting_launcher)
                root.setSafeOnClickListener {
                    addNoNavigationFragment(
                        SearchResultFragment.newInstance(
                            searchString = "",
                            isAdmin = true
                        )
                    )
                }
            }
            layoutManageCategory.apply {
                root.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.admin_color_5, null))
                tvItemAdmin.text = getString(R.string.v1_category)
                ivItemAdmin.setImageResource(R.drawable.ic_outline_category_24)
                root.setSafeOnClickListener {
                    addNoNavigationFragment(CategoryAdminFragment())
                }
            }
            layoutManageUser.apply {
                root.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.admin_color_6, null))
                tvItemAdmin.text = getString(R.string.account_management)
                ivItemAdmin.setImageResource(R.drawable.ic_baseline_manage_accounts_24)
                root.setSafeOnClickListener {
                    addNoNavigationFragment(UserManagementFragment())
                }
            }
            layoutCreateNotification.apply {
                root.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.admin_color_4, null))
                tvItemAdmin.text = getString(R.string.property_assessment)
                ivItemAdmin.setImageResource(R.drawable.ic_baseline_notification_add_24)
                root.setSafeOnClickListener {
                    addNoNavigationFragment(DepartmentFragment())
                }
            }
        }
    }

    private fun initData() {

    }
}
