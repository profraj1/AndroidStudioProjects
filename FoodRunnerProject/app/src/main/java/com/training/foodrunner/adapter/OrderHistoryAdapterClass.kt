package com.training.foodrunner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.training.foodrunner.R
import com.training.foodrunner.database.CartEntity
import com.training.foodrunner.database.OrderHistoryEntity
import com.training.foodrunner.model.OrderHistory
import org.json.JSONObject

class OrderHistoryAdapterClass(val context: Context, val itemList: List<OrderHistoryEntity>): RecyclerView.Adapter<OrderHistoryAdapterClass.OrderHistoryViewHolder>() {
    lateinit var layoutManger:RecyclerView.LayoutManager
    lateinit var adapter:OrderHistItemListAdapterClass
    var orderItemList: ArrayList<CartEntity> = arrayListOf()
    class OrderHistoryViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val txtOrderHistoryResName: TextView = view.findViewById(R.id.txtOrderHistoryResName)
        val txtOrderDate: TextView = view.findViewById(R.id.txtOrderDate)
        val orderHisItemList: RecyclerView = view.findViewById(R.id.orderHisItemList)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_history_layout, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val item = itemList[position]
        holder.txtOrderHistoryResName.text = item.resName
        holder.txtOrderDate.text = item.orderDate

        val gson = Gson()
        val carEntity = object : TypeToken<Array<CartEntity>>() {}.type
        var cartList: Array<CartEntity> = gson.fromJson(item.orderItemList, carEntity)
        layoutManger = LinearLayoutManager(context)
        adapter = OrderHistItemListAdapterClass(context, cartList)
        holder.orderHisItemList.adapter = adapter
        holder.orderHisItemList.layoutManager = layoutManger

    }
}