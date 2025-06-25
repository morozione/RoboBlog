package com.morozione.roboblog.ui.login

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface LoginView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onAuthorizationResult(isSuccess: Boolean)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onRegistrationResult(isSuccess: Boolean)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onSavedUserSuccess(isSuccess: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
}
