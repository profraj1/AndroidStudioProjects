package com.training.foodrunner.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.training.foodrunner.R
import com.training.foodrunner.adapter.HomeAdapterClass
import com.training.foodrunner.model.Restaurants
import com.training.foodrunner.util.ConnectionManager
import org.json.JSONException
import java.lang.RuntimeException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    lateinit var homeRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var homeAdapter: HomeAdapterClass
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var resInfoList: ArrayList<Restaurants> = arrayListOf()

    var ratingComparator = Comparator<Restaurants> { res1, res2 ->
        res1.resRating.compareTo(res2.resRating, true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)

        homeRecyclerView = view.findViewById(R.id.homeRecyclerView)
        layoutManager = LinearLayoutManager(activity)
        progressBarLayout = view.findViewById(R.id.progressBarLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressBarLayout.visibility = View.VISIBLE

        //homeAdapter = HomeAdapterClass(activity as Context, resInfoList)
        //homeRecyclerView.adapter = homeAdapter
        //homeRecyclerView.layoutManager = layoutManager

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

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
                                val resObject = Restaurants(
                                    resJsonObject.getString("id"),
                                    resJsonObject.getString("name"),
                                    resJsonObject.getString("rating"),
                                    resJsonObject.getString("cost_for_one"),
                                    resJsonObject.getString("image_url")
                                )
                                resInfoList.add(resObject)
                                if(activity != null){
                                    homeAdapter = HomeAdapterClass(activity as Context, resInfoList)
                                    homeRecyclerView.adapter = homeAdapter
                                    homeRecyclerView.layoutManager = layoutManager
                                }
                            }

                        } else {
                            Toast.makeText(
                                activity as Context,
                                "No response from the server. Try again later",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Something went wrong!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        activity as Context,
                        "No response from the server. Please try again later.",
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
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Setting") { text, Listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, Listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }



        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_home, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if(id ==  R.id.menuActionSort){
            Collections.sort(resInfoList, ratingComparator)
            resInfoList.reverse()
        }
        homeAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

}
