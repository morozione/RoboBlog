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
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.activity.BlogActivity
import com.morozione.roboblog.ui.adapter.BlogsAdapter
import com.morozione.roboblog.utils.showSnackbar

abstract class BlogsFragment : MvpAppCompatFragment() {

    protected var typeOfList = BlogsAdapter.TypeOfList.GLOBAL
    protected lateinit var adapter: BlogsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = BlogsAdapter(typeOfList, arrayListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_blogs, container, false)
        initView(rootView)
        setListener()

        return rootView
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
    }

    private fun openBlogDetails(blog: Blog) {
        val intent = Intent(context, BlogActivity::class.java)
        startActivity(BlogActivity.createBundleForBlog(intent, blog.id))
    }

    protected fun setBlogs(blogs: List<Blog>, isLoading: Boolean) {
        if (isLoading) {
            adapter.addData(ArrayList(blogs))
        } else {
            adapter.swapData(ArrayList(blogs))
        }
    }

    protected fun showError(messge: String) {
        view?.let { showSnackbar(it, messge, Snackbar.LENGTH_LONG) }
    }
}