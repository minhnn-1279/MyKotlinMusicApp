package com.nguyennhatminh614.musicapp.presenter

import android.media.MediaPlayer
import com.nguyennhatminh614.musicapp.model.Song
import com.nguyennhatminh614.musicapp.presenter.contract.MusicContract
import com.nguyennhatminh614.musicapp.utils.MyMusicPlayer

class MusicPresenter(
    private val mMusicView: MusicContract.View,
    private val listSong: ArrayList<Song>
) : MusicContract.Presenter {
    val mediaPlayer: MediaPlayer = MyMusicPlayer.getInstance()

    override fun onLoadSong(song: Song) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(song.linkSong)
        mediaPlayer.prepare()
        mediaPlayer.start()
        mMusicView.onLoadSongUpdateView(song)
        mMusicView.onRunningSongUpdateView(song)
    }

    override fun onClickPreviousSong() {
        if (MyMusicPlayer.currentIndex == 0) {
            return
        }
        MyMusicPlayer.currentIndex--
        mediaPlayer.reset()
        onLoadSong(listSong[MyMusicPlayer.currentIndex])
    }

    override fun onClickNextSong() {
        if (MyMusicPlayer.currentIndex == listSong.size - 1) {
            return
        }
        MyMusicPlayer.currentIndex++
        mediaPlayer.reset()
        onLoadSong(listSong[MyMusicPlayer.currentIndex])
    }

    override fun onClickPlayPauseSong(onChangeIcon: (Boolean) -> Unit) {
        onChangeIcon(mediaPlayer.isPlaying)

        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }
}