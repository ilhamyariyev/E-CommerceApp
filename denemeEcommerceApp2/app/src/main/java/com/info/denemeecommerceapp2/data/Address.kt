package com.info.denemeecommerceapp2.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Address(
    val adressTitle:String,
    val fullName:String,
    val street:String,
    val phone:String,
    val city:String,
    val state:String,
):Parcelable{

    constructor() : this("","","","","","")
}
