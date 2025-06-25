package com.morozione.roboblog.ui.blogdetails

import com.google.android.material.snackbar.Snackbar
import com.bumptech.glide.Glide
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import com.morozione.roboblog.Constants
import com.morozione.roboblog.R
import com.morozione.roboblog.databinding.BaseBlogDetailsFragmentBinding
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.blogdetails.BaseBlogDetailsPresenter
import com.morozione.roboblog.ui.blogdetails.BaseBlogDetailsView
import com.morozione.roboblog.utils.showSnackbar

abstract class BaseBlogDetailsFragment : MvpAppCompatFragment(), BaseBlogDetailsView {

    abstract var binding: BaseBlogDetailsFragmentBinding

    protected var blog: Blog? = null

    @InjectPresenter
    lateinit var baseBlogDetailsPresenter: BaseBlogDetailsPresenter

    override fun onResume() {
        super.onResume()
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

        binding.mTitle.text = blog.title
        binding.mDescription.text = blog.descrption
        Glide.with(requireContext())
            .load(blog.icon)
            .into(binding.mImage)
    }

    override fun onError() {
        showSnackbar(
            binding.mTitle,
            getString(R.string.error),
            Snackbar.LENGTH_LONG
        )
    }
}