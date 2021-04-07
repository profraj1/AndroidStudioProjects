package com.training.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.training.bookhub.R
import com.training.bookhub.adapter.FavouriteAdapterClass
import com.training.bookhub.database.BookDatabase
import com.training.bookhub.database.BookEntity

/**
 * A simple [Fragment] subclass.
 */
class FavouritesFragment : Fragment() {
    lateinit var favRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var favAdapter: FavouriteAdapterClass
    var dbBookList = listOf<BookEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)


        favRecyclerView = view.findViewById(R.id.favRecyclerView)
        progressBarLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressBarLayout.visibility = View.VISIBLE

        layoutManager = GridLayoutManager(activity as Context, 2)
        dbBookList = FavouritesAsyncTask(activity as Context).execute().get()

        if(activity != null){
            progressBarLayout.visibility = View.GONE
            favAdapter = FavouriteAdapterClass(activity as Context, dbBookList)
            favRecyclerView.adapter = favAdapter
            favRecyclerView.layoutManager = layoutManager
        }

        return view
    }

    class FavouritesAsyncTask(val context: Context): AsyncTask<Void, Void, List<BookEntity>>(){
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()
            return db.bookDao().getAllBooks()
        }

    }



}


