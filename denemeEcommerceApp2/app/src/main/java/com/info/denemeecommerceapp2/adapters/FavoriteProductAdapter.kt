package com.info.denemeecommerceapp2.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.info.denemeecommerceapp2.data.CartProduct
import com.info.denemeecommerceapp2.data.FavoriteProduct
import com.info.denemeecommerceapp2.databinding.FavoriteProductItemBinding
import com.info.denemeecommerceapp2.helper.getProductPrice

class FavoriteProductAdapter : RecyclerView.Adapter<FavoriteProductAdapter.FavoriteProductsViewHolder>(){

    inner class FavoriteProductsViewHolder( val binding: FavoriteProductItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(favoriteProduct: FavoriteProduct){
            binding.apply {
                Glide.with(itemView).load(favoriteProduct.product.images[0]).into(imageFavoriteProduct)
                tvProductFavoriteName.text = favoriteProduct.product.name

                val priceAfterPercentage = favoriteProduct.product.offerPercentage.getProductPrice(favoriteProduct.product.price)
                tvProductFavoritePrice.text = "$ ${String.format("%.2f", priceAfterPercentage)}"

                imageFavoriteProductColor.setImageDrawable(ColorDrawable(favoriteProduct.selectedColor?: Color.TRANSPARENT))
                tvFavoriteProductSize.text = favoriteProduct.selectedSize?:"".also { imageFavoriteProductSize.setImageDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                ) }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<FavoriteProduct>(){
        override fun areItemsTheSame(oldItem: FavoriteProduct, newItem: FavoriteProduct): Boolean {
            return oldItem.product.id ==newItem.product.id
        }

        override fun areContentsTheSame(oldItem: FavoriteProduct, newItem: FavoriteProduct): Boolean {
            return oldItem==newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductsViewHolder {
        return FavoriteProductsViewHolder(
            FavoriteProductItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoriteProductsViewHolder, position: Int) {
        val favoriteProduct = differ.currentList[position]
        holder.bind(favoriteProduct)

        holder.itemView.setOnClickListener{
            onProductClick?.invoke(favoriteProduct)
        }

        holder.binding.imageViewDelete.setOnClickListener{
            onDeleteClick?.invoke(favoriteProduct)
        }
    }

    var onProductClick: ((FavoriteProduct) -> Unit)? = null
    var onDeleteClick: ((FavoriteProduct) -> Unit)? = null

}