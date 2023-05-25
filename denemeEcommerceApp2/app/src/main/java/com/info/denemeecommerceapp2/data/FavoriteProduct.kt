package com.info.denemeecommerceapp2.data

import android.os.Parcelable
import com.info.denemeecommerceapp2.data.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteProduct(
    val product:Product,
    val selectedColor : Int?=null,
    val selectedSize : String? = null
) :Parcelable{

    constructor():this(Product(),null,null)
}