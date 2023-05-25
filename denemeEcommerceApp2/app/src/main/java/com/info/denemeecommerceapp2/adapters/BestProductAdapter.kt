package com.info.denemeecommerceapp2.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.collection.LLRBNode.Color
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.data.Product
import com.info.denemeecommerceapp2.databinding.BestDealsRvItemBinding
import com.info.denemeecommerceapp2.databinding.ProductRvItemBinding
import com.info.denemeecommerceapp2.helper.getProductPrice

class BestProductAdapter :RecyclerView.Adapter<BestProductAdapter.BestProductsViewHolder>(){


    inner class BestProductsViewHolder(private val binding: ProductRvItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imageViewProduct)
                product.offerPercentage?.let {
                    val remainingPricePercentage = 1f-it
                    val priceAfterOffer = remainingPricePercentage * product.price
                    textViewNewPrice.text = "$ ${String.format("%.2f",priceAfterOffer)}"
                    textViewOldPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
                }
                if (product.offerPercentage == null)
                    textViewNewPrice.visibility= View.INVISIBLE
                textViewOldPrice.text = "$ ${product.price}"
                textViewName.text = product.name


            }
        }
    }


    private val diffCallback =object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id ==newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        return BestProductsViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener{
            onClick?.invoke(product)
        }
    }

    var onClick: ((Product) -> Unit)? = null
}