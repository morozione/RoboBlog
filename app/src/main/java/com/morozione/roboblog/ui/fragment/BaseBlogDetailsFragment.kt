package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.morozione.roboblog.Constants
import com.morozione.roboblog.R
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.presenter.BaseBlogDetailsPresenter
import com.morozione.roboblog.mvp.view.BaseBlogDetailsView
import com.morozione.roboblog.utils.showSnackbar
import kotlinx.android.synthetic.main.base_blog_details_fragment.*

abstract class BaseBlogDetailsFragment : MvpAppCompatFragment(), BaseBlogDetailsView {

    protected var blog: Blog? = null

    @InjectPresenter
    lateinit var baseBlogDetailsPresenter: BaseBlogDetailsPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.base_blog_details_fragment, container, false)

    override fun onStart() {
        super.onStart()

        loadData()
    }

    private fun loadData() {
        activity?.intent?.getStringExtra(Constants.EXTRA_ID)?.let {
            baseBlogDetailsPresenter.loadBlog(it)
        }
    }

    override fun onBlogUploaded(blog: Blog) {
        fillData(blog)
    }

    private fun fillData(blog: Blog) {
        this.blog = blog

        m_title.text = blog.title
        m_description.text = blog.descrption
        Glide.with(this).load(blog.icon).into(m_image)
    }

    override fun onError() {
        showSnackbar(
            m_title,
            getString(R.string.error),
            Snackbar.LENGTH_LONG
        )
    }
}