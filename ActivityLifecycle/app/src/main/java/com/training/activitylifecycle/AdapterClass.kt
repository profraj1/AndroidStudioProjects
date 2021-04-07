package com.training.activitylifecycle


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AdapterClass(private val context: Context, private val resList: ArrayList<String>):
    RecyclerView.Adapter<AdapterClass.HomeViewHolder>(){

    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){
        var txtResName: TextView = view.findViewById(R.id.txtResName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_recycler_view, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        var res = resList[position]
        holder.txtResName.text = res
    }
}