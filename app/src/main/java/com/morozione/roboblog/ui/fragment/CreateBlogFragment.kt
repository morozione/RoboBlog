package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.TextUtils
import android.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import moxy.presenter.InjectPresenter
import com.morozione.roboblog.R
import com.morozione.roboblog.databinding.FragmentCreateBlogBinding
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.presenter.CreateBlogPresenter
import com.morozione.roboblog.mvp.view.CreateBlogView
import com.morozione.roboblog.utils.GlideApp
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
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
        if (imageUri == null) {
            context?.let { 
                GlideApp.with(it)
                    .load(blog.icon)
                    .into(binding.mIcon) 
            }
        }
    }

    override fun imageMade(imageUri: String?) {
        if (imageUri != null && !TextUtils.isEmpty(imageUri)) {
            try {
                // Load the image directly with Glide for immediate display
                Glide.with(requireContext())
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.mIcon)
            } catch (e: Exception) {
                // Fallback to bitmap loading if Glide fails
                try {
                    val bitmap = if (imageUri.startsWith("content://")) {
                        // Use URI-based method for content URIs
                        ImageUtil.decodeSampledBitmapFromUri(
                            requireContext(),
                            android.net.Uri.parse(imageUri),
                            300,
                            300
                        )
                    } else {
                        // Use file-based method for regular file paths
                        ImageUtil.decodeSampledBitmapFromResource(
                            imageUri,
                            300,
                            300
                        )
                    }
                    
                    if (bitmap != null) {
                        binding.mIcon.setImageBitmap(bitmap)
                    } else {
                        // If bitmap creation fails, show placeholder
                        binding.mIcon.setImageResource(R.drawable.ic_person)
                    }
                } catch (ex: Exception) {
                    // If both methods fail, show error
                    binding.mIcon.setImageResource(R.drawable.ic_person)
                }
            }
        }
    }
}
