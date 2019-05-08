package com.morozione.roboblog.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import io.reactivex.annotations.SchedulerSupport

interface GlobalDetailsView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onRatingSuccess()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
}