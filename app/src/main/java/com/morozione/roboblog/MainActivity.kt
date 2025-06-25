package com.morozione.roboblog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.shared.adapter.TabAdapter
import com.morozione.roboblog.ui.createblog.CreateBlogFragment
import com.morozione.roboblog.ui.userblogs.UserBlogsFragment
import com.morozione.roboblog.utils.BottomNavigationViewHelper
import com.morozione.roboblog.utils.bind

class MainActivity : AppCompatActivity(), UserBlogsFragment.OnUserBlogListener {

    private val mNavigation by bind<BottomNavigationView>(R.id.navigation)
    private val mContainer by bind<ViewPager>(R.id.m_container)

    private lateinit var tabAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()

        setListeners()
    }

    private fun initUI() {
        tabAdapter = TabAdapter(supportFragmentManager, 4)
        mContainer.offscreenPageLimit = 4
        mContainer.adapter = tabAdapter
        BottomNavigationViewHelper.disableShiftMode(mNavigation)

        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
    }

    private fun setListeners() {
        mNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.blogs -> {
                    tabAdapter.setSelectedItemPosition(1)
                    mContainer.currentItem = 0
                }
                R.id.user_blogs -> {
                    tabAdapter.setSelectedItemPosition(1)
                    mContainer.currentItem = 1
                }
                R.id.create_blog -> {
                    tabAdapter.setSelectedItemPosition(2)
                    mContainer.currentItem = 2
                }
                R.id.user -> {
                    tabAdapter.setSelectedItemPosition(3)
                    mContainer.currentItem = 3
                }
            }
            false
        }
        mContainer.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                tabAdapter.setSelectedItemPosition(position)
                mNavigation.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun onEdit(blog: Blog) {
        mNavigation.menu.getItem(TabAdapter.CREATE_BLOG_FRAGMENT_POSITION).isChecked = true

        tabAdapter.setSelectedItemPosition(TabAdapter.CREATE_BLOG_FRAGMENT_POSITION)
        mContainer.currentItem = TabAdapter.CREATE_BLOG_FRAGMENT_POSITION

        val fragment =
            tabAdapter.getItem(TabAdapter.CREATE_BLOG_FRAGMENT_POSITION) as CreateBlogFragment
        fragment.setBlogForEdit(blog)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }
}
