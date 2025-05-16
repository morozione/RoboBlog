package com.morozione.roboblog.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.morozione.roboblog.R
import com.morozione.roboblog.core.BlogType
import com.morozione.roboblog.databinding.FragmentBlogsBinding
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.activity.BlogDetailsActivity
import com.morozione.roboblog.ui.adapter.BlogsAdapter
import com.morozione.roboblog.utils.showSnackbar
import moxy.MvpAppCompatFragment

abstract class BlogsFragment : MvpAppCompatFragment() {

    private lateinit var binding: FragmentBlogsBinding

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
        binding = FragmentBlogsBinding.inflate(inflater, container, false)
        initView(binding.root)

        return binding.root
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
        binding.mSwipeRefresh.setOnRefreshListener {
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
        binding.mSwipeRefresh.isRefreshing = true
    }

    protected fun hideProgress() {
        binding.mSwipeRefresh.isRefreshing = false
    }

    abstract fun onUpdate()
}