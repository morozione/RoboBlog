package com.morozione.roboblog.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.morozione.roboblog.ui.fragment.GlobalBlogsFragment
import com.morozione.roboblog.ui.fragment.CreateBlogFragment
import com.morozione.roboblog.ui.fragment.UserBlogsFragment
import com.morozione.roboblog.ui.fragment.EditUserFragment
import java.util.*

class TabAdapter(fm: FragmentManager, private val numberOfTabs: Int) : FragmentStatePagerAdapter(fm) {

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