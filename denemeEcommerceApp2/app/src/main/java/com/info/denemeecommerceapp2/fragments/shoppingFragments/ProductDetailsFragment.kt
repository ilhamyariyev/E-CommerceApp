package com.info.denemeecommerceapp2.fragments.shoppingFragments

import android.graphics.Color
import android.graphics.Paint
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
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.adapters.ColorsAdapter
import com.info.denemeecommerceapp2.adapters.SizesAdapter
import com.info.denemeecommerceapp2.adapters.ViewPager2Images
import com.info.denemeecommerceapp2.data.CartProduct
import com.info.denemeecommerceapp2.data.FavoriteProduct
import com.info.denemeecommerceapp2.databinding.FragmentProductDetailsBinding
import com.info.denemeecommerceapp2.util.Resource
import com.info.denemeecommerceapp2.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private lateinit var binding:FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy{ ViewPager2Images()}
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private var selectedColor: Int?=null
    private var selectedSize : String?=null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentProductDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupSizesRv()
        setupColorsRv()
        setupViewpager()

        binding.imageClose.setOnClickListener{
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.buttonAddToCart.setOnClickListener{
            viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
        }

        var isFavorite=false
        binding.buttonAddWishlist.setOnClickListener {
            viewModel.addUpdateProductInFavorite(FavoriteProduct(product,selectedColor,selectedSize))

            val imageRes = if (isFavorite){
                R.drawable.baseline_favorite_border_24
            }else{
                R.drawable.baseline_favorite_24
            }
            binding.buttonAddWishlist.setIconResource(imageRes)
            isFavorite=!isFavorite
            Toast.makeText(requireContext(), "Product was added Favorite", Toast.LENGTH_SHORT).show()

        }


        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.buttonAddToCart.startAnimation()
                    }

                    is Resource.Success ->{
                        binding.buttonAddToCart.revertAnimation()
                        Toast.makeText(requireContext(), "Product was added", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Error -> {
                        binding.buttonAddToCart.stopAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            tvProductName.text=product.name
            tvProductOldPrice.text = "$ ${product.price}"
            product.offerPercentage?.let {
                val remainingPricePercentage = 1f-it
                val priceAfterOffer = remainingPricePercentage * product.price
                tvProductNewPrice.text = "$ ${String.format("%.2f",priceAfterOffer)}"
                tvProductOldPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
            }
            if (product.offerPercentage == null)
                tvProductNewPrice.visibility=View.INVISIBLE
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty())
                tvProductColors.visibility= View.INVISIBLE
            if (product.sizes.isNullOrEmpty())
                tvProductSizes.visibility= View.INVISIBLE
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }
    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImages.adapter= viewPagerAdapter
        }
    }

    private fun setupColorsRv() {
      binding.rvColors.apply {
          adapter = colorsAdapter
          layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
      }
    }

    private fun setupSizesRv() {
        binding.rvSize.apply {
            adapter = sizesAdapter
            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }


}