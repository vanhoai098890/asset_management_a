package com.example.assetmanagementapp.ui.department

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.ui.snackbar.CustomSnackBar
import com.example.app_common.utils.LogUtils
import com.example.app_common.utils.ScreenUtils
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentDepartmentBinding
import com.example.assetmanagementapp.ui.department.DepartmentAdapter.Companion.WAREHOUSE
import com.example.assetmanagementapp.ui.room.DetailDepartmentFragment
import com.example.assetmanagementapp.ui.searchresult.SearchResultFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DepartmentFragment : BaseFragment() {

    private val viewModel: DepartmentViewModel by viewModels()
    private lateinit var binding: FragmentDepartmentBinding
    private val departmentAdapter by lazy {
        DepartmentAdapter().apply {
            onClick = {
                if (it.departmentName == WAREHOUSE) {
                    addNoNavigationFragment(
                        SearchResultFragment.newInstance(
                            departmentId = it.departmentId,
                            departmentName = it.departmentName,
                            roomId = 18
                            //hard code in here 18 -> warehouse room
                        )
                    )
                } else {
                    addNoNavigationFragment(
                        DetailDepartmentFragment.newInstance(
                            it.departmentId,
                            it.departmentName
                        )
                    )
                }
            }
        }
    }
    private val addDepartmentDialog: AddDepartmentDialog by lazy {
        AddDepartmentDialog().apply {
            addAction = {
                viewModel.addDepartment(it)
            }
        }
    }

    private val customSnackBar: CustomSnackBar by lazy {
        CustomSnackBar.make(
            parent = binding.layoutParent,
            message = "This department is already added"
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepartmentBinding.inflate(layoutInflater)
        initData()
        initView()
        initEvent()
        return binding.root
    }

    private fun initView() {
        binding.apply {
            toolbarId.apply {
                tvCenter.text = getString(R.string.department)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }
            rvDepartment.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = departmentAdapter

            }
            btnAddDepartment.setSafeOnClickListener {
                addDepartmentDialog.show(parentFragmentManager, null)
            }
            btnAddDepartment.visibility =
                if (viewModel.stateIsAdmin.value) View.VISIBLE else View.GONE
        }
        layoutListener = binding.btnAddDepartment.let { button ->
            binding.root.let { root ->
                LogUtils.layoutListener(
                    button,
                    root, requireContext()
                )
            }
        }
        binding.apply {
            layoutListener?.apply {
                root.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
            }
        }
    }

    private var layoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null


    private fun initData() {
    }

    private fun initEvent() {
        viewModel.store.apply {
            observe(
                owner = this@DepartmentFragment,
                selector = { state -> state.listDepartment },
                observer = {
                    if (it.isNotEmpty()) {
                        departmentAdapter.submitList(it)
                        binding.layoutNoResult.root.visibility = View.GONE
                        binding.rvDepartment.visibility = View.VISIBLE
                    } else {
                        binding.layoutNoResult.root.visibility = View.VISIBLE
                        binding.rvDepartment.visibility = View.GONE
                    }
                })
            observe(
                owner = this@DepartmentFragment,
                selector = { state -> state.stateAddDepartmentSuccess },
                observer = {
                    it?.let { showCustomSnackBar(it) }
                })
        }
        viewModel.loadingState().onEach {
            handleShowLoadingDialog(it)
        }.launchIn(lifecycleScope)
    }

    private fun showCustomSnackBar(isSuccess: Boolean) {
        val message = getString(
            if (isSuccess) R.string.add_department_success else R.string.add_department_false
        )

        customSnackBar.setText(message)
        if (customSnackBar.isShown) {
            customSnackBar.refreshCounting()
        } else {
            customSnackBar.show()
        }
        viewModel.resetStateSnackBar()
    }

    override fun onPause() {
        super.onPause()
        customSnackBar.dismiss()
    }


    override fun onDestroy() {
        binding.apply {
            layoutListener?.apply {
                root.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
            }
        }
        super.onDestroy()
    }
}
