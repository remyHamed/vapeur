package com.example.vapeur

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vapeur.databinding.FragmentForgottenPasswordBinding
import com.example.vapeur.databinding.FragmentLoginBinding
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
 * Use the [forgotten_password.newInstance] factory method to
 * create an instance of this fragment.
 */
class forgotten_password : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentForgottenPasswordBinding

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
        // Inflate the layout for this fragment
        binding = FragmentForgottenPasswordBinding.inflate(inflater, container, false)
        val network : NetworkRequest = NetworkRequest()
        binding.buttonRegister.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO){
                val userEmail = binding.userEmail.text.toString()
                if(userEmail.isNotEmpty()){
                    try {
                      network.password(userEmail,"xxxx")
                    } catch (h: HttpException){
                        withContext(Dispatchers.Main){
                            Toast.makeText(getActivity(),h.toString(), Toast.LENGTH_SHORT).show()
                        }
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
         * @return A new instance of fragment forgotten_password.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            forgotten_password().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}