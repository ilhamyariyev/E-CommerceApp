package com.info.denemeecommerceapp2.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.info.denemeecommerceapp2.MainActivity
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.databinding.FragmentWelcomeBinding
import com.info.denemeecommerceapp2.fragments.WelcomeFragmentDirections


class WelcomeFragment : Fragment() {
    private lateinit var binding:FragmentWelcomeBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth= Firebase.auth

        val imageView = requireActivity().findViewById<ImageView>(R.id.imageViewFastDelivery)
        val ani : Animation = AnimationUtils.loadAnimation(
            requireContext(),R.anim.left_to_right
        )
        imageView.setAnimation(ani)
        try {
            Handler().postDelayed(
                {
                    if (auth.currentUser==null){
                        findNavController().navigate(WelcomeFragmentDirections.toSlider())
                    }else{
                        findNavController().navigate(WelcomeFragmentDirections.tohome1())
                    }

                },2000
            )
        }catch (e:Exception){
            e.printStackTrace()
        }


    }


}