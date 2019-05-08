package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.morozione.roboblog.R
import com.morozione.roboblog.entity.Blog
import kotlinx.android.synthetic.main.base_blog_details_fragment.*
import kotlinx.android.synthetic.main.item_blog_manage_buttons.*

class UserBlogDetailsFragment : BaseBlogDetailsFragment() {
    companion object {
        val TAG = UserBlogDetailsFragment::class.java.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LayoutInflater.from(context).inflate(R.layout.item_blog_manage_buttons, m_footer)
        setListeners()
    }

    private fun setListeners() {
        m_edit.setOnClickListener {

        }
        m_remove.setOnClickListener {

        }
    }

    override fun onBlogUploaded(blog: Blog) {
        super.onBlogUploaded(blog)
    }
}