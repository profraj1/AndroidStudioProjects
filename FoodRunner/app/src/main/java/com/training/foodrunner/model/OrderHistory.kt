package com.training.foodrunner.model

data class OrderHistory(
    val resName: String,
    val orderDate: String,
    val orderItemList: ArrayList<RestaurantItemList>
)