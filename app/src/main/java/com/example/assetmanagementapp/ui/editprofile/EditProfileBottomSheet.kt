package com.example.assetmanagementapp.ui.editprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app_common.base.BaseBottomSheetDialogFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.utils.bindingadapter.dpToPx
import com.example.app_common.utils.hideKeyboard
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.databinding.LayoutBottomSheetEditProfileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileBottomSheet :
    BaseBottomSheetDialogFragment() {

    private lateinit var binding: LayoutBottomSheetEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    private val avatarAdapter by lazy {
        AvatarAdapter()
    }
    var onBackPress: () -> Unit = {}

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(s: CharSequence, start: Int, end: Int, count: Int) {
            binding.tvWordsCount.text = getString(
                R.string.email_number_limit,
                s.toString().codePointCount(0, s.length),
                20
            )
            if (s.toString().codePointCount(0, s.length) <= 20) {
                binding.tvWordsCount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primary_neutral_maintext
                    )
                )
                binding.etUsername.setBackgroundResource(R.drawable.bg_edt_focus)
            } else {
                binding.tvWordsCount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                binding.etUsername.setBackgroundResource(R.drawable.bg_edt_error)
            }
            checkValid(s.toString().codePointCount(0, s.length))
        }

        override fun afterTextChanged(p0: Editable?) = Unit
    }

    private fun checkValid(length: Int? = null) {
        if (length != null) {
            binding.btnConfirm.isEnabled = binding.etUsername.text.isNotEmpty()
                    && length <= 20
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetEditProfileBinding.inflate(inflater, container, false)
        initView()
        initAction()
        initObservers()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.also {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.isDraggable = false
            view?.requestLayout()
        }
    }

    private fun initObservers() {
        viewModel.store.apply {
            observe(
                owner = this@EditProfileBottomSheet,
                selector = { state -> state.stateNickName },
                observer = { nickName ->
                    binding.etUsername.setText(nickName)
                    binding.etUsername.setSelection(nickName.length)
                    binding.etUsername.setBackgroundResource(R.drawable.bg_edt_default)
                    binding.etUsername.clearFocus()
                }
            )
            observe(
                owner = this@EditProfileBottomSheet,
                selector = { state -> state.stateListAvatars },
                observer = { avatars ->
                    if (avatars.isNotEmpty()) {
                        avatarAdapter.submitList(avatars)
                    }
                }
            )
            observe(
                owner = this@EditProfileBottomSheet,
                selector = { state -> state.stateOnSuccess },
                observer = {
                    if (it) {
                        onBackPress()
                        dismiss()
                    }
                }
            )
        }
    }

    private fun initView() {
        heightOfDialog =
            (resources.displayMetrics.heightPixels)

        binding.rcyAvatar.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = avatarAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            addItemDecoration(
                MaltsGridSpacingItemDecoration(
                    com.example.app_common.R.dimen.margin_8dp.dpToPx(context), 3
                )
            )
            itemAnimator = null
        }
    }

    private fun initAction() {
        binding.layoutOverview.setSafeOnClickListener {
            binding.root.hideKeyboard()
        }

        binding.etUsername.apply {
            addTextChangedListener(textWatcher)
            setOnFocusChangeListener { _, focus ->
                when {
                    focus -> {
                        setBackgroundResource(R.drawable.bg_edt_focus)
                    }
                    text.length <= 20 -> {
                        setBackgroundResource(R.drawable.bg_edt_default)
                    }
                    else -> {
                        setBackgroundResource(R.drawable.bg_edt_error)
                    }
                }
            }
            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    textView: TextView?,
                    actionId: Int,
                    keyEvent: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        binding.root.hideKeyboard()
                        return true
                    }
                    return false
                }

            })
        }
        binding.ivClose.setSafeOnClickListener {
            dismiss()
        }
        binding.btnConfirm.setSafeOnClickListener {
            binding.root.hideKeyboard()
            avatarAdapter.avatarSelect?.apply {
                viewModel.updateProfile(
                    username = binding.etUsername.text.toString(),
                    avatarId = numberImage
                )
            }
        }
    }

    override fun onDestroyView() {
        binding.etUsername.removeTextChangedListener(textWatcher)
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = EditProfileBottomSheet()
    }
}
