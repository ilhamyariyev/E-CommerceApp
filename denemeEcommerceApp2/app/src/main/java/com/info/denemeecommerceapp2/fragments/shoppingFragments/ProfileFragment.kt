package com.info.denemeecommerceapp2.fragments.shoppingFragments


import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.datatransport.BuildConfig
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.databinding.FragmentProfileBinding
import com.info.denemeecommerceapp2.util.Resource
import com.info.denemeecommerceapp2.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }

        binding.allOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
        }

        binding.linearBilling.setOnClickListener {
            val action = ProfileFragmentDirections.tobilling(0f,
            emptyArray()
            )
            findNavController().navigate(action)
        }

        binding.linearOut.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Exit")
                setMessage("Do you want to exit?")
                setNegativeButton("No"){dialog,_ ->
                    dialog.dismiss()
                }
                setPositiveButton("Yes"){dialog,_->
                    viewModel.logout()
                    findNavController().navigate(ProfileFragmentDirections.tologin2())
                    dialog.dismiss()
                }
            }
            alertDialog.create()
            alertDialog.show()

        }

        binding.tvVersionCode.text = "Version ${BuildConfig.VERSION_CODE}"

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.progressbarSettings.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView()).load(it.data!!.imagePath).error(ColorDrawable(
                            Color.BLACK)).into(binding.imgUser)
                        binding.tvUserName.text = "${it.data.firstName} ${it.data.lastName}"
                    }
                    is Resource.Error ->{
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressbarSettings.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }
    }


}