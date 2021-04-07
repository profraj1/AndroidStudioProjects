package com.training.foodrunner.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonArray
import com.training.foodrunner.model.RestaurantItemList

@Entity(tableName = "order_history")
data class OrderHistoryEntity(
    @PrimaryKey val orderId:Int,
    @ColumnInfo(name = "res_name") val resName: String,
    @ColumnInfo(name = "order_date") val orderDate: String,
    @ColumnInfo(name = "order_item_list") val orderItemList: String
)