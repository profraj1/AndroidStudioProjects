package com.training.bookhub.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.training.bookhub.R
import com.training.bookhub.database.BookDatabase
import com.training.bookhub.database.BookEntity
import com.training.bookhub.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

class BookDetailsActivity : AppCompatActivity() {

    lateinit var  bookDetailsToolbar:androidx.appcompat.widget.Toolbar
    lateinit var imgBookDetailsImage: ImageView
    lateinit var txtBookDetailsTitle: TextView
    lateinit var txtBookDetailsAuthor: TextView
    lateinit var txtBookDetailsPrice: TextView
    lateinit var txtBookDetailsRating: TextView
    lateinit var btnAddtoFav: Button
    lateinit var progressBarLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var txtBookDescription: TextView
    var bookId:String = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        bookDetailsToolbar = findViewById(R.id.bookDetailsToolbar)
        imgBookDetailsImage = findViewById(R.id.imgBookDetailsImage)
        txtBookDetailsTitle = findViewById(R.id.txtBookDetailsTitle)
        txtBookDetailsAuthor = findViewById(R.id.txtBookDetailsAuthor)
        txtBookDetailsPrice = findViewById(R.id.txtBookDetailsPrice)
        txtBookDetailsRating =  findViewById(R.id.txtBookDetailsRating)
        btnAddtoFav = findViewById(R.id.btnAddToFav)
        txtBookDescription = findViewById(R.id.txtBookDescription)
        progressBarLayout = findViewById(R.id.progressBarLayout)
        progressBarLayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        setSupportActionBar(bookDetailsToolbar)
        supportActionBar?.title = "Book Details"

        if(intent != null){
            bookId = intent.getStringExtra("book_id")
        }else{
            finish()
            Toast.makeText(this@BookDetailsActivity, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
        }
        if(bookId == "100"){
            finish()
            Toast.makeText(this@BookDetailsActivity, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
        }

        if(ConnectionManager().checkConnectivity(this@BookDetailsActivity)){
            val queue = Volley.newRequestQueue(this@BookDetailsActivity)
            val url = "http://13.235.250.119/v1/book/get_book/"
            val jasonParams = JSONObject()
            jasonParams.put("book_id", bookId)

            val jasonRequest = object : JsonObjectRequest(Method.POST, url, jasonParams,
            Response.Listener {
                try{
                    val success = it.getBoolean("success")

                    if(success){
                        progressBarLayout.visibility = View.GONE
                        val data = it.getJSONObject("book_data")
                        val bookImageUrl = data.getString("image")
                        txtBookDetailsTitle.text = data.getString("name")
                        txtBookDetailsAuthor.text = data.getString("author")
                        txtBookDetailsPrice.text = data.getString("price")
                        txtBookDetailsRating.text = data.getString("rating")
                        txtBookDescription.text = data.getString("description")
                        Picasso.get().load(data.getString("image")).error(R.drawable.bookhub_icon).into(imgBookDetailsImage)

                        val bookEntity = BookEntity(
                            bookId?.toInt(),
                            txtBookDetailsTitle.text.toString(),
                            txtBookDetailsAuthor.text.toString(),
                            txtBookDetailsPrice.text.toString(),
                            txtBookDetailsRating.text.toString(),
                            txtBookDescription.text.toString(),
                            bookImageUrl.toString()
                        )
                        btnAddToFavFunctionality(bookEntity)


                    }else{
                        Toast.makeText(this@BookDetailsActivity, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
                    }

                }catch (e:JSONException){
                    Toast.makeText(this@BookDetailsActivity, "Something went Wrong!!", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                    Toast.makeText(this@BookDetailsActivity, "Some unexpected error occurred!!", Toast.LENGTH_SHORT).show()
                })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val header = HashMap<String, String>()
                    header["Content-type"] = "application/json"
                    header["token"] = "cad0f404a650a8"
                    return header
                }
            }
            queue.add(jasonRequest)

        }else{
            val dialog = AlertDialog.Builder(this@BookDetailsActivity)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Setting"){text, Listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit"){text, Listner ->
                ActivityCompat.finishAffinity(this@BookDetailsActivity)
            }

            dialog.create()
            dialog.show()
        }
    }

    fun btnAddToFavFunctionality(bookEntity: BookEntity){
        val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
        val isFav = checkFav.get()
        if(isFav){
            btnAddtoFav.text = "REMOVE FROM FAVOURITES"
            val removeColor = ContextCompat.getColor(applicationContext, R.color.removeFavColor)
            btnAddtoFav.setBackgroundColor(removeColor)
        }
        else{
            btnAddtoFav.text = "ADD TO FAVOURITES"
            val addColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
            btnAddtoFav.setBackgroundColor(addColor)
        }

        btnAddtoFav.setOnClickListener {
            if(!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()){
                val result = DBAsyncTask(applicationContext, bookEntity, 2).execute().get()
                if(result){
                    Toast.makeText(applicationContext, "Book Added to Favourites", Toast.LENGTH_SHORT).show()
                    btnAddtoFav.text = "REMOVE FROM FAVOURITES"
                    val removeColor = ContextCompat.getColor(applicationContext, R.color.removeFavColor)
                    btnAddtoFav.setBackgroundColor(removeColor)
                }else{
                    Toast.makeText(applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val result = DBAsyncTask(applicationContext, bookEntity, 3).execute().get()
                if(result){
                    Toast.makeText(applicationContext, "Book Removed from Favourites", Toast.LENGTH_SHORT).show()
                    btnAddtoFav.text = "ADD TO FAVOURITES"
                    val addColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                    btnAddtoFav.setBackgroundColor(addColor)
                }else{
                    Toast.makeText(applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    class DBAsyncTask(context: Context, val bookEntity: BookEntity, val mode: Int):AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            var flag: Boolean = false
            when(mode) {
                1 -> {
                    // Check DB if the book is in favourite or not
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    flag =  book != null
                }
                2 -> {
                    // Add the book in the favourite
                    db.bookDao().insertBooks(bookEntity)
                    db.close()
                    flag =  true
                }
                3 -> {
                    // Remove the book from the favourite
                    db.bookDao().deleteBooks(bookEntity)
                    db.close()
                    flag =  true
                }
            }
            return flag
        }
    }


    }

