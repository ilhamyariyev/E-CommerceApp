package com.info.denemeecommerceapp2.fragments.shoppingFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.info.denemeecommerceapp2.MainActivity
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.adapters.HomeViewPagerAdapter
import com.info.denemeecommerceapp2.databinding.FragmentHomeBinding
import com.info.denemeecommerceapp2.fragments.categories.AccessoryFragment
import com.info.denemeecommerceapp2.fragments.categories.ChairFragment
import com.info.denemeecommerceapp2.fragments.categories.CupboardFragment
import com.info.denemeecommerceapp2.fragments.categories.FurnitureFragment
import com.info.denemeecommerceapp2.fragments.categories.MainCategoryFragment
import com.info.denemeecommerceapp2.fragments.categories.TableFragment
import com.info.denemeecommerceapp2.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import me.ibrahimsn.lib.SmoothBottomBar

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    val viewModel by viewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {

                when (it) {
                    is com.info.denemeecommerceapp2.util.Resource.Success ->{
                        val count = it.data?.size ?: 0
                        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(
                            R.id.bottomBar)
                        bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.purple_700)
                        }
                    }
                    else -> Unit
                }

            }
        }

       // (requireActivity() as MainActivity).supportActionBar!!.hide()

        val categoriesfFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )

        binding.viewPagerHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeViewPagerAdapter(categoriesfFragments,childFragmentManager,lifecycle)
        binding.viewPagerHome.adapter=viewPager2Adapter

        TabLayoutMediator(binding.tabLayout,binding.viewPagerHome){tab,position->
            when(position){
                0->tab.text = "Main"
                1->tab.text = "Chair"
                2->tab.text = "Cupboard"
                3->tab.text = "Table"
                4->tab.text = "Accessory"
                5->tab.text = "Furniture"


            }
        }.attach()
    }
}