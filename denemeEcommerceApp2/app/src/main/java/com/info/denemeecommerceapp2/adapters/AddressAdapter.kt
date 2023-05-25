package com.info.denemeecommerceapp2.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.data.Address
import com.info.denemeecommerceapp2.databinding.AddressRvItemBinding

class AddressAdapter :Adapter<AddressAdapter.AddressViewHolder>(){

    inner class AddressViewHolder(val binding:AddressRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(address: Address,isSelected:Boolean) {
            binding.apply {
                buttonAddress.text=address.adressTitle
                if (isSelected){
                    buttonAddress.setBackgroundColor(Color.BLUE)
                }else{
                    buttonAddress.setBackgroundColor(Color.LTGRAY)
                }
            }
        }


    }

    private val diffUtil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.adressTitle == newItem.adressTitle && oldItem.fullName==newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
           return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
       return AddressViewHolder(
           AddressRvItemBinding.inflate(
               LayoutInflater.from(parent.context)
           )
       )
    }


    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    var selectedAddress = -1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address,selectedAddress==position)

        holder.binding.buttonAddress.setOnClickListener {
            if (selectedAddress >=0)
                notifyItemChanged(selectedAddress)
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onClick?.invoke(address)
        }
    }

    init {
        differ.addListListener{_,_->
            notifyItemChanged(selectedAddress)
        }
    }

    var onClick : ((Address) -> Unit)? = null
}