package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozione.roboblog.R
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.presenter.GlobalBlogsPresenter
import com.morozione.roboblog.presenter.view.GlobalBlogsView
import com.morozione.roboblog.ui.adapter.BlogsAdapter

class GlobalBlogsFragment : BlogsFragment(), GlobalBlogsView {

    @InjectPresenter
    lateinit var presenter: GlobalBlogsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setTypeOfList(BlogsAdapter.TypeOfList.GLOBAL)
    }

    override fun onStart() {
        super.onStart()

        adapter.onGlobalBlogListener = object : BlogsAdapter.OnGlobalBlogListener {
            override fun onSetRating(blog: Blog, rating: Int) {
                presenter.setRating(blog, rating)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadBlogs()
    }

    override fun onBlogsUploaded(blogs: List<Blog>, isLoading: Boolean) {
        super.setBlogs(blogs, isLoading)
    }

    override fun onError() {
        super.showError(getString(R.string.error))
    }

    override fun onRatingSuccess() {

    }
}
