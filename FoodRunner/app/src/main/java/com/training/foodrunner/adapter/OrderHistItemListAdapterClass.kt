package com.training.foodrunner.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.training.foodrunner.R
import com.training.foodrunner.database.CartEntity
import com.training.foodrunner.model.RestaurantItemList

class OrderHistItemListAdapterClass(val context: Context, val itemList: Array<CartEntity>):RecyclerView.Adapter<OrderHistItemListAdapterClass.OrderHistItemListViewHolder>() {

    class OrderHistItemListViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val txtCartItemName: TextView = view.findViewById(R.id.txtCartItemName)
        val txtCartItemPrice: TextView = view.findViewById(R.id.txtCartItemPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistItemListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_recycler_cart_item, parent, false)
        return OrderHistItemListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    override fun onBindViewHolder(holder: OrderHistItemListViewHolder, position: Int) {
        val item = itemList[position]
        holder.txtCartItemName.text = item.itemName
        holder.txtCartItemPrice.text = "Rs: ${item.costForOne}"
        holder.txtCartItemPrice.setTextColor(Color.parseColor("#000000"))
        holder.txtCartItemName.setTextColor(Color.parseColor("#000000"))
    }
}