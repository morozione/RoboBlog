package com.morozione.roboblog.ui.adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.morozione.roboblog.R
import com.morozione.roboblog.core.BlogType
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.utils.Utils

class BlogsAdapter(private var listType: BlogType, private var blogs: ArrayList<Blog>) :
    RecyclerView.Adapter<BlogsAdapter.ViewHolder>() {

    var onBlogClickListener: OnBlogClickListener? = null
    var onGlobalBlogListener: OnGlobalBlogListener? = null
    var onUserBlogListener: OnUserBlogListener? = null

    interface OnBlogClickListener {
        fun onBlogClick(blog: Blog)
    }

    interface OnGlobalBlogListener {
        fun onSetRating(blog: Blog, rating: Int)
    }

    interface OnUserBlogListener {
        fun onEdit(blog: Blog)
        fun onDelete(blog: Blog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (listType) {
            BlogType.GLOBAL -> GlobalViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_blog, parent, false
                )
            )
            BlogType.USER -> UserViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_user_blog, parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(blogs[position], position)
    }

    override fun getItemCount(): Int {
        return blogs.size
    }

    fun swapData(blogs: ArrayList<Blog>) {
        this.blogs = blogs
        notifyDataSetChanged()
    }

    fun addData(blogs: ArrayList<Blog>) {
        this.blogs.addAll(blogs)
        notifyDataSetChanged()
    }

    open inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val mTitle: TextView = rootView.findViewById(R.id.title)
        private val mDescription: TextView = rootView.findViewById(R.id.description)
        private val mDate: TextView = rootView.findViewById(R.id.date)
        private val mIcon: ImageView = rootView.findViewById(R.id.m_icon)

        open fun bind(blog: Blog, position: Int) {
            mTitle.text = blog.title
            mDescription.text = blog.descrption
            mDate.text = Utils.getFullDate(blog.date)

            if (blog.icon.isNotEmpty()) {
                mIcon.visibility = View.VISIBLE
                Glide.with(itemView.context).load(blog.icon)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            mIcon.visibility = View.GONE
                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                    }).into(mIcon)
            } else {
                mIcon.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onBlogClickListener?.onBlogClick(blog)
            }
        }
    }

    inner class GlobalViewHolder(rootView: View) : ViewHolder(rootView) {
        private val mRating: TextView = rootView.findViewById(R.id.m_rating)
        private val mArrowUp: ImageView = rootView.findViewById(R.id.m_arrow_up)
        private val mArrowDown: ImageView = rootView.findViewById(R.id.m_arrow_down)

        override fun bind(blog: Blog, position: Int) {
            super.bind(blog, position)
            val userAppreciateStatus = blog.getAppreciatedStatusByUser(UserDao.getCurrentUserId())
            mRating.text = "$userAppreciateStatus"
            userAppreciateStatus?.let {
                setRatingStatus(userAppreciateStatus)
            }
            mArrowUp.setOnClickListener {
                blog.appreciatedPeoples[UserDao.getCurrentUserId()] = 1
                onGlobalBlogListener?.onSetRating(blog, 1)
                blog.rating += 1
            }
            mArrowDown.setOnClickListener {
                blog.appreciatedPeoples[UserDao.getCurrentUserId()] = -1
                onGlobalBlogListener?.onSetRating(blog, -1)
                blog.rating += -1
            }
        }

        private fun setRatingStatus(userAppreciateStatus: Int) {
            when (userAppreciateStatus) {
                0 -> {
                    mArrowDown.isEnabled = true
                    mArrowUp.isEnabled = true
                    mArrowUp.setImageResource(R.drawable.ic_arrow_up_normal)
                    mArrowDown.setImageResource(R.drawable.ic_arrow_down_normal)
                }
                -1 -> {
                    mArrowDown.isEnabled = false
                    mArrowUp.isEnabled = false
                    mArrowUp.setImageResource(R.drawable.ic_arrow_up_normal)
                    mArrowDown.setImageResource(R.drawable.ic_arrow_down_active)
                }
                1 -> {
                    mArrowDown.isEnabled = false
                    mArrowUp.isEnabled = false
                    mArrowUp.setImageResource(R.drawable.ic_arrow_up_active)
                    mArrowDown.setImageResource(R.drawable.ic_arrow_down_normal)
                }
            }
        }
    }

    inner class UserViewHolder(rootView: View) : ViewHolder(rootView) {
        private val mEdit: ImageView = rootView.findViewById(R.id.m_edit)
        private val mRemove: ImageView = rootView.findViewById(R.id.m_remove)

        override fun bind(blog: Blog, position: Int) {
            super.bind(blog, position)
            mEdit.setOnClickListener {
                onUserBlogListener?.onEdit(blog)
            }
            mRemove.setOnClickListener {
                onUserBlogListener?.onDelete(blog)
                blogs.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }
}
