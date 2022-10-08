package com.example.assetmanagementapp.ui.setpassword

import android.os.Bundle
import android.text.Editable
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.onSuccess
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.utils.text_watcher.TextWatcherImpl
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentSetPasswordBinding
import com.example.assetmanagementapp.ui.forgotpassword.SetPasswordTypeFlow
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import com.example.assetmanagementapp.ui.verifyotp.VerifyOTPFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn

@AndroidEntryPoint
class SetPasswordFragment : BaseFragment() {

    companion object {
        private const val KEY_INPUT_PHONE = "KEY_INPUT_PHONE"
        private const val KEY_TYPE_FLOW = "KEY_TYPE_FLOW"
        internal fun newInstance(inputPhone: String, typeFlow: Int) = SetPasswordFragment()
            .apply {
                arguments = Bundle().apply {
                    putString(KEY_INPUT_PHONE, inputPhone)
                    putInt(KEY_TYPE_FLOW, typeFlow)
                }
            }
    }

    private val viewModel by viewModels<SetPasswordViewModel>()
    private var binding: FragmentSetPasswordBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_set_password, container, false)
        binding?.run {
            lifecycleOwner = this@SetPasswordFragment.viewLifecycleOwner
            initEvents()
            data = viewModel
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        activity?.apply {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        arguments?.let {
            viewModel.phoneNumber = it.getString(KEY_INPUT_PHONE, "")
            viewModel.typeFlow = it.getInt(KEY_TYPE_FLOW, -1)
        }
        binding?.apply {
            layoutCommonToolbarAuthen.apply {
                tvCenter.text = resources.getString(R.string.s01004_password_setting)
                backButton.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        handleBackPressed()
                    }
                }
            }
        }
    }

    private fun initEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.loadingState().collect {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        }
        binding?.btnSetPassword?.setSafeOnClickListener {
            checkTypeFlowAndRedirect()
        }
        binding?.ibTogglePassword?.setOnClickListener {
            togglePasswordVisibility()
        }
        binding?.ibToggleRePassword?.setOnClickListener {
            toggleRePasswordVisibility()
        }
        binding?.ibClearPassword?.setOnClickListener {
            clearTextPasswordInput(binding?.edtPassword)
        }
        binding?.ibClearRePassword?.setOnClickListener {
            clearTextPasswordInput(binding?.edtReTypePassword)
        }
        binding?.edtPassword?.setOnFocusChangeListener { _, hasFocus ->
            handleFocusChange(
                hasFocus,
                binding?.edtPassword,
                viewModel.password.value,
                binding?.ibClearPassword
            )
        }

        binding?.edtReTypePassword?.setOnFocusChangeListener { _, hasFocus ->
            handleFocusChange(
                hasFocus,
                binding?.edtReTypePassword,
                viewModel.password.value,
                binding?.ibClearRePassword
            )
        }
    }

    private fun checkTypeFlowAndRedirect() {
        arguments?.apply {
            val phoneEntered = getString(KEY_INPUT_PHONE) ?: ""
            if (getInt(KEY_TYPE_FLOW) == SetPasswordTypeFlow.FLOW_SIGN_UP.ordinal) {
                viewModel.postPhoneSignUp(phoneEntered).onSuccess {
                    navigateToVerifyOTPFragment()
                }.launchIn(lifecycleScope)
            } else {
                viewModel.postPhoneResetPassword(phoneEntered).onSuccess {
                    navigateToVerifyOTPFragment()
                }.launchIn(lifecycleScope)
            }
        }
    }

    private fun navigateToVerifyOTPFragment() {
        addFragmentNotMain(
            VerifyOTPFragment.newInstance(
                viewModel.phoneNumber,
                viewModel.password.value,
                viewModel.typeFlow
            )
        )
    }

    private fun togglePasswordVisibility() {
        if (viewModel.isPasswordVisible) {
            binding?.edtPassword?.transformationMethod = PasswordTransformationMethod()
            binding?.ibTogglePassword?.setImageResource(R.drawable.ic_password_toggle_invisible)
            binding?.edtPassword?.setSelection(viewModel.password.value.length)
        } else {
            binding?.edtPassword?.transformationMethod = null
            binding?.ibTogglePassword?.setImageResource(R.drawable.ic_password_toggle_visible)
            binding?.edtPassword?.setSelection(viewModel.password.value.length)
        }
        viewModel.isPasswordVisible = !viewModel.isPasswordVisible
    }

    private fun toggleRePasswordVisibility() {
        if (viewModel.isRePasswordVisible) {
            binding?.edtReTypePassword?.transformationMethod = PasswordTransformationMethod()
            binding?.ibToggleRePassword?.setImageResource(R.drawable.ic_password_toggle_invisible)
            binding?.edtReTypePassword?.setSelection(viewModel.confirmPassword.value.length)
        } else {
            binding?.edtReTypePassword?.transformationMethod = null
            binding?.ibToggleRePassword?.setImageResource(R.drawable.ic_password_toggle_visible)
            binding?.edtReTypePassword?.setSelection(viewModel.confirmPassword.value.length)
        }
        viewModel.isRePasswordVisible = !viewModel.isRePasswordVisible
    }

    private fun clearTextPasswordInput(textInputEditText: TextInputEditText?) {
        textInputEditText?.setText(AppConstant.EMPTY)
        textInputEditText?.requestFocus()
    }

    private fun handleTextChange(
        textInputEditText: TextInputEditText?,
        clearButton: ImageButton?
    ) {
        textInputEditText?.addTextChangedListener(object : TextWatcherImpl() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if (s.toString().isNotEmpty()) {
                    clearButton?.visibility = View.VISIBLE
                } else {
                    clearButton?.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun handleFocusChange(
        isFocus: Boolean,
        textInputEditText: TextInputEditText?,
        currentValue: String,
        clearButton: ImageButton?
    ) {
        if (isFocus) {
            if (currentValue.isNotEmpty()) {
                clearButton?.visibility = View.VISIBLE
            } else {
                clearButton?.visibility = View.INVISIBLE
            }
            handleTextChange(textInputEditText, clearButton)
        } else {
            clearButton?.visibility = View.INVISIBLE
        }
    }
}
