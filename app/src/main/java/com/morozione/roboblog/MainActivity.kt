package com.morozione.roboblog

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.adapter.TabAdapter
import com.morozione.roboblog.ui.fragment.CreateBlogFragment
import com.morozione.roboblog.ui.fragment.UserBlogsFragment
import com.morozione.roboblog.utils.BottomNavigationViewHelper
import com.morozione.roboblog.utils.bind
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UserBlogsFragment.OnUserBlogListener {

    private val mNavigation by bind<BottomNavigationView>(R.id.navigation)
    private val mContainer by bind<ViewPager>(R.id.container)

    private lateinit var tabAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()

        setListeners()
    }

    private fun initUI() {
        tabAdapter = TabAdapter(supportFragmentManager, 4)
        mContainer.adapter = tabAdapter
        BottomNavigationViewHelper.disableShiftMode(mNavigation)

        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
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
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

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
        tabAdapter.setSelectedItemPosition(TabAdapter.CREATE_BLOG_FRAGMENT_POSITION)
        mNavigation.menu.getItem(TabAdapter.CREATE_BLOG_FRAGMENT_POSITION).isChecked = true
        tabAdapter.setSelectedItemPosition(TabAdapter.CREATE_BLOG_FRAGMENT_POSITION + 1)
        mContainer.currentItem = TabAdapter.CREATE_BLOG_FRAGMENT_POSITION + 1

        val fragment = tabAdapter.getItem(TabAdapter.CREATE_BLOG_FRAGMENT_POSITION) as CreateBlogFragment
        fragment.setBlogForEdit(blog)
    }
}
