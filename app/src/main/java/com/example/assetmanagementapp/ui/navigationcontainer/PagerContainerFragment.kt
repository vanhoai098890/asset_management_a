package com.example.assetmanagementapp.ui.navigationcontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.ui.favourite.FavouriteFragment
import com.example.assetmanagementapp.ui.personal.PersonalFragment
import com.example.assetmanagementapp.ui.searchmain.SearchMainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagerContainerFragment : BaseFragment() {
    companion object {
        private const val KEY_TAB = "key_tab"
        internal fun newInstance(tab: Int) = PagerContainerFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_TAB, tab)
            }
        }
    }

    private var currentFragment: Fragment? = null
    private val viewModel by viewModels<PagerContainerViewModel>()
    internal var onCallApiComplete: () -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_no_navigation_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = viewModel.getToken()
        handleAddChildFragment(token)
    }

    private fun handleAddChildFragment(token: String) {
        if (currentFragment == null) {
            arguments?.also {
                currentFragment = when (it.getInt(KEY_TAB)) {
                    MainNavigationItem.SEARCH.ordinal -> SearchMainFragment()
                    MainNavigationItem.FAVOURITE.ordinal -> FavouriteFragment()
//                    MainNavigationItem.FAVOURITE.ordinal -> FavouriteFragment()
                    MainNavigationItem.PERSONAL.ordinal -> PersonalFragment()
                    else -> {
                        SearchMainFragment()
                    }
                }
            }
            currentFragment?.also {
                replaceInContainer(it, isAddBackStack = false, isEnableAnim = false)
            }
        }
    }
}
