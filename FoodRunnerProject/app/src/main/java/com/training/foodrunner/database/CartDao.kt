package com.training.foodrunner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface CartDao {
    @Insert
    fun insertItemsInCart(cartEntity: CartEntity)

    @Delete
    fun deleteItemsFromCart(cartEntity: CartEntity)

    @Query("SELECT * FROM cart")
    fun getAllItemsFromCart(): List<CartEntity>

    @Query("SELECT * FROM cart WHERE item_id = :itemId")
    fun getItemsById(itemId: Int): CartEntity

    @Query("DELETE FROM cart" )
    fun removeAllItemsFromCart()
}