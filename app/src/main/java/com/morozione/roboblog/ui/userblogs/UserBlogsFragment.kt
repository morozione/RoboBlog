package com.morozione.roboblog.ui.userblogs


import android.content.Context
import android.os.Bundle
import android.view.View
import com.morozione.roboblog.R
import com.morozione.roboblog.core.BlogType
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.blogs.BlogsFragment
import com.morozione.roboblog.ui.shared.adapter.BlogsAdapter
import moxy.presenter.InjectPresenter

class UserBlogsFragment : BlogsFragment(), UserBlogsView {

    interface OnUserBlogListener {
        fun onEdit(blog: Blog)
        fun onCreateArticle()
    }

    @InjectPresenter
    lateinit var userBloPresenter: UserBlogsPresenter

    private lateinit var onUserBlogListener: OnUserBlogListener

    override fun onAttach(context: Context) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up create article button click listener
        setCreateArticleClickListener {
            onUserBlogListener.onCreateArticle()
        }
    }

    override fun onResume() {
        super.onResume()

        userBloPresenter.loadBlogsByUserId(UserDao.getCurrentUserId())
    }

    override fun shouldShowEmptyState(): Boolean {
        // Show empty state for user blogs when there are no articles
        return true
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
