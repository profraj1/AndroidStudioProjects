package com.training.foodrunner.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.training.foodrunner.R
import com.training.foodrunner.activity.MainActivity
import com.training.foodrunner.adapter.CartAdapterClass
import com.training.foodrunner.adapter.FavouritesAdapterClass
import com.training.foodrunner.database.CartDatabase
import com.training.foodrunner.database.CartEntity
import com.training.foodrunner.database.RestaurantDatabase
import com.training.foodrunner.database.RestaurantEntity

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    lateinit var txtProfileName: TextView
    lateinit var txtProfileEmail: TextView
    lateinit var txtProfilePhone: TextView
    lateinit var txtProfileAddress: TextView

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        txtProfileName = view.findViewById(R.id.txtProfileName)
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail)
        txtProfilePhone = view.findViewById(R.id.txtProfilePhone)
        txtProfileAddress = view.findViewById(R.id.txtProfileAddress)

        sharedPreferences = activity?.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        txtProfileName.text = sharedPreferences?.getString("Name", null)
        txtProfileEmail.text = sharedPreferences?.getString("EmailAddress", null)
        txtProfilePhone.text = sharedPreferences?.getString("MobileNo", null)
        txtProfileAddress.text = sharedPreferences?.getString("DeliveryAddress", null)

        return view
    }



}
