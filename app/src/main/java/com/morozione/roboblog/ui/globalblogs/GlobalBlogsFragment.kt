package com.morozione.roboblog.ui.globalblogs

import android.content.Intent
import moxy.presenter.InjectPresenter
import com.morozione.roboblog.R
import com.morozione.roboblog.core.BlogType
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.blogs.BlogsFragment
import com.morozione.roboblog.ui.globalblogs.GlobalBlogsPresenter
import com.morozione.roboblog.ui.globalblogs.GlobalBlogsView
import com.morozione.roboblog.ui.chat.ChatFragment
import com.morozione.roboblog.ui.shared.adapter.BlogsAdapter

class GlobalBlogsFragment : BlogsFragment(), GlobalBlogsView {

    @InjectPresenter
    lateinit var presenter: GlobalBlogsPresenter

    init{
        blogType = BlogType.GLOBAL
    }

    override fun onStart() {
        super.onStart()

        adapter.onGlobalBlogListener = object : BlogsAdapter.OnGlobalBlogListener {
            override fun onOpenChat(blog: Blog) {
                startActivity(Intent(activity, ChatFragment::class.java))
            }

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

    override fun onUpdate() {
        presenter.loadBlogs()
    }

    override fun onError() {
        super.showError(getString(R.string.error))
    }

    override fun onRatingSuccess() {

    }
}
