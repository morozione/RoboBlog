package com.morozione.roboblog.mvp.view

import moxy.MvpView
import com.morozione.roboblog.entity.Blog
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface UserBlogsView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onBlogsUploaded(blogs: List<Blog>, isLoading: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onDeleted()
}