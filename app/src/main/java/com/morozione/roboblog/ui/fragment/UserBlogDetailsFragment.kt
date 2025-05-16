package com.morozione.roboblog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.morozione.roboblog.databinding.BaseBlogDetailsFragmentBinding
import com.morozione.roboblog.databinding.ItemBlogManageButtonsBinding
import com.morozione.roboblog.databinding.ItemVoteBinding
import com.morozione.roboblog.entity.Blog

class UserBlogDetailsFragment : BaseBlogDetailsFragment() {
    companion object {
        val TAG = UserBlogDetailsFragment::class.java.name
    }

    override lateinit var binding: BaseBlogDetailsFragmentBinding
    private lateinit var bindingItem: ItemBlogManageButtonsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BaseBlogDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingItem = ItemBlogManageButtonsBinding.inflate(layoutInflater, binding.mFooter, true)
        setListeners()
    }

    private fun setListeners() {
        bindingItem.mEdit.setOnClickListener {

        }
        bindingItem.mRemove.setOnClickListener {

        }
    }

    override fun onBlogUploaded(blog: Blog) {
        super.onBlogUploaded(blog)
    }
}