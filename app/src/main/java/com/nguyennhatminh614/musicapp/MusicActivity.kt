package com.nguyennhatminh614.musicapp

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.nguyennhatminh614.musicapp.databinding.ActivityMusicBinding
import com.nguyennhatminh614.musicapp.model.Song
import com.nguyennhatminh614.musicapp.presenter.MusicPresenter
import com.nguyennhatminh614.musicapp.presenter.contract.MusicContract
import com.nguyennhatminh614.musicapp.utils.Constant
import com.nguyennhatminh614.musicapp.utils.GlobalExtensionFunction.Companion.createImageUriWithThisID
import com.nguyennhatminh614.musicapp.utils.GlobalExtensionFunction.Companion.loadGlideImageWithUri
import com.nguyennhatminh614.musicapp.utils.GlobalExtensionFunction.Companion.toDateTimeMMSS
import com.nguyennhatminh614.musicapp.utils.GlobalExtensionFunction.Companion.updateAngle
import com.nguyennhatminh614.musicapp.utils.MyMusicPlayer
import java.util.*
import kotlin.concurrent.schedule

class MusicActivity : AppCompatActivity() {
    companion object {
        private const val ROTATE_ANGLE = 0.1f
        var currentAngle: Float = 0f
        var currentPosition: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMusicBinding by lazy { ActivityMusicBinding.inflate(layoutInflater) }
        setContentView(binding.root)
        val songList: ArrayList<Song> =
            intent.getParcelableArrayListExtra(Constant.KEY_SONG_OBJECT)!!

        val musicPresenter by lazy {
            MusicPresenter(
                object : MusicContract.View {
                    override fun onLoadSongUpdateView(song: Song) {
                        with(binding) {
                            tvTitleSong.text = song.nameSong
                            imgSong.loadGlideImageWithUri(
                                this@MusicActivity,
                                song.imageID.createImageUriWithThisID(),
                                isCircleCrop = true
                            )
                            tvCurrentTime.text = Constant.FIRST_LOAD_MUSIC
                            tvTotalTime.text = song.duration.toDateTimeMMSS()
                            sbProgressTime.progress = Constant.FIRST_TIME_SEEK_BAR
                            sbProgressTime.max = MyMusicPlayer.getInstance().duration
                            sbProgressTime.setOnSeekBarChangeListener(object :
                                SeekBar.OnSeekBarChangeListener {
                                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                                    if (MyMusicPlayer.getInstance() != null && p2) {
                                        MyMusicPlayer.getInstance().seekTo(p1)
                                    }
                                }

                                override fun onStartTrackingTouch(p0: SeekBar?) {

                                }

                                override fun onStopTrackingTouch(p0: SeekBar?) {

                                }

                            })
                        }
                    }

                    override fun onRunningSongUpdateView(song: Song) {
                        Timer().schedule(0, 10) {
                            this@MusicActivity.runOnUiThread {
                                with(binding) {
                                    if (currentPosition != MyMusicPlayer.getInstance().currentPosition) {
                                        sbProgressTime.progress =
                                            MyMusicPlayer.getInstance().currentPosition
                                        tvCurrentTime.text =
                                            MyMusicPlayer.getInstance().currentPosition.toLong()
                                                .toDateTimeMMSS()
                                        imgSong.rotation = currentAngle
                                        currentAngle = currentAngle.updateAngle(ROTATE_ANGLE)
                                        currentPosition =
                                            MyMusicPlayer.getInstance().currentPosition
                                    }
                                }
                            }
                        }
                    }
                }, songList
            )
        }

        //First Init
        musicPresenter.onLoadSong(songList[MyMusicPlayer.currentIndex])


        with(binding) {
            btnPreviousSong.setOnClickListener {
                musicPresenter.onClickPreviousSong()
            }

            btnNextSong.setOnClickListener {
                musicPresenter.onClickNextSong()
            }

            btnPlayPauseSong.setOnClickListener {
                musicPresenter.onClickPlayPauseSong {
                    val isPlaying: Boolean = it

                    if (isPlaying) {
                        btnPlayPauseSong.setImageResource(R.drawable.ic_baseline_play_circle_outline_64)
                    } else {
                        btnPlayPauseSong.setImageResource(R.drawable.ic_baseline_pause_circle_outline_64)
                    }
                }
            }
        }
    }
}