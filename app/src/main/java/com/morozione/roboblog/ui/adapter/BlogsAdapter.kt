package com.morozione.roboblog.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.morozione.roboblog.R
import com.morozione.roboblog.database.UserDao
import com.morozione.roboblog.entity.Blog
import com.morozione.roboblog.utils.Utils

class BlogsAdapter : RecyclerView.Adapter<BlogsAdapter.ViewHolder> {

    enum class TypeOfList {
        GLOBAL, USER,
    }

    var onBlogClickListener: OnBlogClickListener? = null
    var onGlobalBlogListener: OnGlobalBlogListener? = null
    var onUserBlogListener: OnUserBlogListener? = null
    private var blogs: ArrayList<Blog>
    private var typeOfList: TypeOfList

    interface OnBlogClickListener {
        fun onBlogClick(blog: Blog)
    }

    interface OnGlobalBlogListener {
        fun onSetRating(blog: Blog, rating: Int)
    }

    interface OnUserBlogListener {
        fun onEdit(blog: Blog)
    }

    constructor(typeOfList: TypeOfList, blogs: ArrayList<Blog>) {
        this.blogs = blogs
        this.typeOfList = typeOfList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (typeOfList) {
            TypeOfList.GLOBAL -> GlobalViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_blog, parent, false
                )
            )
            TypeOfList.USER -> UserViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_user_blog, parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(blogs[position])
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

        open fun bind(blog: Blog) {
            mTitle.text = blog.title
            mDescription.text = blog.descrption
            mDate.text = Utils.getFullDate(blog.date)

            itemView.setOnClickListener {
                if (onBlogClickListener != null)
                    onBlogClickListener!!.onBlogClick(blog)
            }
        }
    }

    inner class GlobalViewHolder(rootView: View) : ViewHolder(rootView) {
        private val mRating: TextView = rootView.findViewById(R.id.rating)
        private val mArrowUp: ImageView = rootView.findViewById(R.id.arrow_up)
        private val mArrowDown: ImageView = rootView.findViewById(R.id.arrow_down)

        override fun bind(blog: Blog) {
            super.bind(blog)
            val userAppreciateStatus = blog.getAppreciatedStatusByUser(UserDao.getCurrentUserId())
            mRating.text = "${blog.rating}"
            setRatingStatus(userAppreciateStatus)
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
        private val mEdit: ImageView = rootView.findViewById(R.id.edit)
        private val mRemove: ImageView = rootView.findViewById(R.id.remove)

        override fun bind(blog: Blog) {
            super.bind(blog)
            mEdit.setOnClickListener {
                onUserBlogListener?.onEdit(blog)
            }

        }
    }
}
