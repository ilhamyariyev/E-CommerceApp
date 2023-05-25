package com.info.denemeecommerceapp2.welcomeslider

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.info.denemeecommerceapp2.MainActivity
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.databinding.FragmentSliderBinding


class SliderFragment : Fragment() {
    private lateinit var binding:FragmentSliderBinding
    private lateinit var viewPager2: ViewPager2

    private val pager2CallBack = object :ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position==PageLists.pageSlides.size -1){
                binding.buttonDavamEt.text="Got it"
                binding.buttonDavamEt.setOnClickListener {
                    findNavController().navigate(SliderFragmentDirections.tologin())
                }
            }else{
                binding.buttonDavamEt.text="Next"
                binding.buttonDavamEt.setOnClickListener {
                    viewPager2.currentItem = position+1
                }
            }

            if (position==PageLists.pageSlides.size -1){
                binding.buttonSkip.visibility=View.GONE

            }else{
                binding.buttonSkip.text="Skip"
                binding.buttonSkip.visibility=View.VISIBLE
                binding.buttonSkip.setOnClickListener {
                    viewPager2.currentItem = PageLists.pageSlides.size-1
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSliderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager(binding)

    }


    private fun setupViewPager(binding: FragmentSliderBinding){

        val adapter = PagesAdapter(PageLists.pageSlides)
        viewPager2=binding.viewPager2
        viewPager2.adapter=adapter
        viewPager2.registerOnPageChangeCallback(pager2CallBack)
        binding.dotsIndicator.setViewPager2(viewPager2)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewPager2.unregisterOnPageChangeCallback(pager2CallBack)
    }

}