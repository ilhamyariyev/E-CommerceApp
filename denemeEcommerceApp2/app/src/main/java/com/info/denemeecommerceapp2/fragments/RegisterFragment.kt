package com.info.denemeecommerceapp2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.info.denemeecommerceapp2.MainActivity
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.data.User
import com.info.denemeecommerceapp2.databinding.FragmentRegisterBinding
import com.info.denemeecommerceapp2.util.RegisterValidation
import com.info.denemeecommerceapp2.util.Resource
import com.info.denemeecommerceapp2.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {
   private lateinit var binding:FragmentRegisterBinding
   private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.textViewNewAccount.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.tologin1())
        }

        binding.imageViewback.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.tologin1())
        }
        binding.apply {
            buttonRegister.setOnClickListener {
                val user =User(
                    editTextFirstName.text.toString().trim(),
                    editTextLastName.text.toString().trim(),
                    editTextEmail.text.toString().trim()
                )
                val password = editTextPassword.text.toString().trim()
                val confirmPassword = editTextConfirmPassword.text.toString().trim()
                viewModel.createAccountWithEmailAndPassword(user,password,confirmPassword)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading->{
                        binding.buttonRegister.startAnimation()
                    }
                    is Resource.Success->{
                        Log.d("test",it.data.toString())
                        binding.buttonRegister.revertAnimation()
                        Toast.makeText(context, "Created Account Succesfully", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(RegisterFragmentDirections.tologin1())
                    }
                    is Resource.Error->{
                        Log.e(TAG,it.message.toString())
                        binding.buttonRegister.revertAnimation()
                    }
                    else->Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{validation->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.editTextEmail.apply {
                            requestFocus()
                            error=validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.editTextPassword.apply {
                            requestFocus()
                            error=validation.password.message
                        }
                    }
                }
                if (validation.cpassword is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.editTextConfirmPassword.apply {
                            requestFocus()
                            error=validation.cpassword.message
                        }
                    }
                }
            }
        }

    }
}