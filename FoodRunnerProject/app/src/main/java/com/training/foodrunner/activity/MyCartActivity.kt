package com.training.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.Gson
import com.training.foodrunner.R
import com.training.foodrunner.adapter.CartAdapterClass
import com.training.foodrunner.database.CartDatabase
import com.training.foodrunner.database.CartEntity
import com.training.foodrunner.database.OrderHistoryDatabase
import com.training.foodrunner.database.OrderHistoryEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyCartActivity : AppCompatActivity() {
    lateinit var cartToolbar: Toolbar
    lateinit var txtCartResName: TextView
    lateinit var btnPlaceOrder: Button
    lateinit var cartItemRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var cartAdapterClass: CartAdapterClass
    lateinit var rlNoItemContent: RelativeLayout
    lateinit var resName: String

    var cartItemList: List<CartEntity> = listOf()
    var totalItemCost = 0



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        cartToolbar = findViewById(R.id.cartToolbar)
        txtCartResName = findViewById(R.id.txtCartResName)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        cartItemRecyclerView = findViewById(R.id.cartItemRecyclerView)
        layoutManager = LinearLayoutManager(this@MyCartActivity)
        rlNoItemContent = findViewById(R.id.rlNoItemContent)

        resName = intent.getStringExtra("resName")
        txtCartResName.text = resName

        setSupportActionBar(cartToolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cartItemList = CartAsyncTask(this@MyCartActivity).execute().get()

        if(cartItemList.isNotEmpty()){
            rlNoItemContent.visibility = View.GONE
            for(element in cartItemList){
                totalItemCost += element.costForOne.toInt()
            }
            btnPlaceOrder.text = "PLACE ORDER (TOTAL: Rs ${totalItemCost})"

            btnPlaceOrder.setOnClickListener {
                var countOrderHistory = OrderHistoryCountItems(this@MyCartActivity).execute().get()
                val orderDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
                val gson = Gson()
                val jsonItemList = gson.toJson(cartItemList)
                val orderItemList = OrderHistoryEntity(countOrderHistory + 1, resName, orderDate, jsonItemList)

                val result = OrderHistoryAsyncTask(this@MyCartActivity, orderItemList).execute().get()
                if(result){
                    val orderPlacedAct = Intent(this@MyCartActivity, OrderPlacedActivity::class.java)
                    startActivity(orderPlacedAct)
                    finishAffinity()
                }
                else{
                    Toast.makeText(this@MyCartActivity, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
                }

            }

            cartAdapterClass = CartAdapterClass(this@MyCartActivity, cartItemList)

            cartItemRecyclerView.adapter = cartAdapterClass
            cartItemRecyclerView.layoutManager = layoutManager
        }
        else{
            rlNoItemContent.visibility = View.VISIBLE
            btnPlaceOrder.visibility = View.GONE
        }

    }

    class CartAsyncTask(val context: Context): AsyncTask<Void, Void, List<CartEntity>>(){
        override fun doInBackground(vararg params: Void?): List<CartEntity> {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "cart-db").build()
            return db.cartDao().getAllItemsFromCart()
        }

    }

    class OrderHistoryAsyncTask(val context: Context, val orderItemList: OrderHistoryEntity): AsyncTask<Void, Void, Boolean>(){
        val db = Room.databaseBuilder(context, OrderHistoryDatabase::class.java, "order_history-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.orderHistoryDao().insertItemsInOrderHistory(orderItemList)
            return true
        }
    }

    class OrderHistoryCountItems(val context: Context):AsyncTask<Void, Void, Int>(){
        override fun doInBackground(vararg params: Void?): Int {
            val db = Room.databaseBuilder(context, OrderHistoryDatabase::class.java, "order_history-db").build()
            return db.orderHistoryDao().countOrderHistoryRows()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
