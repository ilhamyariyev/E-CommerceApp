package com.info.denemeecommerceapp2.fragments.shoppingFragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.adapters.FavoriteProductAdapter
import com.info.denemeecommerceapp2.databinding.FragmentFavoriteBinding
import com.info.denemeecommerceapp2.firebase.FirebaseCommon
import com.info.denemeecommerceapp2.util.Resource
import com.info.denemeecommerceapp2.util.VerticalItemDecoration
import com.info.denemeecommerceapp2.viewmodel.FavoriteViewModel
import kotlinx.coroutines.flow.collectLatest

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteAdapter by lazy {  FavoriteProductAdapter() }
    private val viewModel by activityViewModels<FavoriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFavoriteRv()


        favoriteAdapter.onProductClick = {
            val b = Bundle().apply { putParcelable("product",it.product) }
            findNavController().navigate(R.id.action_favoriteFragment_to_productDetailsFragment,b)
        }

        favoriteAdapter.onDeleteClick = {

                    val alertDialog = AlertDialog.Builder(requireContext()).apply {
                        setTitle("Delete item from cart")
                        setMessage("Do you want to delete this item from your cart?")
                        setNegativeButton("Cancel"){dialog,_ ->
                            dialog.dismiss()
                        }
                        setPositiveButton("Delete"){dialog,_->
                            viewModel.deleteFavoriteProduct(it)
                            dialog.dismiss()
                        }
                    }
                    alertDialog.create()
                    alertDialog.show()


        }



        lifecycleScope.launchWhenStarted {
            viewModel.favoriteProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressbarFavorite.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressbarFavorite.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()){

                        }else{
                            favoriteAdapter.differ.submitList(it.data)

                        }
                    }
                    is Resource.Error ->{
                        binding.progressbarFavorite.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }

            }
        }

    }



    private fun setupFavoriteRv() {
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            adapter = favoriteAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}