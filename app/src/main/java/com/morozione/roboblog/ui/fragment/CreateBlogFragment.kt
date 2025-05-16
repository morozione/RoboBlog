package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.TextUtils
import android.view.*
import moxy.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.morozione.roboblog.R
import com.morozione.roboblog.databinding.FragmentCreateBlogBinding
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.presenter.CreateBlogPresenter
import com.morozione.roboblog.mvp.view.CreateBlogView
import com.morozione.roboblog.utils.ImageUtil
import com.morozione.roboblog.utils.showSnackbar

class CreateBlogFragment : BaseImageFragment(), CreateBlogView {

    private lateinit var binding: FragmentCreateBlogBinding

    @InjectPresenter
    lateinit var presenter: CreateBlogPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCreateBlogBinding.inflate(inflater, container, false).apply {
        binding = this
        root
    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.m_save -> creteBlog()
            R.id.m_image -> makePhoto()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun creteBlog() {
        if (presenter.blog == null) {
            activity?.let { activity ->
                val blog = constructBlog(Blog())
                presenter.createBlog(blog, blog.icon, activity)
            }
        } else {
            presenter.blog?.let { blog ->
                presenter.updateBlog(constructBlog(blog))
            }
        }
    }

    private fun constructBlog(blog: Blog): Blog {
        blog.title = binding.mTitle.text.toString()
        blog.descrption = binding.mDescription.text.toString()
        blog.icon = imageUri.toString()
        imageUri = null

        return blog
    }

    override fun onError() {
        view?.let {
            showSnackbar(
                it,
                getString(R.string.something_was_wrong),
                Snackbar.LENGTH_SHORT
            )
        }
    }

    override fun onBlogCreated() {
        view?.let { showSnackbar(it, getString(R.string.saved), Snackbar.LENGTH_SHORT) }
        imageUri = null
        emptyField()
    }

    override fun onBlogUpdated() {
        view?.let { showSnackbar(it, getString(R.string.updated), Snackbar.LENGTH_SHORT) }
        emptyField()
        presenter.blog = null
    }

    private fun emptyField() {
        binding.mTitle.setText("")
        binding.mDescription.setText("")
        binding.mIcon.setImageBitmap(null)
    }

    fun setBlogForEdit(blog: Blog) {
        presenter.blog = blog

        binding.mTitle.setText(blog.title)
        binding.mDescription.setText(blog.descrption)
        if (imageUri == null)
            context?.let { Glide.with(it).load(blog.icon).into(binding.mIcon) }
    }

    override fun imageMade(imageUri: String?) {
        if (imageUri != null && !TextUtils.isEmpty(imageUri.toString())) {
            val bitmap = ImageUtil.decodeSampledBitmapFromResource(
                BaseImageFragment.imageUri.toString(),
                300,
                300
            )
            binding.mIcon.setImageBitmap(bitmap)
        }
//        context?.let { Glide.with(it).load(BaseImageFragment.imageUri).into(binding.mIcon) }
    }
}
