package com.morozione.roboblog.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface GlobalDetailsView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onRatingSuccess()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
}