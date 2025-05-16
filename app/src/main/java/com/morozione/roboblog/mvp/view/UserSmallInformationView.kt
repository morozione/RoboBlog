package com.morozione.roboblog.mvp.view

import moxy.MvpView
import com.morozione.roboblog.entity.User
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface UserSmallInformationView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setUser(user: User)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
}