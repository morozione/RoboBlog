package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.morozione.roboblog.R
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.databinding.FragmentEditUserBinding
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.mvp.presenter.EditUserPresenter
import com.morozione.roboblog.mvp.view.EditUserView
import com.morozione.roboblog.utils.ImageUtil
import com.morozione.roboblog.utils.showSnackbar
import moxy.presenter.InjectPresenter

class EditUserFragment : BaseImageFragment(), EditUserView {
    
    private lateinit var binding: FragmentEditUserBinding 

    @InjectPresenter
    lateinit var presenter: EditUserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.loadUser(UserDao.getCurrentUserId())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.mSave.setOnClickListener {
            activity?.let { activity ->
                presenter.saveUser(activity, imageUri, getFilledUser())
            }
        }
        binding.mIcon.setOnClickListener {
            makePhoto()
        }
    }

    private fun getFilledUser(): User {
        presenter.user.name = binding.mName.text.toString()
        presenter.user.id = UserDao.getCurrentUserId()
        return presenter.user
    }

    override fun onUserLoaded(user: User) {
        fillView(user)
    }

    private fun fillView(user: User) {
        binding.mName.setText(user.name)
        binding.mRating.mRating.text = "${user.rating}"
        if (!user.image.isNullOrEmpty()) {
            loadImage(user.image)
        }
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(requireContext())
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .into(binding.mIcon)
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
        if (imageUri != null && !TextUtils.isEmpty(imageUri)) {
            try {
                // Load the image directly with Glide for immediate display
                Glide.with(requireContext())
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(binding.mIcon)
            } catch (e: Exception) {
                // Fallback to bitmap loading if Glide fails
                try {
                    val bitmap = if (imageUri.startsWith("content://")) {
                        // Use URI-based method for content URIs
                        ImageUtil.decodeSampledBitmapFromUri(
                            requireContext(),
                            android.net.Uri.parse(imageUri),
                            300,
                            300
                        )
                    } else {
                        // Use file-based method for regular file paths
                        ImageUtil.decodeSampledBitmapFromResource(
                            imageUri,
                            300,
                            300
                        )
                    }
                    
                    if (bitmap != null) {
                        binding.mIcon.setImageBitmap(bitmap)
                    } else {
                        // If bitmap creation fails, show placeholder
                        binding.mIcon.setImageResource(R.drawable.ic_person)
                    }
                } catch (ex: Exception) {
                    // If both methods fail, show error
                    binding.mIcon.setImageResource(R.drawable.ic_person)
                }
            }
        }
    }
}
