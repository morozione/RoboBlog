package com.morozione.roboblog.mvp.view

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
    fun onError()
}