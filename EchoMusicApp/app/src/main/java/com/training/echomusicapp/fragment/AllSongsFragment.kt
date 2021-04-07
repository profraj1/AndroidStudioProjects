package com.training.echomusicapp.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.training.echomusicapp.R
import com.training.echomusicapp.activity.MainActivity
import com.training.echomusicapp.adapter.AllSongsAdapterClass
import com.training.echomusicapp.model.Songs

/**
 * A simple [Fragment] subclass.
 */
class AllSongsFragment : Fragment() {
    lateinit var allSongsContent: RelativeLayout
    lateinit var allSongsRecyclerView: RecyclerView
    lateinit var hiddenBarMainScreen:RelativeLayout
    lateinit var imgPauseButton:ImageView
    lateinit var imgPlayingIcon:ImageView
    lateinit var txtPlayingSongs:TextView
    lateinit var myActivity: Activity
    lateinit var allSongsAdapter: AllSongsAdapterClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_songs, container, false)

        allSongsContent = view.findViewById(R.id.allSongsContent)
        allSongsRecyclerView = view.findViewById(R.id.allSongsRecyclerView)
        hiddenBarMainScreen = view.findViewById(R.id.hiddenBarMainScreen)
        imgPauseButton = view.findViewById(R.id.imgPauseButton)
        imgPlayingIcon = view.findViewById(R.id.imgPlayingIcon)
        txtPlayingSongs = view.findViewById(R.id.txtPlayingSongs)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        allSongsRecyclerView.layoutManager = LinearLayoutManager(activity as Context)
        allSongsAdapter = AllSongsAdapterClass(activity as Context, getAllSongs())
        allSongsRecyclerView.adapter = allSongsAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        myActivity = activity
    }
    fun getAllSongs():ArrayList<Songs>{
        var songsList:ArrayList<Songs> = arrayListOf()
        var contentResolver = myActivity.contentResolver
        var songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songCursor = contentResolver.query(songUri, null, null, null,null)
        if(songCursor != null && songCursor.moveToFirst()){
            val songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dateAdded = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while(songCursor.moveToNext()){
                var currentId = songCursor.getLong(songId)
                var currentTitle = songCursor.getString(songTitle)
                var currentArtist = songCursor.getString(songArtist)
                var currentData = songCursor.getString(songData)
                var currentDate = songCursor.getLong(dateAdded)
                songsList.add(Songs(currentId, currentTitle, currentArtist, currentData, currentDate))
            }
        }
        return songsList
    }

}
