package com.training.echomusicapp.fragment

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView

import com.training.echomusicapp.R
import com.training.echomusicapp.model.CurrentSongHelper
import com.training.echomusicapp.model.Songs
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class SongPlayingFragment : Fragment() {
    lateinit var activity: Activity
    lateinit var mediaPlayer: MediaPlayer
    lateinit var txtSongPlayingTitle: TextView
    lateinit var txtSongPlayingArtist: TextView
    lateinit var seekBar: SeekBar
    lateinit var txtStartTime: TextView
    lateinit var txtEndTime: TextView
    lateinit var imgPlayPauseButton: ImageButton
    lateinit var imgNextButton: ImageButton
    lateinit var imgPreviousButton: ImageButton
    lateinit var imgShuffleButton: ImageButton
    lateinit var imgLoopButton: ImageButton

    var currentSong = CurrentSongHelper()
    var currentSongPosition: Int = 0
    var fetchSongs: ArrayList<Songs> = arrayListOf()

    var updateSongTime = object : Runnable {
        override fun run() {
            val getCurrent = mediaPlayer?.currentPosition
            txtStartTime.setText(
                String.format(
                    "%d : %d",

                    TimeUnit.MILLISECONDS.toMinutes(getCurrent.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(getCurrent.toLong()) -
                            TimeUnit.MILLISECONDS.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    getCurrent.toLong()
                                )
                            )
                )
            )

            seekBar.setProgress(getCurrent)
            Handler().postDelayed(this, 1000)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_song_playing, container, false)
        txtSongPlayingTitle = view.findViewById(R.id.txtSongPlayingTitle)
        txtSongPlayingArtist = view.findViewById(R.id.txtSongPlayingArtist)
        seekBar = view.findViewById(R.id.seekBar)
        txtStartTime = view.findViewById(R.id.txtStartTime)
        txtEndTime = view.findViewById(R.id.txtEndTime)
        imgPlayPauseButton = view.findViewById(R.id.imgPlayPauseButton)
        imgNextButton = view.findViewById(R.id.imgNextButton)
        imgPreviousButton = view.findViewById(R.id.imgPreviousButton)
        imgShuffleButton = view.findViewById(R.id.imgShuffleButton)
        imgLoopButton = view.findViewById(R.id.imgLoopButton)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        currentSong?.isPlaying = true
        currentSong?.isLoop = false
        currentSong?.isShuffle = false

        var path: String? = null
        var songTitle: String? = null
        var songArtist: String? = null
        var songId: Int? = null
        try {
            path = arguments?.getString("path")
            songTitle = arguments?.getString("songTitle")
            songArtist = arguments?.getString("songArtist")
            songId = arguments?.getLong("songId")?.toInt() as Int
            currentSongPosition = arguments?.getInt("songPosition") as Int
            fetchSongs = arguments?.getStringArrayList("songData") as ArrayList<Songs>

            currentSong?.currentPosition = currentSongPosition

            currentSong?.songPath = path
            currentSong?.songTitle = songTitle
            currentSong?.songArtist = songArtist
            currentSong?.songId = songId?.toLong()
            updateTextViews(currentSong?.songTitle as String, currentSong?.songArtist as String)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        println("Media Path ${path}")
        try {
            mediaPlayer?.setDataSource(activity, Uri.parse(path))
            mediaPlayer.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer?.start()
        processInformation(mediaPlayer)
        if (currentSong?.isPlaying ) {
            imgPlayPauseButton.setBackgroundResource(R.drawable.ic_play_icon)
        } else {
            imgPlayPauseButton.setBackgroundResource(R.drawable.ic_pause_icon)
        }
        mediaPlayer?.setOnCompletionListener {
            onSongComplete()
        }
        clickHandler()


    }

    fun clickHandler() {
        imgPlayPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                currentSong?.isPlaying = false
                imgPlayPauseButton.setBackgroundResource(R.drawable.ic_play_icon)
            } else {
                mediaPlayer.start()
                currentSong?.isPlaying = true
                imgPlayPauseButton.setBackgroundResource(R.drawable.ic_pause_icon)
            }
        }
        imgNextButton.setOnClickListener {
            currentSong?.isPlaying = true
            if (currentSong?.isShuffle as Boolean) {
                playNext("PlayNextShuffle")
            } else {
                playNext("PlayNextNormal")
            }
        }
        imgPreviousButton.setOnClickListener {
            currentSong?.isPlaying = true
            if (currentSong?.isLoop ) {
                imgLoopButton.setBackgroundResource(R.drawable.ic_loop_icon)
            }
            playPrevious()
        }
        imgLoopButton.setOnClickListener {
            if (currentSong?.isLoop) {
                currentSong?.isLoop = false
                imgLoopButton.setBackgroundResource(R.drawable.ic_loop_yellow_icon)
            } else {
                currentSong?.isLoop = true
                currentSong?.isShuffle = false
                imgLoopButton.setBackgroundResource(R.drawable.ic_loop_icon)
            }
        }
        imgShuffleButton.setOnClickListener {
            if (currentSong?.isShuffle) {
                currentSong?.isShuffle = false
                imgShuffleButton.setBackgroundResource(R.drawable.ic_shuffle_yellow_icon)
            } else {
                currentSong?.isLoop = false
                currentSong?.isShuffle = true
                imgShuffleButton.setBackgroundResource(R.drawable.ic_shuffle_icon)
            }

        }
    }

    fun playNext(check: String) {
        if (check.equals("PlayNextNormal", true)) {
            currentSongPosition += 1
        } else if (check.equals("PlayNextShuffle", true)) {
            var randomPosition = java.util.Random().nextInt(fetchSongs?.size?.plus(1) as Int)
            currentSongPosition = randomPosition
        }
        if (currentSongPosition == fetchSongs?.size) {
            currentSongPosition = 0
        }



        var nextSong = fetchSongs?.get(currentSongPosition)
        currentSong?.isLoop = false
        currentSong?.songTitle = nextSong?.songName
        currentSong?.songId = nextSong?.songId
        currentSong?.songArtist = nextSong?.songArtist
        currentSong?.currentPosition = currentSongPosition
        currentSong?.songPath = nextSong?.songData
        updateTextViews(currentSong?.songTitle as String, currentSong?.songArtist as String)
        mediaPlayer?.reset()
        try {
            mediaPlayer.setDataSource(activity, Uri.parse(currentSong?.songPath))
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            processInformation(mediaPlayer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playPrevious() {
        currentSongPosition = currentSongPosition - 1
        if (currentSongPosition == -1) {
            currentSongPosition = 0
        }
        if (currentSong?.isPlaying ) {
            imgPlayPauseButton.setBackgroundResource(R.drawable.ic_pause_icon)
        } else {
            imgPlayPauseButton.setBackgroundResource(R.drawable.ic_play_icon)
        }
        var nextSong = fetchSongs?.get(currentSongPosition)
        currentSong?.isLoop = false
        currentSong?.songTitle = nextSong?.songName
        currentSong?.songId = nextSong?.songId
        currentSong?.songArtist = nextSong?.songArtist
        currentSong?.currentPosition = currentSongPosition
        currentSong?.songPath = nextSong?.songData
        updateTextViews(currentSong?.songTitle as String, currentSong?.songArtist as String)
        mediaPlayer?.reset()
        try {
            mediaPlayer.setDataSource(activity, Uri.parse(currentSong?.songPath))
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            processInformation(mediaPlayer)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun onSongComplete() {
        if (currentSong?.isShuffle ) {
            playNext("PlayNextShuffle")
            currentSong?.isPlaying = true
        } else {
            if (currentSong?.isLoop as Boolean) {
                var nextSong = fetchSongs?.get(currentSongPosition)
                currentSong?.isLoop = false
                currentSong?.songTitle = nextSong?.songName
                currentSong?.songId = nextSong?.songId
                currentSong?.songArtist = nextSong?.songArtist
                currentSong?.currentPosition = currentSongPosition
                currentSong?.songPath = nextSong?.songData
                updateTextViews(currentSong?.songTitle as String, currentSong?.songArtist as String)
                mediaPlayer?.reset()
                try {
                    mediaPlayer.setDataSource(activity, Uri.parse(currentSong?.songPath))
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                    processInformation(mediaPlayer)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                playNext("PlayNextNormal")
                currentSong?.isPlaying = true
            }
        }
    }

    fun updateTextViews(songTitle: String, songArtist: String) {
        txtSongPlayingTitle.setText(songTitle)
        txtSongPlayingArtist.setText(songArtist)
    }

    fun processInformation(mediaPlayer: MediaPlayer) {
        val finalTime = mediaPlayer.duration
        val startTime = mediaPlayer.currentPosition
        seekBar.max = finalTime
        txtStartTime.setText(
            String.format(
                "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())
                )
            )
        )

        txtEndTime.setText(
            String.format(
                "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong())
                )
            )
        )

        seekBar.setProgress(startTime)
        Handler().postDelayed(updateSongTime, 1000)
    }
}
