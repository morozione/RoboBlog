package com.morozione.roboblog.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.morozione.roboblog.Constants
import com.morozione.roboblog.R

class ChatFragment : AppCompatActivity() {

    companion object {
        fun getInrent(blogId: String): Intent {
            val intent = Intent()
            intent.putExtra(Constants.EXTRA_ID, blogId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_fragment)
    }


}
