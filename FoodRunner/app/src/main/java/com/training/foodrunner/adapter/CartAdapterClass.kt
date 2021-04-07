package com.training.foodrunner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.training.foodrunner.R
import com.training.foodrunner.database.CartEntity

class CartAdapterClass(private val context: Context, val cartItemList: List<CartEntity>): RecyclerView.Adapter<CartAdapterClass.CartViewHolder>() {

    class CartViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val txtCartItemName:TextView = view.findViewById(R.id.txtCartItemName)
        val txtCartItemPrice:TextView = view.findViewById(R.id.txtCartItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_recycler_cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
       return cartItemList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val itemList = cartItemList[position]
        holder.txtCartItemName.text = itemList.itemName
        holder.txtCartItemPrice.text = "Rs ${itemList.costForOne}"
    }
}