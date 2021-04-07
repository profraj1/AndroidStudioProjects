package com.training.foodrunner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RestaurantDao {
    @Insert
    fun insertItemsInRestaurant(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteItemsFromRestaurant(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM restaurant")
    fun getAllItemsFromRestaurant(): List<RestaurantEntity>

    @Query("SELECT * FROM restaurant WHERE res_id = :itemId")
    fun getResItemsById(itemId: Int): RestaurantEntity
}