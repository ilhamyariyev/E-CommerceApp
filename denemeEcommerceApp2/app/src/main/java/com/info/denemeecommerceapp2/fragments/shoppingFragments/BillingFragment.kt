package com.info.denemeecommerceapp2.fragments.shoppingFragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.adapters.AddressAdapter
import com.info.denemeecommerceapp2.adapters.BillingProductsAdapter
import com.info.denemeecommerceapp2.data.Address
import com.info.denemeecommerceapp2.data.CartProduct
import com.info.denemeecommerceapp2.data.order.Order
import com.info.denemeecommerceapp2.data.order.OrderStatus
import com.info.denemeecommerceapp2.databinding.FragmentBillingBinding
import com.info.denemeecommerceapp2.util.HorizontalItemDecoration
import com.info.denemeecommerceapp2.util.Resource
import com.info.denemeecommerceapp2.viewmodel.BillingViewModel
import com.info.denemeecommerceapp2.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding:FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductsAdapter by lazy {BillingProductsAdapter()}
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f

    private var selectedAddress:Address?=null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        products= args.products.toList()
        totalPrice = args.totalPrice
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBillingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBillingProductsRv()
        setupAddressRv()

        binding.imgAddAddress.setOnClickListener{
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        lifecycleScope.launchWhenStarted {
            billingViewModel.address.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressbarAddresses.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddresses.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        binding.progressbarAddresses.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.btnPlaceOlder.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.btnPlaceOlder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(requireView(),"Your order was placed",Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        binding.btnPlaceOlder.revertAnimation()
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


        billingProductsAdapter.differ.submitList(products)

        binding.tvTotalprice.text = "$ $totalPrice"

        addressAdapter.onClick = {
            selectedAddress = it
        }

        binding.btnPlaceOlder.setOnClickListener{
            if (selectedAddress == null){
                Toast.makeText(requireContext(), "Please select any address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showOrderConfirmationDialog()
        }
    }

    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Do you want to order your cart items?")
            setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes"){dialog,_->
                val order = Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setupBillingProductsRv() {
       binding.rvProducts.apply {
           layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
           adapter=billingProductsAdapter
           addItemDecoration(HorizontalItemDecoration())
       }
    }

    private fun setupAddressRv() {
       binding.rvAdresses.apply {
           layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
           adapter = addressAdapter
           addItemDecoration(HorizontalItemDecoration())
       }
    }


}