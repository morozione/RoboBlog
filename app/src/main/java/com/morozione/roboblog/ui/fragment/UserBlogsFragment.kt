package com.morozione.roboblog.ui.fragment


import android.content.Context
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozione.roboblog.R
import com.morozione.roboblog.core.BlogType
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.presenter.UserBlogsPresenter
import com.morozione.roboblog.mvp.view.UserBlogsView
import com.morozione.roboblog.ui.adapter.BlogsAdapter

class UserBlogsFragment : BlogsFragment(), UserBlogsView {

    interface OnUserBlogListener {
        fun onEdit(blog: Blog)
    }

    @InjectPresenter
    lateinit var userBloPresenter: UserBlogsPresenter

    private lateinit var onUserBlogListener: OnUserBlogListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnUserBlogListener) {
            onUserBlogListener = context
        } else {
            throw RuntimeException("Cant cast $context to ${OnUserBlogListener::class.java.simpleName}")
        }
    }

    init {
        blogType = BlogType.USER
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter.onUserBlogListener = object : BlogsAdapter.OnUserBlogListener {
            override fun onEdit(blog: Blog) {
                onUserBlogListener.onEdit(blog)
            }

            override fun onDelete(blog: Blog) {
                userBloPresenter.deleteBlog(blog.id)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        userBloPresenter.loadBlogsByUserId(UserDao.getCurrentUserId())
    }

    override fun onBlogsUploaded(blogs: List<Blog>, isLoading: Boolean) {
        super.setBlogs(blogs, isLoading)
    }

    override fun onDeleted() {

    }

    override fun onUpdate() {
        userBloPresenter.loadBlogsByUserId(UserDao.getCurrentUserId())
    }

    override fun onError() {
        super.showError(getString(R.string.error))
    }
}
