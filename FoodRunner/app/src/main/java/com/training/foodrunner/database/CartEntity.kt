package com.training.foodrunner.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val item_id: Int,
    @ColumnInfo(name = "item_name") val itemName: String,
    @ColumnInfo(name = "cost_for_one") val costForOne: String,
    @ColumnInfo(name = "restaurant_id") val restaurantId: String
) {
}