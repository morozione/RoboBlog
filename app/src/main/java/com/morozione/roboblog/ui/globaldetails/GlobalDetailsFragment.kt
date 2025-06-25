package com.morozione.roboblog.ui.globaldetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.presenter.InjectPresenter
import com.morozione.roboblog.R
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.databinding.BaseBlogDetailsFragmentBinding
import com.morozione.roboblog.databinding.ItemVoteBinding
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.ui.blogdetails.BaseBlogDetailsFragment
import com.morozione.roboblog.ui.userinfo.UserSmallInformationFragment
import com.morozione.roboblog.utils.changeFragmentTo

class GlobalDetailsFragment : BaseBlogDetailsFragment(), GlobalDetailsView {

    companion object {
        val TAG = GlobalDetailsFragment::class.java.name
    }
    
    override lateinit var binding: BaseBlogDetailsFragmentBinding
    private lateinit var footerBinding: ItemVoteBinding

    @InjectPresenter
    lateinit var globalDetailsPresenter: GlobalDetailsPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BaseBlogDetailsFragmentBinding.inflate(inflater, container, false)
        footerBinding = ItemVoteBinding.inflate(LayoutInflater.from(context), binding.mFooter, true)
        return binding.root
    }

    override fun onBlogUploaded(blog: Blog) {
        super.onBlogUploaded(blog)
        showUserData(blog.userId)
    }

    private fun showUserData(userId: String) {
        activity?.changeFragmentTo(
            R.id.m_footer_1,
            UserSmallInformationFragment.Companion.newInstance(userId),
            UserSmallInformationFragment.Companion.TAG
        )

        val userAppreciateStatus = blog?.getAppreciatedStatusByUser(UserDao.getCurrentUserId())
        footerBinding.mRating.text = "$userAppreciateStatus"
        userAppreciateStatus?.let {
            setRatingStatus(userAppreciateStatus)
        }

        footerBinding.mArrowUp.setOnClickListener {
            blog?.let { blog ->
                blog.appreciatedPeoples.set(UserDao.getCurrentUserId(), 1)
                blog.let { it1 -> globalDetailsPresenter.onSetRating(it1, 1) }
                blog.rating.plus(1)
            }
        }
        footerBinding.mArrowDown.setOnClickListener {
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
                footerBinding.mArrowDown.isEnabled = true
                footerBinding.mArrowUp.isEnabled = true
                footerBinding.mArrowUp.setImageResource(R.drawable.ic_arrow_up_normal)
                footerBinding.mArrowDown.setImageResource(R.drawable.ic_arrow_down_normal)
            }
            -1 -> {
                footerBinding.mArrowDown.isEnabled = false
                footerBinding.mArrowUp.isEnabled = false
                footerBinding.mArrowUp.setImageResource(R.drawable.ic_arrow_up_normal)
                footerBinding.mArrowDown.setImageResource(R.drawable.ic_arrow_down_active)
            }
            1 -> {
                footerBinding.mArrowDown.isEnabled = false
                footerBinding.mArrowUp.isEnabled = false
                footerBinding.mArrowUp.setImageResource(R.drawable.ic_arrow_up_active)
                footerBinding.mArrowDown.setImageResource(R.drawable.ic_arrow_down_normal)
            }
        }
    }

    override fun onRatingSuccess() {
        val userAppreciateStatus = blog?.getAppreciatedStatusByUser(UserDao.getCurrentUserId())
        footerBinding.mRating.text = "$userAppreciateStatus"
        userAppreciateStatus?.let {
            setRatingStatus(userAppreciateStatus)
        }
    }
}