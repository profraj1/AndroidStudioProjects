package com.training.foodrunner.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant")
data class RestaurantEntity(
    @PrimaryKey val res_id: Int,
    @ColumnInfo(name = "res_name") val resName: String,
    @ColumnInfo(name = "res_rating") val resRating:String,
    @ColumnInfo(name = "res_cost") val resCost: String,
    @ColumnInfo(name = "res_image") val resImage: String
)