package com.training.foodrunner.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.training.foodrunner.R
import com.training.foodrunner.adapter.OrderHistoryAdapterClass
import com.training.foodrunner.database.OrderHistoryDatabase
import com.training.foodrunner.database.OrderHistoryEntity
import com.training.foodrunner.model.OrderHistory
import com.training.foodrunner.model.RestaurantItemList

/**
 * A simple [Fragment] subclass.
 */
class OrderHistoryFragment : Fragment() {
    lateinit var orderHistoryRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var orderHistoryAdapter: OrderHistoryAdapterClass

    var orderItemList: List<OrderHistoryEntity> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)
        orderHistoryRecyclerView = view.findViewById(R.id.OrderHistoryRecyclerView)
        layoutManager = LinearLayoutManager(activity as Context)

        orderItemList = OrderHistoryAsyncTask(activity as Context).execute().get()

        orderHistoryAdapter = OrderHistoryAdapterClass(activity as Context, orderItemList)

        orderHistoryRecyclerView.adapter = orderHistoryAdapter
        orderHistoryRecyclerView.layoutManager = layoutManager


        return view
    }

    class OrderHistoryAsyncTask(val context: Context): AsyncTask<Void, Void, List<OrderHistoryEntity>>(){
        override fun doInBackground(vararg params: Void?): List<OrderHistoryEntity> {
            val db = Room.databaseBuilder(context, OrderHistoryDatabase::class.java, "order_history-db").build()
            return db.orderHistoryDao().getAllItemsFromOrderHistory()
        }
    }

}
