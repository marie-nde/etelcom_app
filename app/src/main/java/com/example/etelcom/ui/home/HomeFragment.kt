package com.example.etelcom.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.etelcom.R
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textButton: TextView = root.findViewById(R.id.interventionHour)
        // The text of the button is in HomeViewModel
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textButton.text = it
        })
        val interventionButton: Button = root.findViewById(R.id.interventionHour)
        interventionButton.setOnClickListener { view ->
            // Gets the hour of the device
            val dateFormat: DateFormat = SimpleDateFormat("HH:mm")
            val date = Date()
            val currentHour = dateFormat.format(date)
            Snackbar.make(view, "Heure de début d'intervention définie à : $currentHour", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        return root
    }
}