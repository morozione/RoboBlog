package com.morozione.roboblog.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class RoboBlogGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDefaultRequestOptions(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
} 