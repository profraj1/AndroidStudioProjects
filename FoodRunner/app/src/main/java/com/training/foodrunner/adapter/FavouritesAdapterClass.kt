package com.training.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.training.foodrunner.R
import com.training.foodrunner.activity.RestaurantsDetailsActivity
import com.training.foodrunner.database.RestaurantEntity

class FavouritesAdapterClass(val context: Context, val favItemList:List<RestaurantEntity>): RecyclerView.Adapter<FavouritesAdapterClass.FavouritesViewHolder>() {

    class FavouritesViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var llFavContent: LinearLayout = view.findViewById(R.id.llFavContent)
        var imgFavResImage: ImageView = view.findViewById(R.id.imgFavResImage)
        var txtFavResName: TextView = view.findViewById(R.id.txtFavResName)
        var txtFavResCost: TextView = view.findViewById(R.id.txtFavResCost)
        var txtFavResRating: TextView = view.findViewById(R.id.txtFavResRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_recycler_fav_item, parent, false)
        return FavouritesViewHolder(view)
    }

    override fun getItemCount(): Int {
      return favItemList.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        var favList = favItemList[position]
        holder.txtFavResName.text = favList.resName
        holder.txtFavResCost.text = "Rs. " + favList.resCost +"/person"
        holder.txtFavResRating.text = favList.resRating
        Picasso.get().load(favList.resImage).error(R.drawable.food_runner_icon).into(holder.imgFavResImage)

        holder.llFavContent.setOnClickListener {
            val resDetailsAct = Intent(context, RestaurantsDetailsActivity::class.java)
            resDetailsAct.putExtra("id", favList.res_id.toString())
            resDetailsAct.putExtra("name", favList.resName)
            context.startActivity(resDetailsAct)
        }
    }
}