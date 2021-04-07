package com.training.foodrunner.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.foodrunner.R
import com.training.foodrunner.adapter.HomeAdapterClass
import com.training.foodrunner.adapter.RestaurantDetailsAdapterClass
import com.training.foodrunner.database.CartDatabase
import com.training.foodrunner.database.CartEntity
import com.training.foodrunner.model.RestaurantItemList
import com.training.foodrunner.model.Restaurants
import com.training.foodrunner.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.single_row_restaurant_detail.*
import org.json.JSONException

class RestaurantsDetailsActivity : AppCompatActivity() {
    lateinit var resDetailsToolbar: androidx.appcompat.widget.Toolbar
    var resId: String = "100"
    lateinit var resName: String
    lateinit var resDetailsRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var resDetailsAdapter: RestaurantDetailsAdapterClass
    lateinit var btnProceedToCart: Button
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    var resItemList: ArrayList<RestaurantItemList> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants_details)
        resDetailsToolbar = findViewById(R.id.ResDetailsToolbar)
        resDetailsRecyclerView = findViewById(R.id.resDetailsRecyclerView)
        btnProceedToCart = findViewById(R.id.btnProceedToCart)
        layoutManager = LinearLayoutManager(this@RestaurantsDetailsActivity)

        progressBarLayout = findViewById(R.id.progressBarLayout)
        progressBar = findViewById(R.id.progressBar)

        progressBarLayout.visibility = View.VISIBLE

        if (intent != null) {
            resId = intent.getStringExtra("id")
            resName = intent.getStringExtra("name")
        } else {
            finish()
            Toast.makeText(
                this@RestaurantsDetailsActivity,
                "Something went Wrong!!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (resId == "100") {
            finish()
            Toast.makeText(
                this@RestaurantsDetailsActivity,
                "Something went Wrong!!",
                Toast.LENGTH_SHORT
            ).show()
        }

        setSupportActionBar(resDetailsToolbar)
        supportActionBar?.title = resName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (CartEmptyAsyncTask(applicationContext).execute().get()) {
            Toast.makeText(this@RestaurantsDetailsActivity, "No Items in Cart", Toast.LENGTH_SHORT)
                .show()
        }

        if (ConnectionManager().checkConnectivity(this@RestaurantsDetailsActivity)) {
            val queue = Volley.newRequestQueue(this@RestaurantsDetailsActivity)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {
                        progressBarLayout.visibility = View.GONE
                        val rootData = it.getJSONObject("data")
                        val success = rootData.getBoolean("success")
                        if (success) {
                            val data = rootData.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val resJsonObject = data.getJSONObject(i)
                                val resItemObject = RestaurantItemList(
                                    resJsonObject.getString("id"),
                                    resJsonObject.getString("name"),
                                    resJsonObject.getString("cost_for_one"),
                                    resJsonObject.getString("restaurant_id")
                                )
                                resItemList.add(resItemObject)

                                resDetailsAdapter = RestaurantDetailsAdapterClass(
                                    this@RestaurantsDetailsActivity,
                                    resItemList
                                )
                                resDetailsRecyclerView.adapter = resDetailsAdapter
                                resDetailsRecyclerView.layoutManager = layoutManager
                            }

                        } else {
                            Toast.makeText(
                                this@RestaurantsDetailsActivity,
                                "Something went wrong!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@RestaurantsDetailsActivity,
                            "Something went wrong!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@RestaurantsDetailsActivity,
                        "No Response from the server",
                        Toast.LENGTH_SHORT
                    ).show()

                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val header = HashMap<String, String>()
                    header["Content-type"] = "application/json"
                    header["token"] = "cad0f404a650a8"
                    return header
                }
            }

            queue.add(jsonObjectRequest)


        } else {
            val dialog = AlertDialog.Builder(this@RestaurantsDetailsActivity)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Setting") { text, Listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, Listner ->
                ActivityCompat.finishAffinity(this@RestaurantsDetailsActivity)
            }
            dialog.create()
            dialog.show()
        }

        btnProceedToCart.setOnClickListener {
            val cartAct = Intent(this@RestaurantsDetailsActivity, MyCartActivity::class.java)
            cartAct.putExtra("resName", resName)
            startActivity(cartAct)
        }

    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if(CartItemAsyncTask(applicationContext).execute().get().isNotEmpty()){
            val dialog = AlertDialog.Builder(this@RestaurantsDetailsActivity)
            dialog.setTitle("Question")
            dialog.setMessage("Items in the cart will be removed. Do you want to continue?")
            dialog.setPositiveButton("Yes") { text, Listner ->
                super.onBackPressed()
            }
            dialog.setNegativeButton("No") { text, Listner ->
                //do nothing
            }
            dialog.create()
            dialog.show()
        }else{
            super.onBackPressed()
        }
    }

    class CartEmptyAsyncTask(val context: Context) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "cart-db").build()
            db.cartDao().removeAllItemsFromCart()
            db.close()
            return true
        }

    }
    class CartItemAsyncTask(val context: Context): AsyncTask<Void, Void, List<CartEntity>>(){
        override fun doInBackground(vararg params: Void?): List<CartEntity> {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "cart-db").build()
            return db.cartDao().getAllItemsFromCart()
        }

    }


}
