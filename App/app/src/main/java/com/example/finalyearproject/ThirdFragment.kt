package com.example.finalyearproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Break button
        view.findViewById<Button>(R.id.button_break).setOnClickListener {
            //findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            Toast.makeText(context,"You are on break", Toast.LENGTH_SHORT).show()
        }

        // Clock-Out button, goes to Get Started Page
        view.findViewById<Button>(R.id.button_clockout).setOnClickListener {
            findNavController().navigate(R.id.action_ThirdFragment_to_FirstFragment)
            Toast.makeText(context,"You have clocked out", Toast.LENGTH_SHORT).show()
        }
    }

}