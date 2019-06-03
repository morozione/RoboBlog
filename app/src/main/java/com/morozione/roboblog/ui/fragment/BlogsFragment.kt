package com.morozione.roboblog.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.morozione.roboblog.R
import com.morozione.roboblog.core.BlogType
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.activity.BlogDetailsActivity
import com.morozione.roboblog.ui.adapter.BlogsAdapter
import com.morozione.roboblog.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_blogs.*

abstract class BlogsFragment : MvpAppCompatFragment() {

    protected var blogType = BlogType.GLOBAL
    protected lateinit var adapter: BlogsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = BlogsAdapter(blogType, arrayListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_blogs, container, false)
        initView(rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }

    private fun initView(rootView: View) {
        val mRecyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_view)
        mRecyclerView.adapter = adapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setListener() {
        adapter.onBlogClickListener = object : BlogsAdapter.OnBlogClickListener {
            override fun onBlogClick(blog: Blog) {
                openBlogDetails(blog)
            }
        }
        m_swipe_refresh.setOnRefreshListener {
            onUpdate()
        }
    }

    private fun openBlogDetails(blog: Blog) {
        val intent = Intent(context, BlogDetailsActivity::class.java)
        startActivity(BlogDetailsActivity.createBundleForBlog(intent, blog.id, blogType))
    }

    protected fun setBlogs(blogs: List<Blog>, isLoading: Boolean) {
        hideProgress()

        if (isLoading) {
            adapter.addData(ArrayList(blogs))
        } else {
            adapter.swapData(ArrayList(blogs))
        }
    }

    protected fun showError(messge: String) {
        view?.let { showSnackbar(it, messge, Snackbar.LENGTH_LONG) }
    }

    protected fun showProgress() {
        m_swipe_refresh.isRefreshing = true
    }

    protected fun hideProgress() {
        m_swipe_refresh.isRefreshing = false
    }

    abstract fun onUpdate()
}