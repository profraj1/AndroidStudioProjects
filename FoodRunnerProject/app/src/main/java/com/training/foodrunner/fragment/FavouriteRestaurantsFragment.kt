package com.training.foodrunner.fragment

import com.training.foodrunner.R
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.training.foodrunner.activity.MainActivity
import com.training.foodrunner.adapter.FavouritesAdapterClass
import com.training.foodrunner.database.RestaurantDatabase
import com.training.foodrunner.database.RestaurantEntity


/**
 * A simple [Fragment] subclass.
 */
class FavouriteRestaurantsFragment : Fragment() {

    lateinit var favRecyclerView: RecyclerView
    lateinit var favNoItems: RelativeLayout
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var favAdapterClass: FavouritesAdapterClass
    lateinit var imgAddItems: ImageView

    var favItemList: List<RestaurantEntity> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite_restaurants, container, false)

        favRecyclerView = view.findViewById(R.id.favRecyclerView)
        favNoItems = view.findViewById(R.id.favNoItems)
        imgAddItems = view.findViewById(R.id.imgAddItems)
        favItemList = FavouritesAsyncTask(activity as Context).execute().get()
        if(favItemList.isNotEmpty()){
            favNoItems.visibility = View.GONE
            favAdapterClass = FavouritesAdapterClass(activity as Context, favItemList)
            layoutManager = LinearLayoutManager(activity as Context)
            favRecyclerView.adapter = favAdapterClass
            favRecyclerView.layoutManager = layoutManager
        }
        else{
            favNoItems.visibility = View.VISIBLE
            imgAddItems.setOnClickListener {
                val mainAct = Intent(activity, MainActivity::class.java)
                startActivity(mainAct)
            }
        }

        return view
    }

    class FavouritesAsyncTask(val context: Context) :
        AsyncTask<Void, Void, List<RestaurantEntity>>() {
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db")
                .build()
            return db.restaurantDao().getAllItemsFromRestaurant()
        }

    }

}
