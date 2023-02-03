package com.example.vapeur

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.vapeur.databinding.FragmentRegisterPlayerBinding
import com.example.vapeur.networkRequest.NetworkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [registerPlayer.newInstance] factory method to
 * create an instance of this fragment.
 */
class registerPlayer : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentRegisterPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
   // user_nameuser_emailpassword_login
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentRegisterPlayerBinding.inflate(inflater, container, false)
       val network : NetworkRequest = NetworkRequest()
       binding.buttonRegister.setOnClickListener {

           GlobalScope.launch(Dispatchers.IO) {

               val username = binding.userName.text.toString()
               val emailInput = binding.userEmail.text.toString()
               val passwordInput = binding.passwordLogin.text.toString()
               val confirmpassword = binding.passwordVerification.text.toString()

               if(
                   username.isNotEmpty() &&
                   passwordInput.isNotEmpty() &&
                   emailInput.isNotEmpty() &&
                   confirmpassword.isNotEmpty()
               ) {
                   if(confirmpassword == passwordInput) {
                    try{
                        val user = network.create(
                            username,
                            emailInput,
                            passwordInput
                        )
                        withContext(Dispatchers.Main){
                            findNavController().navigate(R.id.MostPlayedGameList)
                        }

                    }catch (h: HttpException){
                       withContext(Dispatchers.Main){
                           Toast.makeText(getActivity(),h.toString(), Toast.LENGTH_SHORT).show()
                       }
                   }

                   }
                   else {
                       withContext(Dispatchers.Main){
                           Toast.makeText(getActivity(), "password et confirmation différents", Toast.LENGTH_SHORT).show()
                       }
                   }
               } else {
                   withContext(Dispatchers.Main){
                       Toast.makeText(getActivity(), "vous devez remplir les champs", Toast.LENGTH_SHORT).show()
                   }
               }

           }



       }


       return binding.root;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment registerPlayer.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            registerPlayer().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}