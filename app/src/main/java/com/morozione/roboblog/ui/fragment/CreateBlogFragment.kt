package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.morozione.roboblog.R
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.presenter.CreateBlogPresenter
import com.morozione.roboblog.mvp.view.CreateBlogView
import com.morozione.roboblog.utils.ImageUtil
import com.morozione.roboblog.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_create_blog.*

class CreateBlogFragment : BaseImageFragment(), CreateBlogView {

    @InjectPresenter
    lateinit var presenter: CreateBlogPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_create_blog, container, false)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.m_save -> creteBlog()
            R.id.m_image -> makePhoto()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun creteBlog() {
        if (presenter.blog == null) {
            presenter.createBlog(constructBlog(Blog()))
        } else {
            presenter.updateBlog(constructBlog(presenter.blog!!))
        }
    }

    private fun constructBlog(blog: Blog): Blog {
        blog.title = m_title.text.toString()
        blog.descrption = m_description.text.toString()
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
        emptyField()
    }

    override fun onBlogUpdated() {
        view?.let { showSnackbar(it, getString(R.string.updated), Snackbar.LENGTH_SHORT) }
        emptyField()
        presenter.blog = null
    }

    private fun emptyField() {
        m_title.setText("")
        m_description.setText("")
        m_icon.setImageBitmap(null)
    }

    fun setBlogForEdit(blog: Blog) {
        presenter.blog = blog

        m_title.setText(blog.title)
        m_description.setText(blog.descrption)
        context?.let { Glide.with(it).load(BaseImageFragment.imageUri).into(m_icon) }
    }

    override fun imageMade(imageUri: String?) {
        if (imageUri != null && !TextUtils.isEmpty(imageUri.toString())) {
            val bitmap = ImageUtil.decodeSampledBitmapFromResource(
                BaseImageFragment.imageUri.toString(),
                300,
                300
            )
            m_icon.setImageBitmap(bitmap)
        }
//        context?.let { Glide.with(it).load(BaseImageFragment.imageUri).into(m_icon) }
    }
}
