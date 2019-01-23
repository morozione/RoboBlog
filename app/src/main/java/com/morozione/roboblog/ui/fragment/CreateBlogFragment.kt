package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.*
import android.widget.EditText
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozione.roboblog.R
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.presenter.CreateBlogPresenter
import com.morozione.roboblog.presenter.view.CreateBlogView
import com.morozione.roboblog.utils.bind
import com.morozione.roboblog.utils.showSnackbar

class CreateBlogFragment : MvpAppCompatFragment(), CreateBlogView {

    @InjectPresenter
    lateinit var presenter: CreateBlogPresenter

    private val mTitle by bind<EditText>(R.id.title)
    private val mDescription by bind<EditText>(R.id.description)

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
            R.id.save -> creteBlog()
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
        blog.title = mTitle.text.toString()
        blog.descrption = mDescription.text.toString()

        return blog
    }

    override fun onError() {
        view?.let { showSnackbar(it, getString(R.string.something_was_wrong), Snackbar.LENGTH_SHORT) }
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
        mTitle.setText("")
        mDescription.setText("")
    }

    fun setBlogForEdit(blog: Blog) {
        presenter.blog = blog

        mTitle.setText(blog.title)
        mDescription.setText(blog.descrption)
    }
}
