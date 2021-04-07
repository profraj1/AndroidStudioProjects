package com.training.foodrunner.adapter

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.Gson
import com.training.foodrunner.R
import com.training.foodrunner.activity.RestaurantsDetailsActivity
import com.training.foodrunner.database.CartDatabase
import com.training.foodrunner.database.CartEntity
import com.training.foodrunner.model.RestaurantItemList

class RestaurantDetailsAdapterClass(val context: Context, private val resItemDetailsList:ArrayList<RestaurantItemList>):
RecyclerView.Adapter<RestaurantDetailsAdapterClass.RestaurantDetailsViewHolder>(){

    class RestaurantDetailsViewHolder(view: View): RecyclerView.ViewHolder(view){
        var llResDetailsContent: LinearLayout = view.findViewById(R.id.llResDetailsContent)
        var resDetailSerialNo: TextView = view.findViewById(R.id.resDetailSerialNo)
        var resDetailItemName: TextView = view.findViewById(R.id.resDetailItemName)
        var resDetailItemPrice: TextView = view.findViewById(R.id.resDetailItemPrice)
        var btnAddRemoveCart: Button = view.findViewById(R.id.btnAddRemoveCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_restaurant_detail, parent, false)
        return RestaurantDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resItemDetailsList.size
    }

    override fun onBindViewHolder(holder: RestaurantDetailsViewHolder, position: Int) {
        val resItemDetail = resItemDetailsList[position]
        holder.resDetailSerialNo.text = (position + 1).toString()
        holder.resDetailItemName.text = resItemDetail.name
        holder.resDetailItemPrice.text = resItemDetail.cost_for_one
        val cartEntity = CartEntity(
            resItemDetail.id.toInt(),
            resItemDetail.name,
            resItemDetail.cost_for_one,
            resItemDetail.restaurant_id
        )

        holder.btnAddRemoveCart.setOnClickListener {
            if(!DBAsyncTask(context, cartEntity,  1).execute().get()){
                val result = DBAsyncTask(context, cartEntity, 2).execute().get()
                if(result){

                    holder.btnAddRemoveCart.text = "REMOVE"
                    val removeColor = ContextCompat.getColor(context, R.color.colorAccent)
                    holder.btnAddRemoveCart.setBackgroundColor(removeColor)
                }else{
                    Toast.makeText(context, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val result = DBAsyncTask(context, cartEntity, 3).execute().get()
                if(result){
                    holder.btnAddRemoveCart.text = "ADD"
                    val removeColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    holder.btnAddRemoveCart.setBackgroundColor(removeColor)
                }else{
                    Toast.makeText(context, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    class DBAsyncTask(context: Context, val cartEntity: CartEntity, val mode: Int):AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, CartDatabase::class.java, "cart-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            var flag: Boolean = false
            when(mode) {
                1 -> {
                    // Check DB if the Item is in cart or not
                    val item: CartEntity? = db.cartDao().getItemsById(cartEntity.item_id)
                    db.close()
                    flag =  item != null
                }
                2 -> {
                    // Add the Item in the cart
                    db.cartDao().insertItemsInCart(cartEntity)
                    db.close()
                    flag =  true
                }
                3 -> {
                    // Remove the Item from the Cart
                    db.cartDao().deleteItemsFromCart(cartEntity)
                    db.close()
                    flag =  true
                }
            }
            return flag
        }
    }
}