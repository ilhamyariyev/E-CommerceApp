package com.info.denemeecommerceapp2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.info.denemeecommerceapp2.data.CartProduct
import com.info.denemeecommerceapp2.data.FavoriteProduct
import com.info.denemeecommerceapp2.firebase.FirebaseCommon
import com.info.denemeecommerceapp2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth:FirebaseAuth,
    private val firebaseCommon : FirebaseCommon
) :ViewModel(){

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()

    private val _addToFavorite = MutableStateFlow<Resource<FavoriteProduct>>(Resource.Unspecified())
    val addToFavorite = _addToFavorite.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct){
        viewModelScope.launch { _addToCart.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id",cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()){
                        addNewProduct(cartProduct)
                    }else{
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product==cartProduct){
                            val documentId = it.first().id
                            increaseQuantity(documentId,cartProduct)
                        }else{
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToCart.emit(Resource.Error(it.message.toString())) }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct){
        firebaseCommon.addProductToCart(cartProduct){addedProduct,e->
            viewModelScope.launch {
                if (e==null)
                    _addToCart.emit(Resource.Success(addedProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun increaseQuantity(documentId : String,cartProduct: CartProduct){
        firebaseCommon.increaseQuantity(documentId){_,e->
            viewModelScope.launch {
                if (e==null)
                    _addToCart.emit(Resource.Success(cartProduct))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    fun addUpdateProductInFavorite(favoriteProduct: FavoriteProduct){
        viewModelScope.launch { _addToFavorite.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("favorite")
            .whereEqualTo("product.id",favoriteProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()){
                        addNewProduct(favoriteProduct)
                    }else{
                        val product = it.first().toObject(FavoriteProduct::class.java)
                        if (product==favoriteProduct){
                            val documentId = it.first().id
                            increaseQuantity(documentId,favoriteProduct)
                        }else{
                            addNewProduct(favoriteProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToFavorite.emit(Resource.Error(it.message.toString())) }
            }
    }

    private fun addNewProduct(favoriteProduct: FavoriteProduct){
        firebaseCommon.addProductToFavorite(favoriteProduct){addedProduct,e->
            viewModelScope.launch {
                if (e==null)
                    _addToFavorite.emit(Resource.Success(addedProduct!!))
                else
                    _addToFavorite.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun increaseQuantity(documentId : String,favoriteProduct: FavoriteProduct){
        firebaseCommon.increaseQuantity(documentId){_,e->
            viewModelScope.launch {
                if (e==null)
                    _addToFavorite.emit(Resource.Success(favoriteProduct))
                else
                    _addToFavorite.emit(Resource.Error(e.message.toString()))
            }
        }
    }

}