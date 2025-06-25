package com.morozione.roboblog.ui.createblog

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface CreateBlogView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onBlogCreated()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onBlogUpdated()
}