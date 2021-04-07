package com.training.echomusicapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.training.echomusicapp.R
import com.training.echomusicapp.activity.MainActivity
import com.training.echomusicapp.fragment.AboutUsFragment
import com.training.echomusicapp.fragment.AllSongsFragment
import com.training.echomusicapp.fragment.FavouriteFragment
import com.training.echomusicapp.fragment.SettingFragment
import com.training.echomusicapp.model.NavMenuItem

class NavMenuAdapterClass(
    val context: Context,
    val menuItemList: ArrayList<NavMenuItem>
) : RecyclerView.Adapter<NavMenuAdapterClass.NavMenuViewHolder>() {

    class NavMenuViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val navItemsContentHolder: RelativeLayout = view.findViewById(R.id.navItemsContentHolder)
        val imgNavMenuIcon: ImageView = view.findViewById(R.id.imgNavMenuIcon)
        val txtNavItemName: TextView = view.findViewById(R.id.txtNavItemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_nav_menu_item, parent, false)
        return NavMenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuItemList.size
    }

    override fun onBindViewHolder(holder: NavMenuViewHolder, position: Int) {
        val item = menuItemList[position]
        holder.txtNavItemName.text = item.menuItemName
        holder.imgNavMenuIcon.setBackgroundResource(item.menuItemIcon)
        holder.navItemsContentHolder.setOnClickListener {
            if(position == 0){
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, AllSongsFragment()).commit()
                context.supportActionBar?.title = "All Songs"
                context.drawerLayout.closeDrawer(context.navigationView)
            }
            else if(position == 1){
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, FavouriteFragment()).commit()
                (context as MainActivity).supportActionBar?.title = "Favourites"
                context.drawerLayout.closeDrawer(context.navigationView)
            }
            else if(position == 2){
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, SettingFragment()).commit()
                (context as MainActivity).supportActionBar?.title = "Settings"
                context.drawerLayout.closeDrawer(context.navigationView)
            }
            else if(position == 3){
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, AboutUsFragment()).commit()
                (context as MainActivity).supportActionBar?.title = "About Us"
                context.drawerLayout.closeDrawer(context.navigationView)
            }
        }
    }
}