package com.cal.foodorderingapp.models

data class CartItemModel(
    var foodName : String? = null,
    var foodPrice : String? = null,
    var foodDescription : String? = null,
    var foodImage : String? = null,
    var foodIngredients : String? = null,
    var foodQuantity : Int? = 0,
)