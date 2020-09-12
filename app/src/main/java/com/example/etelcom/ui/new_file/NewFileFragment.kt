package com.example.etelcom.ui.new_file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.etelcom.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NewFileFragment : Fragment() {
    private lateinit var newFileViewModel: NewFileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newFileViewModel =
            ViewModelProviders.of(this).get(NewFileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_file, container, false)
        val textClient: TextView = root.findViewById(R.id.text_client)
        newFileViewModel.text.observe(viewLifecycleOwner, Observer {
            textClient.text = it
        })
        // Gets the date of the device
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val newDate = Date()
        val currentDate = dateFormat.format(newDate)
        val dateToFill: TextView = root.findViewById(R.id.date)
        dateToFill.text = "$currentDate"

        return root
    }
}