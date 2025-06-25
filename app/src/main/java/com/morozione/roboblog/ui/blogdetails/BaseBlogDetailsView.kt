package com.morozione.roboblog.ui.blogdetails

import moxy.MvpView
import com.morozione.roboblog.entity.Blog
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface BaseBlogDetailsView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onBlogUploaded(blog: Blog)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onError()
}