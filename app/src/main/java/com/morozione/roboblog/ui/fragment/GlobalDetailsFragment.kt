package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozione.roboblog.R
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.mvp.presenter.GlobalDetailsPresenter
import com.morozione.roboblog.mvp.view.GlobalDetailsView
import com.morozione.roboblog.utils.changeFragmentTo
import kotlinx.android.synthetic.main.base_blog_details_fragment.*
import kotlinx.android.synthetic.main.item_vote.*

class GlobalDetailsFragment : BaseBlogDetailsFragment(), GlobalDetailsView {

    companion object {
        val TAG = GlobalDetailsFragment::class.java.name
    }

    @InjectPresenter
    lateinit var globalDetailsPresenter: GlobalDetailsPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LayoutInflater.from(context).inflate(R.layout.item_vote, m_footer)
    }

    override fun onBlogUploaded(blog: Blog) {
        super.onBlogUploaded(blog)
        showUserData(blog.userId)
    }

    private fun showUserData(userId: String) {
        activity?.changeFragmentTo(
            R.id.m_footer_1,
            UserSmallInformationFragment.newInstance(userId),
            UserSmallInformationFragment.TAG
        )

        val userAppreciateStatus = blog?.getAppreciatedStatusByUser(UserDao.getCurrentUserId())
        m_rating.text = "$userAppreciateStatus"
        userAppreciateStatus?.let {
            setRatingStatus(userAppreciateStatus)
        }

        m_arrow_up.setOnClickListener {
            blog?.let { blog ->
                blog.appreciatedPeoples.set(UserDao.getCurrentUserId(), 1)
                blog.let { it1 -> globalDetailsPresenter.onSetRating(it1, 1) }
                blog.rating.plus(1)
            }
        }
        m_arrow_down.setOnClickListener {
            blog?.let { blog ->
                blog.appreciatedPeoples.set(UserDao.getCurrentUserId(), -1)
                globalDetailsPresenter.onSetRating(blog, -1)
                blog.rating.plus(-1)
            }
        }
    }

    private fun setRatingStatus(userAppreciateStatus: Int) {
        when (userAppreciateStatus) {
            0 -> {
                m_arrow_down.isEnabled = true
                m_arrow_up.isEnabled = true
                m_arrow_up.setImageResource(R.drawable.ic_arrow_up_normal)
                m_arrow_down.setImageResource(R.drawable.ic_arrow_down_normal)
            }
            -1 -> {
                m_arrow_down.isEnabled = false
                m_arrow_up.isEnabled = false
                m_arrow_up.setImageResource(R.drawable.ic_arrow_up_normal)
                m_arrow_down.setImageResource(R.drawable.ic_arrow_down_active)
            }
            1 -> {
                m_arrow_down.isEnabled = false
                m_arrow_up.isEnabled = false
                m_arrow_up.setImageResource(R.drawable.ic_arrow_up_active)
                m_arrow_down.setImageResource(R.drawable.ic_arrow_down_normal)
            }
        }
    }

    override fun onRatingSuccess() {
        val userAppreciateStatus = blog?.getAppreciatedStatusByUser(UserDao.getCurrentUserId())
        m_rating.text = "$userAppreciateStatus"
        userAppreciateStatus?.let {
            setRatingStatus(userAppreciateStatus)
        }
    }
}