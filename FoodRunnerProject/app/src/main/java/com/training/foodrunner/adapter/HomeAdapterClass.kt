package com.training.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.training.foodrunner.R
import com.training.foodrunner.activity.RestaurantsDetailsActivity
import com.training.foodrunner.database.RestaurantDatabase
import com.training.foodrunner.database.RestaurantEntity
import com.training.foodrunner.model.Restaurants
import kotlinx.android.synthetic.main.recycler_home_single_row.*

class HomeAdapterClass(private val context: Context, private val resList: ArrayList<Restaurants>):
    RecyclerView.Adapter<HomeAdapterClass.HomeViewHolder>(){

    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){
        var llHomeContent: LinearLayout = view.findViewById(R.id.llHomeContent)
        var imgResImage: ImageView = view.findViewById(R.id.imgResImage)
        var txtResName: TextView = view.findViewById(R.id.txtResName)
        var txtResCost: TextView = view.findViewById(R.id.txtResCost)
        var txtResRating: TextView = view.findViewById(R.id.txtResRating)
        var imgFavouriteIcon: ImageView = view.findViewById(R.id.imgFavouriteIcon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        var count = 0
        var res = resList[position]
        holder.txtResName.text = res.resName
        holder.txtResCost.text = "Rs. " + res.resCost +"/person"
        holder.txtResRating.text = res.resRating
        //holder.imgResImage.setImageResource(res.resImage)
        Picasso.get().load(res.resImage).error(R.drawable.food_runner_icon).into(holder.imgResImage)

        holder.llHomeContent.setOnClickListener {
            val resDetailsAct = Intent(context, RestaurantsDetailsActivity::class.java)
            resDetailsAct.putExtra("id", res.resId)
            resDetailsAct.putExtra("name", res.resName)
            context.startActivity(resDetailsAct)
        }
        val restaurantEntity = RestaurantEntity(
            res.resId.toInt(),
            res.resName,
            res.resRating,
            res.resCost,
            res.resImage
        )
        btnAddToFavFunctionality(restaurantEntity, holder)
    }

    private fun btnAddToFavFunctionality(restaurantEntity: RestaurantEntity, holder: HomeViewHolder){
        val checkFav = DBAsyncTask(context, restaurantEntity, 1).execute()
        val isFav = checkFav.get()
        if(isFav){
            holder.imgFavouriteIcon.setImageResource(R.drawable.ic_favorite_red_24dp)
        }
        else{
            holder.imgFavouriteIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }

        holder.imgFavouriteIcon.setOnClickListener {
            if(!DBAsyncTask(context, restaurantEntity, 1).execute().get()){
                val result = DBAsyncTask(context, restaurantEntity, 2).execute().get()
                if(result){
                    holder.imgFavouriteIcon.setImageResource(R.drawable.ic_favorite_red_24dp)
                }else{
                    Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val result = DBAsyncTask(context, restaurantEntity, 3).execute().get()
                if(result){
                    holder.imgFavouriteIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                }else{
                    Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    class DBAsyncTask(context: Context, val restaurantEntity: RestaurantEntity, val mode: Int):
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            var flag: Boolean = false
            when(mode) {
                1 -> {
                    // Check DB if the book is in favourite or not
                    val res: RestaurantEntity? = db.restaurantDao().getResItemsById(restaurantEntity.res_id)
                    db.close()
                    flag =  res != null
                }
                2 -> {
                    // Add the book in the favourite
                    db.restaurantDao().insertItemsInRestaurant(restaurantEntity)
                    db.close()
                    flag =  true
                }
                3 -> {
                    // Remove the book from the favourite
                    db.restaurantDao().deleteItemsFromRestaurant(restaurantEntity)
                    db.close()
                    flag =  true
                }
            }
            return flag
        }
    }
}