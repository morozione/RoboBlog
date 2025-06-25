package com.morozione.roboblog.ui.blogdetails

import android.content.Intent
import android.os.Bundle
import moxy.MvpAppCompatActivity
import com.morozione.roboblog.Constants
import com.morozione.roboblog.R
import com.morozione.roboblog.core.BlogType
import com.morozione.roboblog.ui.globaldetails.GlobalDetailsFragment
import com.morozione.roboblog.ui.blogdetails.UserBlogDetailsFragment
import com.morozione.roboblog.utils.changeFragmentTo

class BlogDetailsActivity : MvpAppCompatActivity() {

    companion object {
        fun createBundleForBlog(intent: Intent, id: String, blogType: BlogType): Intent {
            intent.putExtra(Constants.EXTRA_ID, id)
            intent.putExtra(Constants.EXTRA_BLOG_TYPE, blogType.id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blog_activity)

        showFragment()
    }

    private fun showFragment() {
        when(intent?.extras?.getInt(Constants.EXTRA_BLOG_TYPE)) {
            BlogType.GLOBAL.id -> changeFragmentTo(GlobalDetailsFragment(), GlobalDetailsFragment.TAG)
            BlogType.USER.id -> changeFragmentTo(UserBlogDetailsFragment(), UserBlogDetailsFragment.TAG)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
