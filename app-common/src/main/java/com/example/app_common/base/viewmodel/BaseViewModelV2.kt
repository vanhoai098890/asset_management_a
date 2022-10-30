package com.example.app_common.base.viewmodel

/**
 * ver2 BaseViewModel with extension dispatch state
 */
abstract class BaseViewModelV2<T : Any> : BaseViewModel() {
    val store by lazy {
        ViewStateStore(this.initState())
    }
    val currentState: T
        get() = store.state

    abstract fun initState(): T

    protected fun dispatchState(state: T) {
        store.dispatchState(state = state)
    }

    fun getOriginalState() = store.getOriginalState()
}
