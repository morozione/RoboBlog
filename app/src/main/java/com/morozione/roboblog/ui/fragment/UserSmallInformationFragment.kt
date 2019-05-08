package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.morozione.roboblog.R
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.mvp.presenter.UserSmallInformationPresenter
import com.morozione.roboblog.mvp.view.UserSmallInformationView
import com.morozione.roboblog.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_user_small_inforgation.*
import kotlinx.android.synthetic.main.item_rating.*

class UserSmallInformationFragment : MvpAppCompatFragment(), UserSmallInformationView {

    companion object {
        val TAG = this::class.java.name
        const val EXTRA_USER_ID = "user_id"

        fun newInstance(userId: String): UserSmallInformationFragment {
            val bundle = Bundle()
            bundle.putString(EXTRA_USER_ID, userId)
            val fragment = UserSmallInformationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var userSmallInformationPresenter: UserSmallInformationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadData()
    }

    private fun loadData() {
        arguments?.getString(EXTRA_USER_ID)?.let {
            userSmallInformationPresenter.loadUser(it)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_user_small_inforgation, container, false)


    override fun setUser(user: User) {
        Glide.with(m_icon).load(user.image).centerCrop().into(m_icon)
        m_name.text = user.name
        m_rating.text = "${user.rating}"
    }

    override fun onError() {
        showSnackbar(m_icon, getString(R.string.something_was_wrong), Snackbar.LENGTH_SHORT)
    }
}
