package com.morozione.roboblog.ui.shared.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.morozione.roboblog.ui.createblog.CreateBlogFragment
import com.morozione.roboblog.ui.fragment.EditUserFragment
import com.morozione.roboblog.ui.globalblogs.GlobalBlogsFragment
import com.morozione.roboblog.ui.userblogs.UserBlogsFragment
import java.util.*

class TabAdapter(fm: FragmentManager, private val numberOfTabs: Int) : FragmentPagerAdapter(fm) {

    companion object {
        const val BLOGS_FRAGMENT_POSITION = 0
        const val USER_BLOG_FRAGMENT_POSITION = 1
        const val CREATE_BLOG_FRAGMENT_POSITION = 2
        const val USER_FRAGMENT_POSITION = 3
    }

    private val items = ArrayList<Int>()

    private val blogsFragment = GlobalBlogsFragment()
    private val userBlogsFragment = UserBlogsFragment()
    private val createBlogFragment = CreateBlogFragment()
    private val userFragment = EditUserFragment()

    override fun getCount(): Int {
        return numberOfTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            BLOGS_FRAGMENT_POSITION -> blogsFragment
            USER_BLOG_FRAGMENT_POSITION -> userBlogsFragment
            CREATE_BLOG_FRAGMENT_POSITION -> createBlogFragment
            USER_FRAGMENT_POSITION -> userFragment
            else -> throw Error("Unknown fragment: $position")
        }
    }

    fun setSelectedItemPosition(position: Int) {
        items.add(position)
    }
}