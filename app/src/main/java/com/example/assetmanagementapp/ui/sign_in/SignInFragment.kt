package com.example.assetmanagementapp.ui.sign_in

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.replaceFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseActivity
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentSignInBinding
import com.example.assetmanagementapp.ui.forgotpassword.ResetPasswordFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment() {
    private var binding: FragmentSignInBinding? = null
    private val viewModel: SignInViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            data = viewModel
        }
        initData()
        initEvents()
        return binding?.root
    }

    private fun initEvents() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.loadingState().collect {
                    handleShowLoadingDialog(it)
                }
            }
            launch {
                viewModel.navigateToHomeStatus.collect {
                    if (it) {
                        handleRemember()
                        viewModel.navigateToHomeStatus.value = false
                    }
                }
            }
        }
    }

    private fun handleRemember() {
        binding?.apply {
            if (checkboxRemember.isChecked) {
                saveInfo()
            } else {
                clearInfo()
            }
        }
    }

    private fun clearInfo() {
        if (!viewModel.clearRemember()) {
            viewModel.isShowError.value = SignInViewModel.ERROR_DATABASE
        } else {
            navToHomeActivity()
        }
    }

    private fun saveInfo() {
        if (!viewModel.saveUserInfo()) {
            viewModel.isShowError.value = SignInViewModel.ERROR_DATABASE
        } else {
            navToHomeActivity()
        }
    }

    private fun navToHomeActivity() {
        recreateMainScreen()
    }

    private fun togglePasswordVisibility() {
        binding?.apply {
            if (viewModel.isPasswordVisible) {
                edtPassword.transformationMethod = PasswordTransformationMethod()
                ibTogglePassword.setImageResource(R.drawable.ic_password_toggle_invisible)
                edtPassword.setSelection(edtPassword.length())
            } else {
                edtPassword.transformationMethod = null
                ibTogglePassword.setImageResource(R.drawable.ic_password_toggle_visible)
                edtPassword.setSelection(edtPassword.length())
            }
            viewModel.isPasswordVisible = !viewModel.isPasswordVisible
        }
    }

    private fun clearInput(editText: EditText) {
        editText.setText(AppConstant.EMPTY)
        editText.requestFocus()
    }

    private fun initData() {
        binding?.apply {
            tvForgotPass.setSafeOnClickListener {
                (requireActivity() as? BaseActivity)?.replaceFragment(ResetPasswordFragment().apply {
                    setLevel(AppConstant.LEVEL_TAB)
                }, true)
            }
            ibTogglePassword.setOnClickListener {
                togglePasswordVisibility()
            }
            ibClearPassword.setOnClickListener {
                clearInput(edtPassword)
            }
            ibClearUsername.setOnClickListener {
                clearInput(edtUsername)
            }
        }
    }
}
