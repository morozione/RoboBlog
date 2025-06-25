package com.morozione.roboblog.ui.edituser

import moxy.MvpView
import com.morozione.roboblog.entity.User
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface EditUserView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onUserLoaded(user: User)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onUpdateSuccess(isSuccess: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onLogoutSuccess()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
}