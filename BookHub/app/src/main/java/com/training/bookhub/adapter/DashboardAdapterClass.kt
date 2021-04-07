package com.training.bookhub.adapter

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
import com.training.bookhub.R
import com.training.bookhub.activity.BookDetailsActivity
import com.training.bookhub.model.Book
import com.training.bookhub.model.Restaurants
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.*

class DashboardAdapterClass(private val context: Context, private val bookDetails: ArrayList<Book> ): RecyclerView.Adapter<DashboardAdapterClass.DashboardViewHolder>() {

    class DashboardViewHolder(view:View): RecyclerView.ViewHolder(view){
        var txtBookTitle:TextView = view.findViewById(R.id.txtBookTitle)
        var txtBookPrice: TextView = view.findViewById(R.id.txtBookPrice)
        var txtBookAuthor: TextView = view.findViewById(R.id.txtBookAuthor)
        var txtBookRating: TextView = view.findViewById(R.id.txtBookRating)
        var imgBookImage: ImageView = view.findViewById(R.id.imgBookImage)

        var dashboardContent: LinearLayout = view.findViewById(R.id.llDashboardContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookDetails.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
       var book = bookDetails[position]
        holder.txtBookTitle.text = book.bookTitle
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        //holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.bookhub_icon).into(holder.imgBookImage)

        holder.dashboardContent.setOnClickListener {
            val intent = Intent(context, BookDetailsActivity::class.java)
            intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)
        }

    }
}