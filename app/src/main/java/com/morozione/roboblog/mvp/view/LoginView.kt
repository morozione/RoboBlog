package com.morozione.roboblog.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface LoginView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onAuthorizationResult(isSuccess: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onRegistrationResult(isSuccess: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onSavedUserSuccess(isSuccess: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
}
