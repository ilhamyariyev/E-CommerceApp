package com.info.denemeecommerceapp2.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.info.denemeecommerceapp2.R
import com.info.denemeecommerceapp2.data.User
import com.info.denemeecommerceapp2.databinding.FragmentLoginBinding
import com.info.denemeecommerceapp2.dialog.setupBottomSheetDialog
import com.info.denemeecommerceapp2.util.Constants
import com.info.denemeecommerceapp2.util.Resource
import com.info.denemeecommerceapp2.viewmodel.LoginViewModel
import com.info.denemeecommerceapp2.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private lateinit var client:GoogleSignInClient
    private val viewModel by viewModels<LoginViewModel>()

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonMobile.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.tophone())
        }


        binding.textViewSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.toregister())
        }


        binding.apply {
            buttonLogin.setOnClickListener {
                val email = editTextEmail.text.toString().trim()
                val password = editTextPassword.text.toString().trim()
                if(email.isEmpty()){
                    binding.editTextEmail.error="Email cannot be empty"
                    return@setOnClickListener
                }
                if (password.isEmpty()){
                    binding.editTextPassword.error="Password cannot be empty"
                    return@setOnClickListener
                }
                viewModel.login(email,password)
            }

        }

        binding.textViewForgotPassword.setOnClickListener {
            setupBottomSheetDialog {email->
                viewModel.resetPassword(email)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect{
                when(it){
                    is Resource.Loading->{
                    }
                    is Resource.Success->{
                        Snackbar.make(requireView(),"Reset link was sent to your email",Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error->{
                        Snackbar.make(requireView(),"Error ${it.message}",Snackbar.LENGTH_LONG).show()
                    }
                    else->Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect{
                when(it){
                    is Resource.Loading->{
                        binding.buttonLogin.startAnimation()
                    }
                    is Resource.Success->{
                        binding.buttonLogin.revertAnimation()
                        findNavController().navigate(LoginFragmentDirections.toHome())
                    }
                    is Resource.Error->{
                        binding.buttonLogin.revertAnimation()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else->Unit
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(LoginFragmentDirections.towelcomeexit())
            }
        })


        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(requireActivity(),options)
        binding.buttonGoogle.setOnClickListener {
            val intent = client.signInIntent
            startActivityForResult(intent,10001)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==10001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {task->
                    if (task.isSuccessful){
                        findNavController().navigate(LoginFragmentDirections.toHome())
                    }else{
                        Toast.makeText(requireContext(), "Problem was happened", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }


}