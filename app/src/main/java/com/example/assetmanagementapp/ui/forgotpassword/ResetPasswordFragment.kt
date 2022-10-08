package com.example.assetmanagementapp.ui.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.base.exception.DefaultError
import com.example.app_common.base.response.ApiResponseCode
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.app_common.extensions.replaceFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.utils.LogUtils.layoutListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseActivity
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.assetmanagementapp.ui.setpassword.SetPasswordFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment() {

    companion object {
        private const val EMPTY = -1
    }

    private var binding: FragmentResetPasswordBinding? = null
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            resetPasswordViewModel = viewModel
        }
        initViews()
        initEvents()
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.apply {
            layoutListener?.apply {
                root.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
            }
            edtPhoneNumber.removeTextChangedListener(viewModel.phoneNumberTextWatcher)
        }
        binding = null
    }

    private fun initEvents() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.loadingState().collect {
                    if (it) {
                        showLoading()
                    } else {
                        hideLoading()
                    }
                }
            }
        }
        binding?.btnResetPassword?.setSafeOnClickListener {
            viewModel.validationPhoneNumber(viewModel.phoneNumber.value)
            if (viewModel.isPhoneNumberEqual11Digits(viewModel.phoneNumber.value)) {
                processResetPassword()
            }
        }
        binding?.layoutToolbar?.backButton?.setSafeOnClickListener {
            handleBackPressed()
        }
        layoutListener = binding?.btnResetPassword?.let { button ->
            binding?.root?.let { root ->
                layoutListener(
                    button,
                    root, requireContext()
                )
            }
        }
        binding?.apply {
            layoutListener?.apply {
                root.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
            }
        }
    }

    private var layoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    private fun processResetPassword() {
        viewModel.resetPassword().onSuccess {
            (requireActivity() as? BaseActivity)?.replaceFragment(
                fragment = SetPasswordFragment.newInstance(
                    viewModel.phoneNumber.value,
                    SetPasswordTypeFlow.FLOW_RESET_PASSWORD.ordinal
                ).apply {
                    setLevel(AppConstant.LEVEL_TAB)
                },
                isAddBackStack = true,
                tagNameBackStack = "SignUpInputPhoneFragment"
            )
        }.onError {
            if (it is DefaultError) {
                viewModel.enableNextButton(false)
                when (it.apiErrorCode) {
                    ApiResponseCode.E005 -> viewModel.setErrorMessage(
                        R.string.s02001_phone_number_is_not_register
                    )
                    ApiResponseCode.E008 -> viewModel.setErrorMessage(
                        R.string.s02001_account_is_deactivate
                    )
                    ApiResponseCode.E037 -> viewModel.setErrorMessage(
                        R.string.s02001_phone_number_is_locked
                    )
                    else -> viewModel.setErrorMessage(EMPTY)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun initViews() {
        binding?.run {
            layoutToolbar.tvCenter.text =
                getString(R.string.s02001_reset_password_title)
            layoutToolbar.backButton.visibility = View.VISIBLE
        }
    }
}
