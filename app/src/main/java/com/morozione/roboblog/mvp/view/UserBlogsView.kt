package com.morozione.roboblog.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.morozione.roboblog.entity.Blog

interface UserBlogsView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onBlogsUploaded(blogs: List<Blog>, isLoading: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onDeleted()
}