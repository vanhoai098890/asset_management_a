package com.example.assetmanagementapp.ui.searchmain.chart

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.example.app_common.base.BaseListAdapter
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.databinding.LayoutItemBarBinding

class ChartCategoryAdapter : BaseListAdapter<TypeAsset>(diffCallBack = diffUtil) {
    var onItemClick: (Float, Float, TypeAsset, View) -> Unit = { _, _, _, _ -> }
    var maxHeightChart: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder {
        return ViewHolder(
            LayoutItemBarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BaseItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when {
            payloads.isEmpty() -> {
                super.onBindViewHolder(holder, position, payloads)
            }
            payloads[0] == VALUE_CHANGE_PAYLOAD -> {
                (holder as? ViewHolder)?.updateValue(getItem(position))
            }
            else -> {
                //do nothing
            }
        }
    }

    private val animationSet = AnimatorSet()

    inner class ViewHolder(private val binding: LayoutItemBarBinding) :
        BaseItemViewHolder(binding.root) {

        override fun bind(data: TypeAsset) {
            super.bind(data)
            if (!data.isAnimated) {
                animationBar(data)
                data.isAnimated = true
            }
            binding.root.setSafeOnClickListener {
                onItemClick.invoke(
                    binding.root.right + (binding.root.width / 2).toFloat(),
                    binding.root.top.toFloat(),
                    getItem(adapterPosition),
                    binding.root
                )
            }
        }

        private fun animationBar(data: TypeAsset) {
            val verticalAnimation =
                ObjectAnimator.ofInt(
                    0,
                    (-data.totalPercent * maxHeightChart).toInt()
                )
            verticalAnimation.addUpdateListener {
                binding.customChartView.rectF.top = (it.animatedValue as Int) * 1f
                binding.customChartView.postInvalidate()
            }
            animationSet.duration = 1000
            animationSet.interpolator = LinearInterpolator()
            animationSet.play(verticalAnimation)
            animationSet.start()
        }

        fun updateValue(data: TypeAsset) {
            val verticalAnimation =
                ObjectAnimator.ofInt(
                    binding.customChartView.rectF.top.toInt(),
                    (-data.totalPercent * maxHeightChart).toInt()
                )
            verticalAnimation.addUpdateListener {
                binding.customChartView.rectF.top = (it.animatedValue as Int) * 1f
                binding.customChartView.postInvalidate()
            }
            animationSet.duration = 1000
            animationSet.interpolator = LinearInterpolator()
            animationSet.play(verticalAnimation)
            animationSet.start()
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseItemViewHolder) {
        animationSet.cancel()
        super.onViewDetachedFromWindow(holder)
    }

    companion object {
        private const val VALUE_CHANGE_PAYLOAD = "VALUE_CHANGE_PAYLOAD"
        private const val VALUE_NOT_CHANGE_PAYLOAD = "VALUE_NOT_CHANGE_PAYLOAD"
        private val diffUtil = object : BaseDiffUtilItemCallback<TypeAsset>() {
            override fun areContentsTheSame(oldItem: TypeAsset, newItem: TypeAsset): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: TypeAsset, newItem: TypeAsset): Boolean {
                return oldItem.id == newItem.id
            }

            override fun getChangePayload(oldItem: TypeAsset, newItem: TypeAsset): Any {
                return if (oldItem.totalPercent != newItem.totalPercent) {
                    VALUE_CHANGE_PAYLOAD
                } else {
                    VALUE_NOT_CHANGE_PAYLOAD
                }
            }
        }
    }
}
