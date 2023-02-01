package com.example.vapeur.fragmentClass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vapeur.R
import com.example.vapeur.databinding.FragmentLoginBinding

class Login: Fragment() {
    //lateinit = sera initialisé obligatoirement
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.buttonTestChloe.setOnClickListener {
            findNavController().navigate(R.id.MostPlayedGameList)
        }

        return binding.root
    }
}