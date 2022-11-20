package com.example.assetmanagementapp.ui.searchmain

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.ui.tooltips.TipListener
import com.example.app_common.ui.tooltips.ToolTip
import com.example.app_common.ui.tooltips.ToolTipsManager
import com.example.app_common.ui.tooltips.ToolTipsManager.Companion.parseTextToKVND
import com.example.app_common.utils.ScreenUtils.getScreenWidth
import com.example.app_common.utils.bindingadapter.dpToPx
import com.example.assetmanagementapp.MainViewModel
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.data.remote.api.model.infomain.InfoMain
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.databinding.FragmentSearchMainBinding
import com.example.assetmanagementapp.ui.department.DepartmentFragment
import com.example.assetmanagementapp.ui.searchmain.chart.ChartCategoryAdapter
import com.example.assetmanagementapp.ui.searchresult.SearchResultFragment
import com.example.assetmanagementapp.utils.bindImageAvatar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMainFragment : BaseFragment(), TipListener {

    private lateinit var animatorSet: AnimatorSet
    private val viewModel: SearchMainViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var binding: FragmentSearchMainBinding? = null
    private val chartAdapter: ChartCategoryAdapter by lazy {
        ChartCategoryAdapter().apply {
            onItemClick = { x, y, it, chartView ->
                binding?.apply {
                    toolTipsManager?.findAndDismiss(constraintParentLayout)
                    when {
                        toolTipsManager?.isShow == false -> {
                            viewModel.stateOldIndex = it.id
                            setToolTip(
                                x = x,
                                y = y + rvChart.top,
                                category = it.typeName,
                                value = it.totalDepreciation,
                                view = chartView
                            )
                        }
                        it.id != viewModel.stateOldIndex -> {
                            viewModel.stateOldIndex = it.id
                            setToolTip(
                                x = x,
                                y = y + rvChart.top,
                                category = it.typeName,
                                value = it.totalDepreciation,
                                view = chartView
                            )
                        }
                        else -> {
                            //other case
                        }
                    }
                }
            }
        }
    }
    private var toolTipsManager: ToolTipsManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMainBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initEvents()
    }

    private fun initData() {
        toolTipsManager = ToolTipsManager(this)
        binding?.apply {
            parentLayout.setOnScrollChangeListener { _, _, _, _, _ ->
                toolTipsManager?.findAndDismiss(rvChart)
            }
            rvChart.setOnScrollChangeListener { _, _, _, _, _ ->
                toolTipsManager?.findAndDismiss(rvChart)
            }
            rvChart.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = chartAdapter
            }
        }
        binding?.layoutAssetInfo?.apply {
            ivAssetInfo.setBackgroundResource(R.drawable.ic_baseline_circle_geyser_24)
            ivAssetInfo.setImageResource(R.drawable.ic_search_outline)
            tvTitleInfo.text = getString(R.string.assets)
            tvDetailInfo.text = getString(R.string.view_all_assets)
            root.setSafeOnClickListener {
                addNoNavigationFragment(SearchResultFragment.newInstance(""))
            }
        }
        binding?.layoutDepartment?.apply {
            ivAssetInfo.setBackgroundResource(R.drawable.ic_baseline_circle_snuff_24)
            ivAssetInfo.setImageResource(R.drawable.ic_baseline_add_location_24)
            tvTitleInfo.text = getString(R.string.department)
            tvDetailInfo.text = getString(R.string.view_all_department)
            root.setSafeOnClickListener {
                addNoNavigationFragment(DepartmentFragment())
            }
        }
        binding?.layoutCateInfo?.apply {
            ivAssetInfo.setBackgroundResource(R.drawable.ic_baseline_circle_pampas_24)
            ivAssetInfo.setImageResource(R.drawable.ic_outline_category_24)
            tvTitleInfo.text = getString(R.string.v1_category)
            tvDetailInfo.text = getString(R.string.view_all_categories)
        }
        binding?.layoutTotalPrice?.apply {
            ivAssetInfo.setBackgroundResource(R.drawable.ic_baseline_circle_bizarre_24)
            ivAssetInfo.setImageResource(R.drawable.ic_amount)
            tvTitleInfo.text = getString(R.string.total_price)
            tvDetailInfo.text = getString(R.string.total_price_description)
        }
    }

    private fun initEvents() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.stateUserInfo.collect { handleShowUIWithUserInfo(it) }
            }
            launch {
                viewModel.listCategory.collect { handleShowChart(it) }
            }
            launch {
                viewModel.stateValueTotalDepreciation.collect { handleShowTotalDepreciation(it) }
            }
            launch {
                viewModel.stateInfoMain.collect { it?.let { handleShowInfoMain(it) } }
            }
            launch {
                viewModel.loadingState().collect { handleShowLoadingDialog(it) }
            }
        }
        mainViewModel.store.apply {
            observe(
                owner = this@SearchMainFragment,
                selector = { state -> state.stateClickedHome },
                observer = {
                    if (it) {
                        viewModel.getCustomerInfo()
                        viewModel.getListCategory()
                        viewModel.getInfoMain()
                        mainViewModel.dispatchClickHome(false)
                    }
                })
        }
    }

    private fun handleShowInfoMain(infoMain: InfoMain) {
        binding?.apply {
            layoutAssetInfo.tvValueInfo.text = infoMain.assetNumber.toString()
            layoutCateInfo.tvValueInfo.text = infoMain.categoryNumber.toString()
            layoutDepartment.tvValueInfo.text = infoMain.locationNumber.toString()
            layoutTotalPrice.tvValueInfo.text =
                parseTextToKVND(infoMain.totalPrice.toInt().toString())
        }
    }

    override fun handleShowLoadingDialog(isStateShow: Boolean) {
        if (isStateShow) {
            binding?.loadingDialog?.visibility = View.VISIBLE
        } else {
            binding?.loadingDialog?.visibility = View.GONE
        }
    }

    private fun handleShowTotalDepreciation(totalDepreciation: Float) {
        binding?.apply {
            animatorSet = AnimatorSet()
            val animationCash =
                ObjectAnimator.ofInt(tvTotal, "number", totalDepreciation.toInt())
            animationCash.duration = 1000
            animationCash.interpolator = LinearInterpolator()
            animationCash.addUpdateListener {
                tvTotal.setNumber(it.animatedValue as Int)
            }
            animatorSet.play(animationCash)
            animatorSet.start()
        }
    }

    var spaceItemDecoration: RecyclerView.ItemDecoration? = null
    private fun handleShowChart(listCategories: List<TypeAsset>) {
        binding?.apply {
            context?.apply {
                if (listCategories.isNotEmpty()) {
                    val space =
                        (getScreenWidth(context = this) -
                                // margin start and padding horizontal of chart
                                R.dimen._8dp.dpToPx(context) * 4 -
                                // margin horizontal of parent chart
                                R.dimen._16dp.dpToPx(context) -
                                // total width of chart views
                                R.dimen._30dp.dpToPx(context) * listCategories.size) / (listCategories.size - 1)
                    spaceItemDecoration?.let { rvChart.removeItemDecoration(it) }
                    spaceItemDecoration = object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            if (parent.getChildAdapterPosition(view) == 0) {
                                return
                            }
                            outRect.left =
                                if (space < R.dimen._16dp.dpToPx(context)) {
                                    R.dimen._16dp.dpToPx(context)
                                } else {
                                    space
                                }
                        }
                    }
                    spaceItemDecoration?.let { rvChart.addItemDecoration(it) }
                    chartAdapter.apply {
                        maxHeightChart = rvChart.height
                    }.submitList(listCategories)
                }
            }
        }
    }

    private fun handleShowUIWithUserInfo(userInfo: UserInfo?) {
        userInfo?.apply {
            binding?.apply {
                tvUserName.text =
                    resources.getString(R.string.tv_hi_account, userInfo.username)
                ivAvatar.bindImageAvatar(avatarId)
            }
        } ?: kotlin.run {
            binding?.apply {
                tvUserName.text =
                    resources.getString(R.string.tv_hi_account, "Hoài Văn")
            }
        }
    }

    private fun setToolTip(x: Float, y: Float, category: String, value: Float, view: View) {
        binding?.apply {
            toolTipsManager?.findAndDismiss(rvChart)
            toolTipsManager?.setStartEndTextChangeSize(category.length)
            val builder: ToolTip.Builder =
                ToolTip.Builder(
                    requireContext(),
                    view,
                    constraintParentLayout,
                    "$category\n${value.toInt()}",
                    ToolTip.POSITION_ABOVE
                )
            builder.setAlign(
                ToolTip.ALIGN_CENTER
            )
            builder.setGravity(ToolTip.GRAVITY_CENTER)
            builder.setOffsetX(x.toInt())
            builder.setOffsetY(y.toInt())
            builder.setBackgroundColor(
                resources.getColor(
                    R.color.white,
                    resources.newTheme()
                )
            )
            context?.let { toolTipsManager?.show(builder, it) }
        }
    }

    override fun onResume() {
        viewModel.getCustomerInfo()
        viewModel.getListCategory()
        viewModel.getInfoMain()
        super.onResume()
    }

    override fun onTipDismissed(view: View?, anchorViewId: Int, byUser: Boolean) {
        // do no thing
    }
}
