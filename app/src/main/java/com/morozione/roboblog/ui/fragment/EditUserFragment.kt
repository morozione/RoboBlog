package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.morozione.roboblog.R
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.presenter.EditUserPresenter
import com.morozione.roboblog.presenter.view.EditUserView
import com.morozione.roboblog.utils.ImageUtil
import com.morozione.roboblog.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_edit_user.*

class EditUserFragment : BaseImageFragment(), EditUserView {

    @InjectPresenter
    lateinit var presenter: EditUserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.loadUser(UserDao.getCurrentUserId())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_edit_user, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        m_save.setOnClickListener {
            activity?.let { activity ->
                presenter.saveUser(activity, imageUri, getFilledUser())
            }
        }
        m_icon.setOnClickListener {
            makePhoto()
        }
    }

    private fun getFilledUser(): User {
        presenter.user.name = m_name.text.toString()
        presenter.user.id = UserDao.getCurrentUserId()
        return presenter.user
    }

    override fun onUserLoaded(user: User) {
        fillView(user)
    }

    private fun fillView(user: User) {
        m_name.setText(user.name)
        m_rating.text = "${user.rating}"
        context?.let { Glide.with(it).load(user.image).into(m_icon) }
    }

    override fun onUpdateSuccess(isSuccess: Boolean) {
        view?.let { showSnackbar(it, getString(R.string.saved), Snackbar.LENGTH_SHORT) }
    }

    override fun onError() {
        view?.let {
            showSnackbar(it, getString(R.string.something_was_wrong), Snackbar.LENGTH_SHORT)
        }
    }

    override fun imageMade(imageUri: String?) {
        if (imageUri != null && !TextUtils.isEmpty(imageUri.toString())) {
            val bitmap = ImageUtil.decodeSampledBitmapFromResource(BaseImageFragment.imageUri.toString(), 300, 300)
            m_icon.setImageBitmap(bitmap)
        }
        context?.let { Glide.with(it).load(BaseImageFragment.imageUri).into(m_icon) }
    }
}
