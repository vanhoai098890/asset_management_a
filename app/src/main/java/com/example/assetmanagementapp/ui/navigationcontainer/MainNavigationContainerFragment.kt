package com.example.assetmanagementapp.ui.navigationcontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentNavigationContainerBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainNavigationContainerFragment : BaseFragment() {
    private var binding: FragmentNavigationContainerBinding? = null
    private val viewModel: MainNavigationContainerViewModel by viewModels()
    private val fragmentPagers: MutableList<PagerContainerFragment> by lazy {
        mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNavigationContainerBinding.inflate(inflater, container, false)
        binding?.run {
            data = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initViews()
        initActions()
        initObservers()
        return binding?.root
    }

    private fun initObservers() {
    }

    private fun handleCloseDragLayout(isOpen: Boolean) {
    }

    private fun initActions() {
        binding?.apply {
        }
    }

    private fun initViews() {
        binding?.apply {
            vpContainer.apply {
                adapter = MainNavigationVpAdapter(
                    this@MainNavigationContainerFragment,
                    level,
                    fragmentPagers
                )
                isUserInputEnabled = false
                offscreenPageLimit = 1
            }
            bottomNavigation.let {
                it.setOnItemSelectedListener { menuItem ->
                    if (menuItem.order == MainNavigationItem.SCAN.ordinal) {
                        return@setOnItemSelectedListener true
                    }
                    vpContainer.currentItem = when (menuItem.order + 1) {
                        MainNavigationItem.MESSENGER.ordinal, MainNavigationItem.PERSONAL.ordinal -> {
                            menuItem.order - 1
                        }
                        else -> {
                            menuItem.order
                        }
                    }
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    private fun initFragments() {
        MainNavigationItem.values().forEachIndexed { index, _ ->
            if (index == MainNavigationItem.SCAN.ordinal) {
                return@forEachIndexed
            }
            fragmentPagers.add(PagerContainerFragment.newInstance(index).apply {
                onCallApiComplete = {
                }
            })
        }
    }
}
