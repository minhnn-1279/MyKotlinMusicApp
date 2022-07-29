package com.nguyennhatminh614.musicapp.presenter.contract

import android.content.ContentResolver
import android.content.Context
import com.nguyennhatminh614.musicapp.model.Song

class SongContract {
    interface Presenter {
        fun requestPermission(context: Context, checkPermissionFunction: (Context) -> Boolean)
        fun loadMusic(contentResolver: ContentResolver): ArrayList<Song>
        fun navigateToMusicActivity(position: Int, songList: ArrayList<Song>, context: Context)
    }
}