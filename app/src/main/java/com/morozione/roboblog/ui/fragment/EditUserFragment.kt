package com.morozione.roboblog.ui.fragment


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter

import com.morozione.roboblog.R
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.presenter.EditUserPresenter
import com.morozione.roboblog.presenter.view.EditUserView
import com.morozione.roboblog.utils.showSnackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_edit_user.view.*

class EditUserFragment : MvpAppCompatFragment(), EditUserView {

    @InjectPresenter
    lateinit var presenter: EditUserPresenter

    private lateinit var mIcon: CircleImageView
    private lateinit var mName: EditText
    private lateinit var mRating: TextView
    private lateinit var mSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.loadUser(UserDao.getCurrentUserId())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_edit_user, container, false)

        initView(rootView)
        setListeners()

        return rootView
    }

    private fun initView(view: View) {
        mIcon = view.findViewById(R.id.icon)
        mName = view.findViewById(R.id.name)
        mRating = view.findViewById(R.id.rating)
        mSave = view.findViewById(R.id.save)
    }

    private fun setListeners() {
        mSave.setOnClickListener {
            presenter.updateUser(getFilledUser())
        }
    }

    private fun getFilledUser(): User {
        presenter.user.name = mName.name.toString()
        return presenter.user
    }

    override fun onUserLoaded(user: User) {
        fillView(user)
    }

    private fun fillView(user: User) {
        mName.setText(user.name)
        mRating.text = "${user.rating}"
    }

    override fun onUpdateSuccess(isSuccess: Boolean) {
        view?.let { showSnackbar(it, getString(R.string.saved), Snackbar.LENGTH_SHORT) }
    }

    override fun onError() {
        view?.let { showSnackbar(it, getString(R.string.something_was_wrong), Snackbar.LENGTH_SHORT) }
    }
}
