package com.info.denemeecommerceapp2.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.databinding.FragmentVerifyBinding
import com.info.denemeecommerceapp2.fragments.shoppingFragments.HomeFragment


class VerifyFragment : Fragment() {

    private lateinit var binding:FragmentVerifyBinding
    private  lateinit var auth: FirebaseAuth
    private val args by navArgs<VerifyFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentVerifyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth=FirebaseAuth.getInstance()

        val storedVerificationId = args.storedverificationid

        binding.verifyBtn.setOnClickListener{
            var otp=binding.idOtp.text.toString().trim()
            if(!otp.isEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId, otp)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(requireContext(),"Enter OTP",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(VerifyFragmentDirections.tohome5())
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(),"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}