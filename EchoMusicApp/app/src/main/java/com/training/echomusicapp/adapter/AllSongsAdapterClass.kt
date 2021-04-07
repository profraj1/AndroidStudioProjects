package com.training.echomusicapp.adapter

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.training.echomusicapp.R
import com.training.echomusicapp.activity.MainActivity
import com.training.echomusicapp.fragment.SongPlayingFragment
import com.training.echomusicapp.model.Songs

class AllSongsAdapterClass(val context: Context, val songList:ArrayList<Songs>):
RecyclerView.Adapter<AllSongsAdapterClass.AllSongsViewHolder>(){

    class AllSongsViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val txtSongTitle:TextView = view.findViewById(R.id.txtSongTitle)
        val txtSongArtist:TextView = view.findViewById(R.id.txtSongArtist)
        val allSongsContentHolder:RelativeLayout = view.findViewById(R.id.allSongsContentHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllSongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_all_songs, parent, false)
        return AllSongsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun onBindViewHolder(holder: AllSongsViewHolder, position: Int) {
        var item = songList[position]
        holder.txtSongTitle.text = item.songName
        holder.txtSongArtist.text = item.songArtist
        holder.allSongsContentHolder.setOnClickListener {
            val songPlayingFragment = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist", item.songArtist)
            args.putString("path", item.songData)
            args.putString("songTitle", item.songName)
            args.putLong("songId", item.songId)
            args.putInt("songPosition", position)
            args.putStringArrayList("songData", songList as java.util.ArrayList<String>)
            songPlayingFragment.arguments = args
            (context as MainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, songPlayingFragment)
                .commit()
        }
    }
}