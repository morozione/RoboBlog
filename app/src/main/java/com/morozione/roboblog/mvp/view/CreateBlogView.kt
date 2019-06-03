package com.morozione.roboblog.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface CreateBlogView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onBlogCreated()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onBlogUpdated()
}