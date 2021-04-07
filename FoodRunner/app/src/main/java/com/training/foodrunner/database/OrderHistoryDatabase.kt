package com.training.foodrunner.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [OrderHistoryEntity::class], version = 1)
abstract class OrderHistoryDatabase: RoomDatabase() {
    abstract fun orderHistoryDao(): OrderHistoryDao
}