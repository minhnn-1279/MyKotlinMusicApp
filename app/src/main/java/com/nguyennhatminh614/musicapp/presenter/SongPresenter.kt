package com.nguyennhatminh614.musicapp.presenter

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import com.nguyennhatminh614.musicapp.MusicActivity
import com.nguyennhatminh614.musicapp.model.Song
import com.nguyennhatminh614.musicapp.presenter.contract.SongContract
import com.nguyennhatminh614.musicapp.utils.Constant
import com.nguyennhatminh614.musicapp.utils.MyMusicPlayer

class SongPresenter : SongContract.Presenter {
    override fun requestPermission(
        context: Context,
        checkPermissionFunction: (Context) -> Boolean
    ) {
        if (!checkPermissionFunction(context)) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.REQUEST_CODE_READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun loadMusic(contentResolver: ContentResolver): ArrayList<Song> {
        val selection: String = MediaStore.Audio.Media.IS_MUSIC + " != 0 "
        val cursor: Cursor by lazy {
            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                null,
                null,
                null
            )!!
        }

        val songList: ArrayList<Song> = ArrayList()
        while (cursor.moveToNext()) {
            with(cursor) {
                val linkSong: String =
                    if (getColumnIndex(MediaStore.Audio.Media.DATA) < 0) "" else getString(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.DATA
                        )
                    )
                val imageID: Long =
                    if (getColumnIndex(MediaStore.Audio.Media.ALBUM_ID) < 0) 0 else getLong(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.ALBUM_ID
                        )
                    )
                val nameSong: String =
                    if (getColumnIndex(MediaStore.Audio.Media.TITLE) < 0) "" else getString(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.TITLE
                        )
                    )
                val author: String =
                    if (getColumnIndex(MediaStore.Audio.Media.ARTIST) < 0) "" else getString(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.ARTIST
                        )
                    )
                val duration: Long =
                    if (getColumnIndex(MediaStore.Audio.Media.DURATION) < 0) 0 else getLong(
                        getColumnIndexOrThrow(
                            MediaStore.Audio.Media.DURATION
                        )
                    )

                songList.add(Song(linkSong, imageID, nameSong, author, duration))
            }
        }

        return songList
    }

    override fun navigateToMusicActivity(
        position: Int,
        songList: ArrayList<Song>,
        context: Context
    ) {
        val intent = Intent(context, MusicActivity::class.java)
        MyMusicPlayer.currentIndex = position
        intent.putExtra(Constant.KEY_SONG_OBJECT, songList)
        context.startActivity(intent)
    }
}