package com.info.denemeecommerceapp2.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.info.denemeecommerceapp2.data.Product
import com.info.denemeecommerceapp2.databinding.BestDealsRvItemBinding


class BestDealsAdapter() :RecyclerView.Adapter<BestDealsAdapter.BestDielsViewHolder>() {


    inner class BestDielsViewHolder(private val binding: BestDealsRvItemBinding,):ViewHolder(binding.root){
        fun bind(product:Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imagBestDeal)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDielsViewHolder {
        return BestDielsViewHolder(
            BestDealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestDielsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener{
            onClick?.invoke(product)
        }
    }

    var onClick: ((Product) -> Unit)? = null


}

