package com.sema.biirrapp.ui.binding

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sema.biirrapp.R

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

@BindingAdapter("ibu")
fun TextView.showIbu(ibu: Double?) {
    ibu?.let {
        val ibuRes = when {
            ibu<=20 -> R.string.beer_detail_smooth
            ibu<=50 && ibu>20 -> R.string.beer_detail_bitter
            ibu >= 50 -> R.string.beer_detail_hipsterPlus
            else -> return@let
        }
      text = context.getString(ibuRes)
    }
}

@SuppressLint("StringFormatInvalid")
@BindingAdapter("abv")
fun TextView.showAbv(abv: Double?) {
    abv?.let {
        text = context.getString(R.string.beer_detail_abv_percentage, abv.toString())
    }
}


