package com.morozione.roboblog.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.morozione.roboblog.entity.User

interface UserSmallInformationView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setUser(user: User)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
}