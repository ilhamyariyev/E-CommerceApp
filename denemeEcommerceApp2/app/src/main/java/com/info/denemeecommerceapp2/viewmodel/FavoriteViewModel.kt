package com.info.denemeecommerceapp2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.info.denemeecommerceapp2.data.CartProduct
import com.info.denemeecommerceapp2.data.FavoriteProduct
import com.info.denemeecommerceapp2.firebase.FirebaseCommon
import com.info.denemeecommerceapp2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _favoriteProducts = MutableStateFlow<Resource<List<FavoriteProduct>>>(Resource.Unspecified())
    val favoriteProducts = _favoriteProducts.asStateFlow()


    private var favoriteProductDocuments = emptyList<DocumentSnapshot>()

    fun deleteFavoriteProduct(favoriteProduct: FavoriteProduct) {
        val index = favoriteProducts.value.data?.indexOf(favoriteProduct)
        if(index != null && index != -1){
            val documentId = favoriteProductDocuments[index].id
            firestore.collection("user").document(auth.uid!!).collection("favorite")
                .document(documentId).delete()
        }
    }

    init {
        getFavoriteProducts()
    }



    private fun getFavoriteProducts(){
        viewModelScope.launch { _favoriteProducts.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("favorite")
            .addSnapshotListener{value, error->
                if (error !=null || value == null){
                    viewModelScope.launch { _favoriteProducts.emit(Resource.Error(error?.message.toString())) }
                }else{
                    favoriteProductDocuments = value.documents
                    val favoriteProducts = value.toObjects(FavoriteProduct::class.java)
                    viewModelScope.launch {_favoriteProducts.emit(Resource.Success(favoriteProducts))}
                }
            }
    }

}