package com.nguyennhatminh614.musicapp.utils

import android.media.MediaPlayer

class MyMusicPlayer private constructor() {
    private object MyInstance {
        val instance = MediaPlayer()
    }

    companion object {
        var currentIndex: Int = Constant.FIRST_MUSIC_INDEX

        @JvmStatic
        fun getInstance(): MediaPlayer {
            return MyInstance.instance
        }
    }
}