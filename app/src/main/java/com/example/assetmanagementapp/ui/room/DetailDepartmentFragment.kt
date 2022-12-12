package com.example.assetmanagementapp.ui.room

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
import com.example.assetmanagementapp.databinding.FragmentDetailDepartmentBinding
import com.example.assetmanagementapp.ui.department.AddDepartmentDialog
import com.example.assetmanagementapp.ui.searchresult.SearchResultFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailDepartmentFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailDepartmentBinding
    private val viewModel: DetailDepartmentViewModel by viewModels()
    private var layoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private val roomAdapter: RoomAdapter by lazy {
        RoomAdapter().apply {
            onClick = {
                addNoNavigationFragment(
                    SearchResultFragment.newInstance(
                        departmentName = it.roomName,
                        departmentId = viewModel.currentState.departmentId,
                        roomId = it.roomId
                    )
                )
            }
            isShowAddNotification = viewModel.stateIsAdmin.value
            onAddNotification = {
                viewModel.createNotification(it)
            }
        }
    }
    private val addDepartmentDialog: AddDepartmentDialog by lazy {
        AddDepartmentDialog().apply {
            addAction = {
                viewModel.addRoom(it)
            }
        }
    }

    private val customSnackBar: CustomSnackBar by lazy {
        CustomSnackBar.make(
            parent = binding.layoutParent,
            message = "This room is already added"
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
        binding = FragmentDetailDepartmentBinding.inflate(layoutInflater)
        initData()
        initView()
        initEvent()
        return binding.root
    }

    private fun initEvent() {
        viewModel.store.apply {
            observe(
                owner = this@DetailDepartmentFragment,
                selector = { state -> state.listRoom },
                observer = {
                    if (it.isNotEmpty()) {
                        roomAdapter.submitList(it)
                        binding.layoutNoResult.root.visibility = View.GONE
                        binding.rvRooms.visibility = View.VISIBLE
                    } else {
                        binding.layoutNoResult.root.visibility = View.VISIBLE
                        binding.rvRooms.visibility = View.GONE
                    }
                })
            observe(
                owner = this@DetailDepartmentFragment,
                selector = { state -> state.stateAddRoomSuccess },
                observer = {
                    it?.let { showCustomSnackBar(it) }
                })
        }

        viewModel.loadingState().onEach {
            handleShowLoadingDialog(it)
        }.launchIn(lifecycleScope)
    }

    private fun initView() {
        binding.apply {
            toolbarId.apply {
                tvCenter.text = getString(
                    R.string.v1_rooms_of_department,
                    arguments?.getString(DEPARTMENT_NAME) ?: "Department"
                )
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
                endIcon.visibility = View.VISIBLE
                endIcon.setImageResource(R.drawable.ic_search_outline)
                endIcon.setSafeOnClickListener {
                    addNoNavigationFragment(
                        SearchResultFragment.newInstance(
                            departmentName = arguments?.getString(DEPARTMENT_NAME),
                            departmentId = viewModel.currentState.departmentId
                        )
                    )
                }
            }
            rvRooms.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = roomAdapter

            }
            btnAddRoom.setSafeOnClickListener {
                addDepartmentDialog.show(parentFragmentManager, null)
            }

            btnAddRoom.visibility = if (viewModel.stateIsAdmin.value) View.VISIBLE else View.GONE
        }
        layoutListener = binding.btnAddRoom.let { button ->
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

    private fun showCustomSnackBar(isSuccess: Boolean) {
        val message = getString(
            if (isSuccess) R.string.add_room_success else R.string.add_room_false
        )

        customSnackBar.setText(message)
        if (customSnackBar.isShown) {
            customSnackBar.refreshCounting()
        } else {
            customSnackBar.show()
        }
        viewModel.resetStateSnackBar()
    }

    private fun initData() {
        arguments?.getInt(DEPARTMENT_ID)?.let { departmentId ->
            viewModel.currentState.departmentId = departmentId
            viewModel.getRoomsByDepartmentId(departmentId)
        }
    }

    companion object {
        private const val DEPARTMENT_ID = "DEPARTMENT_ID"
        private const val DEPARTMENT_NAME = "DEPARTMENT_NAME"
        fun newInstance(departmentId: Int, nameDepartment: String): DetailDepartmentFragment {
            val args = Bundle().apply {
                putInt(DEPARTMENT_ID, departmentId)
                putString(DEPARTMENT_NAME, nameDepartment)
            }
            val fragment = DetailDepartmentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
