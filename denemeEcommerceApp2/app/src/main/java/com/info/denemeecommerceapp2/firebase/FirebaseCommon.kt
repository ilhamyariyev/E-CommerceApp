package com.info.denemeecommerceapp2.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.info.denemeecommerceapp2.data.CartProduct
import com.info.denemeecommerceapp2.data.FavoriteProduct
import java.lang.Exception

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val cartCollection =
        firestore.collection("user").document(auth.uid!!).collection("cart")

    private val favoriteCollection =
        firestore.collection("user").document(auth.uid!!).collection("favorite")

    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?,Exception?) -> Unit){
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct,null)
            }.addOnFailureListener {
                onResult(null,it)
            }
    }

    fun addProductToFavorite(favoriteProduct: FavoriteProduct, onResult: (FavoriteProduct?, Exception?) -> Unit){
        favoriteCollection.document().set(favoriteProduct)
            .addOnSuccessListener {
                onResult(favoriteProduct,null)
            }.addOnFailureListener {
                onResult(null,it)
            }
    }


    fun increaseQuantity(documentId:String,onResult:(String?,Exception?)->Unit){
        firestore.runTransaction{transition->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let {cartProduct->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }

    fun decreaseQuantity(documentId:String,onResult:(String?,Exception?)->Unit){
        firestore.runTransaction{transition->
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let {cartProduct->
                val newQuantity = cartProduct.quantity - 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }

    enum class QuantityChanging{
        INCREASE,DECREASE
    }
}