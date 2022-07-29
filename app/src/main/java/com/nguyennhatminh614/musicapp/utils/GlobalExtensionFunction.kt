package com.nguyennhatminh614.musicapp.utils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.nguyennhatminh614.musicapp.R
import java.util.concurrent.TimeUnit

class GlobalExtensionFunction {
    companion object{
        //Convert Id Image To Uri
        fun Long.createImageUriWithThisID(): Uri {
            return ContentUris.withAppendedId(Uri.parse(Constant.AUDIO_IMAGE_URI), this)
        }

        //Convert Long duration to date time string MM:SS
        fun Long.toDateTimeMMSS(): String{
            return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(this) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(this) % TimeUnit.HOURS.toMinutes(1))
        }

        //Load Glide Image with URI
        fun ImageView.loadGlideImageWithUri(context: Context, uri: Uri, isCircleCrop: Boolean = false){
            if(isCircleCrop){
                Glide.with(context).load(uri).placeholder(R.drawable.ic_baseline_music_note_24).circleCrop().into(this)
            }else{
                Glide.with(context).load(uri).placeholder(R.drawable.ic_baseline_music_note_24).into(this)
            }
        }

        //Update Angle in Rotate Circle Image
        fun Float.updateAngle(rotateAngle: Float) : Float{
            var x: Float = this
            x += rotateAngle
            x %= 360
            return x
        }
    }
}