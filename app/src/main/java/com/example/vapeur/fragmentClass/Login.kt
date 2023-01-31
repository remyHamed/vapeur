package com.example.vapeur.fragmentClass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.vapeur.R
import com.example.vapeur.networkClass.UserLogin
import com.example.vapeur.networkRequest.NetworkRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Login: Fragment() {
    val network :NetworkRequest = NetworkRequest()
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        button_login

        GlobalScope.launch(Dispatchers.Main) {
            val user  = network.login("r@r.fr", "azerty")
            println(user._id)
        }
        return inflater.inflate(R.layout.fragment_login, container, false)

    }
}