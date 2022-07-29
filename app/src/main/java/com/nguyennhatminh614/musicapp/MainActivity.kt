package com.nguyennhatminh614.musicapp

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyennhatminh614.musicapp.adapter.SongAdapter
import com.nguyennhatminh614.musicapp.databinding.ActivityMainBinding
import com.nguyennhatminh614.musicapp.model.Song
import com.nguyennhatminh614.musicapp.presenter.MusicPresenter
import com.nguyennhatminh614.musicapp.presenter.SongPresenter
import com.nguyennhatminh614.musicapp.presenter.contract.MusicContract
import com.nguyennhatminh614.musicapp.utils.GlobalExtensionFunction.Companion.createImageUriWithThisID
import com.nguyennhatminh614.musicapp.utils.GlobalExtensionFunction.Companion.loadGlideImageWithUri

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
        setContentView(binding.root)

        val songPresenter = SongPresenter()

        songPresenter.requestPermission(this@MainActivity) {
            hasReadExternalMemoryPermission(it)
        }

        if (hasReadExternalMemoryPermission(this@MainActivity)) {
            val songList: ArrayList<Song> = songPresenter.loadMusic(contentResolver)
            with(binding) {
                if (songList.size != 0) {
                    rcSong.adapter = SongAdapter(songList, this@MainActivity) {
                        val position: Int = it
                        val thisSong = songList[position]
                        val musicPresenter = MusicPresenter(object : MusicContract.View {
                            override fun onLoadSongUpdateView(song: Song) {
                                tvAuthor.text = song.author
                                tvTitleSong.text = song.nameSong
                                imgSong.loadGlideImageWithUri(
                                    this@MainActivity,
                                    song.imageID.createImageUriWithThisID()
                                )
                            }

                            override fun onRunningSongUpdateView(song: Song) {

                            }

                        }, songList)

                        currentSongLayout.visibility = View.VISIBLE
                        tvAuthor.text = thisSong.author
                        tvTitleSong.text = thisSong.nameSong
                        imgSong.loadGlideImageWithUri(
                            this@MainActivity,
                            thisSong.imageID.createImageUriWithThisID()
                        )

                        btnPlayPauseSong.setOnClickListener {
                            musicPresenter.onClickPlayPauseSong {
                                val isPlaying: Boolean = it

                                if (isPlaying) {
                                    binding.btnPlayPauseSong.setImageResource(R.drawable.ic_baseline_play_circle_outline_64)
                                } else {
                                    binding.btnPlayPauseSong.setImageResource(R.drawable.ic_baseline_pause_circle_outline_64)
                                }
                            }
                        }

                        musicPresenter.onLoadSong(songList[position])

                        btnPreviousSong.setOnClickListener {
                            musicPresenter.onClickPreviousSong()
                        }

                        btnNextSong.setOnClickListener {
                            musicPresenter.onClickNextSong()
                        }

                        songPresenter.navigateToMusicActivity(position, songList, this@MainActivity)
                    }

                    rcSong.layoutManager = LinearLayoutManager(this@MainActivity)

                } else {
                    tvNoSong.isVisible = true
                }
            }
        }
    }

    private fun hasReadExternalMemoryPermission(context: Context): Boolean {
        val result: Int = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }
}
