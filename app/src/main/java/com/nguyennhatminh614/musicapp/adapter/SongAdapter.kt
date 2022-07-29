package com.nguyennhatminh614.musicapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nguyennhatminh614.musicapp.R
import com.nguyennhatminh614.musicapp.model.Song
import com.nguyennhatminh614.musicapp.utils.GlobalExtensionFunction.Companion.createImageUriWithThisID
import com.nguyennhatminh614.musicapp.utils.GlobalExtensionFunction.Companion.loadGlideImageWithUri

class SongAdapter(
    private val listSong: ArrayList<Song>,
    private val context: Context,
    private val clickInterface: (Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {


    class ViewHolder(private var view: View, private var clickInterface: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        var imgSong: ImageView = itemView.findViewById(R.id.imgSong)
        var tvTitleSong: TextView = itemView.findViewById(R.id.tvTitleSong)
        var tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)

        init {
            itemView.setOnClickListener {
                clickInterface(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.song_item_layout, parent, false)
        return ViewHolder(view, clickInterface)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song: Song = listSong[position]
        holder.tvTitleSong.text = song.nameSong
        holder.tvAuthor.text = song.author
        holder.imgSong.loadGlideImageWithUri(context, song.imageID.createImageUriWithThisID())
    }

    override fun getItemCount(): Int = listSong.size
}