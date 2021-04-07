package com.training.foodrunner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderHistoryDao {
    @Insert
    fun insertItemsInOrderHistory(orderHistoryEntity: OrderHistoryEntity)

    @Delete
    fun deleteItemsFromOrderHistory(orderHistoryEntity: OrderHistoryEntity)

    @Query("SELECT * FROM order_history")
    fun getAllItemsFromOrderHistory(): List<OrderHistoryEntity>

    @Query("SELECT * FROM order_history WHERE orderId = :orderId")
    fun getItemsById(orderId: Int): OrderHistoryEntity

    @Query("SELECT count(orderId) FROM order_history ")
    fun countOrderHistoryRows(): Int

}