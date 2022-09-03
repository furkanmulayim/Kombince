package com.example.kombince.view.fragmentview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController

import androidx.navigation.ui.setupWithNavController
import com.example.kombince.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashBoardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_dash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bottomNaviagtionview: BottomNavigationView = view.findViewById(R.id.bottom_navigation)
        val navController: NavController =
            findNavController(requireActivity(), R.id.fragmentContainerView2)
        bottomNaviagtionview.setupWithNavController(navController)

        isimVer()
    }


    private fun isimVer() {

        var nameSurname = view?.findViewById(R.id.nameSurname) as TextView

        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            val personName = acct.displayName.toString()
            nameSurname.text = personName
        }
    }

}