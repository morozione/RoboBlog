package com.morozione.roboblog.ui.userinfo

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import com.morozione.roboblog.R
import com.morozione.roboblog.databinding.FragmentUserSmallInforgationBinding
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.ui.userinfo.UserSmallInformationPresenter
import com.morozione.roboblog.ui.userinfo.UserSmallInformationView
import com.morozione.roboblog.utils.showSnackbar

class UserSmallInformationFragment : MvpAppCompatFragment(), UserSmallInformationView {

    private lateinit var binding: FragmentUserSmallInforgationBinding

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
    ): View {
        binding = FragmentUserSmallInforgationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setUser(user: User) {
        Glide.with(requireContext())
            .load(user.image)
            .centerCrop()
            .into(binding.mIcon)
        binding.mName.text = user.name
        binding.mRating.mRating.text = "${user.rating}"
    }

    override fun onError() {
        showSnackbar(binding.mIcon, getString(R.string.something_was_wrong), Snackbar.LENGTH_SHORT)
    }
}
