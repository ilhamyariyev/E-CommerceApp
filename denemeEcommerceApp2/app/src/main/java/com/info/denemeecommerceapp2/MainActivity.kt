package com.info.denemeecommerceapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.load.engine.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.info.denemeecommerceapp2.databinding.ActivityMainBinding
import com.info.denemeecommerceapp2.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController =navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomBar,navController)

        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                when(destination.id){
                    R.id.welcomeFragment ->binding.bottomBar.visibility= View.GONE
                    R.id.registerFragment ->binding.bottomBar.visibility= View.GONE
                    R.id.loginFragment ->binding.bottomBar.visibility= View.GONE
                    R.id.sliderFragment ->binding.bottomBar.visibility= View.GONE
                    R.id.productDetailsFragment ->binding.bottomBar.visibility= View.GONE
                    R.id.phoneFragment ->binding.bottomBar.visibility= View.GONE
                    R.id.verifyFragment ->binding.bottomBar.visibility= View.GONE


                    else->{
                        if (binding.bottomBar.visibility== View.GONE){
                            binding.bottomBar.visibility = View.VISIBLE
                        }
                    }
                }

            }
        })


    }
}