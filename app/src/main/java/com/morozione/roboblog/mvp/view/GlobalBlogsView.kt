package com.morozione.roboblog.mvp.view

import moxy.MvpView
import com.morozione.roboblog.entity.Blog
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface GlobalBlogsView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onBlogsUploaded(blogs: List<Blog>, isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onRatingSuccess()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
}