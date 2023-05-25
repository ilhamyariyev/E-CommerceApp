package com.info.denemeecommerceapp2.data.order

import android.os.Parcelable
import com.info.denemeecommerceapp2.data.Address
import com.info.denemeecommerceapp2.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong


@Parcelize
data class Order(
    val orderStatus:String = "",
    val totalPrice:Float = 0f,
    val products:List<CartProduct> = emptyList(),
    val address:Address = Address(),
    val date:String = SimpleDateFormat("yyyy_MM_dd", Locale.ENGLISH).format(Date()),
    val orderId: Long = nextLong(0,100_000_000_000)+ totalPrice.toLong()
):Parcelable
