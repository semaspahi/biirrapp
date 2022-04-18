package com.sema.biirrapp.ui.beer

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("showUrl")
fun ImageView.showImage(url: String?) {
    url?.let {
        Glide
            .with(context)
            .load(it)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)

    } ?: setImageDrawable(null)
}


