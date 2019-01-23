package com.morozione.roboblog.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.ViewGroup
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozione.roboblog.Constants
import com.morozione.roboblog.R
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.presenter.BlogPresenter
import com.morozione.roboblog.presenter.view.BlogView
import com.morozione.roboblog.utils.bind
import com.morozione.roboblog.utils.showSnackbar

class BlogActivity : MvpAppCompatActivity(), BlogView {

    @InjectPresenter
    lateinit var blogPresenter: BlogPresenter

    private val mTitle by bind<TextView>(R.id.title)
    private val mDescription by bind<TextView>(R.id.description)

    private lateinit var id: String

    companion object {
        fun createBundleForBlog(intent: Intent, id: String): Intent {
            intent.putExtra(Constants.EXTRA_ID, id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)

        getBundleData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun getBundleData() {
        id = intent.getStringExtra(Constants.EXTRA_ID)
    }

    private fun loadData() {
        blogPresenter.loadBlog(id)
    }

    override fun onBlogUploaded(blog: Blog) {
        fillData(blog)
    }

    private fun fillData(blog: Blog) {
        mTitle.text = blog.title
        mDescription.text = blog.descrption
    }

    override fun onError() {
        showSnackbar(findViewById<ViewGroup>(R.id.content), getString(R.string.error), Snackbar.LENGTH_LONG)
    }
}
