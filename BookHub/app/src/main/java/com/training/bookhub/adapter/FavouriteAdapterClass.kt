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
import com.training.bookhub.database.BookEntity

class FavouriteAdapterClass(val context: Context, val bookList: List<BookEntity>): RecyclerView.Adapter<FavouriteAdapterClass.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view){
        var llFavContent: LinearLayout = view.findViewById(R.id.llFavContent)
        var imgFavBookImage: ImageView = view.findViewById(R.id.imgFavBookImage)
        var txtFavBookTitle: TextView = view.findViewById(R.id.txtFavBookTitle)
        var txtFavBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        var txtFavBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        var txtFavBookRating: TextView = view.findViewById(R.id.txtFavBookRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        var book = bookList[position]
        holder.txtFavBookTitle.text = book.bookName
        holder.txtFavBookAuthor.text = book.bookAuthor
        holder.txtFavBookPrice.text = book.bookPrice
        holder.txtFavBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.bookhub_icon).into(holder.imgFavBookImage)

        holder.llFavContent.setOnClickListener {
            val intent = Intent(context, BookDetailsActivity::class.java)
            intent.putExtra("book_id", book.book_id.toString())
            context.startActivity(intent)
        }

    }
}