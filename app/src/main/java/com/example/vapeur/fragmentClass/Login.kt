package com.example.vapeur.fragmentClass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vapeur.R
import com.example.vapeur.databinding.FragmentLoginBinding
import com.example.vapeur.forgotten_password
import com.example.vapeur.networkRequest.NetworkRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Login: Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val editText = findViewById(R.id.username)
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val network : NetworkRequest = NetworkRequest()



        binding.buttonLogin.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO) {

                val mailInput = binding.username.text.toString()
                val passwordInput = binding.passwordLogin.text.toString()
                if(mailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                  try{
                      val user = network.login(mailInput, passwordInput)
                      withContext(Dispatchers.Main){
                          Toast.makeText(getActivity(), user.mail, Toast.LENGTH_SHORT).show()
                          findNavController().navigate(R.id.MostPlayedGameList)
                      }
                      println("user" + user.mail)
                  }catch (h:HttpException){
                      withContext(Dispatchers.Main){
                          Toast.makeText(getActivity(),h.toString(), Toast.LENGTH_SHORT).show()
                      }
                  }
                } else {
                    withContext(Dispatchers.Main){
                        Toast.makeText(getActivity(), "vous devez remplir les champs", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }


        binding.buttonTestChloe.setOnClickListener {
:
            findNavController().navigate(R.id.MostPlayedGameList)
        }


        binding.buttonRegister.setOnClickListener{
            findNavController().navigate(R.id.forgotten_password)
        }
        return binding.root
    }

}