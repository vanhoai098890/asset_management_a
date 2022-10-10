package com.example.assetmanagementapp.ui.navigationcontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.app_common.base.viewmodel.DaggerLayoutViewModel
import com.example.app_common.ui.drawflower.ElasticDrawer
import com.example.assetmanagementapp.MainNoNavigationContainerFragment
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentNavigationContainerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class MainNavigationContainerFragment : BaseFragment() {
    private var binding: FragmentNavigationContainerBinding? = null
    private val viewModel: MainNavigationContainerViewModel by viewModels()
    private val daggerLayoutViewModel: DaggerLayoutViewModel by viewModels()

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
        daggerLayoutViewModel.stateOpenDragLayout.onEach { handleCloseDragLayout(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleCloseDragLayout(isOpen: Boolean) {
        if (binding?.drawerlayout?.isMenuVisible == true && !isOpen) {
            binding?.drawerlayout?.closeMenu(true)
        }
    }

    private fun initActions() {
        binding?.apply {
        }
    }

    private fun initViews() {
        binding?.drawerlayout?.apply {
            setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL)
            setOnDrawerStateChangeListener(object : ElasticDrawer.OnDrawerStateChangeListener {
                override fun onDrawerStateChange(oldState: Int, newState: Int) {
                    if (newState == ElasticDrawer.STATE_OPENING || newState == ElasticDrawer.STATE_OPEN) {
                        daggerLayoutViewModel.stateOpenDragLayout.value = true
                    }
                }

                override fun onDrawerSlide(openRatio: Float, offsetPixels: Int) {}

            })
        }
    }

    private fun initFragments() {
        replaceInContainer(fragment = MainNoNavigationContainerFragment(), isAddBackStack = false)
    }

    override fun handleDragLayoutInContainer(): Boolean {
        binding?.drawerlayout?.apply {
            if (isMenuVisible) {
                closeMenu(true)
                return true
            }
        }
        return false
    }
}
