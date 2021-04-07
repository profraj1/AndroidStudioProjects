package com.training.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.view.textclassifier.TextLinks
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.training.bookhub.R
import com.training.bookhub.adapter.DashboardAdapterClass
import com.training.bookhub.model.Book
import com.training.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {

    lateinit var dashboardRecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var dashboardAdapter: DashboardAdapterClass
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var bookInfoList: ArrayList<Book> = arrayListOf()

    var ratingComparator = Comparator<Book>{book1, book2 ->
        book1.bookRating.compareTo(book2.bookRating, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)

        dashboardRecyclerView = view.findViewById(R.id.dashboardRecyclerView)
        layoutManager = LinearLayoutManager(activity)
        progressBarLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressBarLayout.visibility = View.VISIBLE

        if(ConnectionManager().checkConnectivity(activity as Context)){
            //Creating Queue for request to the server
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v1/book/fetch_books/"

            val jasonObjectRequest = object: JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    //Code for handling Responses
                    try{
                        progressBarLayout.visibility = View.GONE
                        val success = it.getBoolean("success")
                        if(success){
                            val data = it.getJSONArray("data")
                            for(i in 0 until data.length()){
                                val bookJasonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJasonObject.getString("book_id"),
                                    bookJasonObject.getString("name"),
                                    bookJasonObject.getString("author"),
                                    bookJasonObject.getString("price"),
                                    bookJasonObject.getString("rating"),
                                    bookJasonObject.getString("image"))
                                bookInfoList.add(bookObject)

                                dashboardAdapter = DashboardAdapterClass(activity as Context, bookInfoList)
                                dashboardRecyclerView.adapter = dashboardAdapter
                                dashboardRecyclerView.layoutManager = layoutManager
                            }

                        }
                        else{
                            if(activity != null){
                                Toast.makeText(activity as Context, "Some unexpected error occurred!!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    catch (e: JSONException){
                        if(activity != null){
                            Toast.makeText(activity as Context, "Some unexpected error occurred!!", Toast.LENGTH_SHORT).show()
                        }

                    }

                }, Response.ErrorListener {
                    // Code for Handling Errors
                    if(activity != null){
                        Toast.makeText(activity as Context, "Some unexpected error occurred!!", Toast.LENGTH_SHORT).show()
                    }


                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val header = HashMap<String, String>()
                    header["Content-type"] = "application/json"
                    header["token"] = "cad0f404a650a8"
                    return header
                }
            }

            queue.add(jasonObjectRequest)

        }
        else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Setting"){text, Listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, Listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }

            dialog.create()
            dialog.show()
        }



        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if(id == R.id.menuActionSort){
            Collections.sort(bookInfoList, ratingComparator)
            bookInfoList.reverse()
        }
        dashboardAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

}
