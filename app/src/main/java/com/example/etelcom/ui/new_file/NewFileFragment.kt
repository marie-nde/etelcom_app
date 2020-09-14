package com.example.etelcom.ui.new_file

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RemoteViews
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.etelcom.MainActivity
import com.example.etelcom.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NewFileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_new_file, container, false)

        val interventionBtn: Button = root.findViewById(R.id.interventionBtn)
        val beginHour: TextView = root.findViewById(R.id.beginHour)
        interventionBtn.setOnClickListener {
            val dateFormat: DateFormat = SimpleDateFormat("HH:mm")
            val date = Date()
            val currentHour = dateFormat.format(date)
            val hourFormat: String = currentHour.replace(":", "h")
            beginHour.text = "$hourFormat"
        }

        // Get current date to display it by default
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val newDate = Date()
        val currentDate = dateFormat.format(newDate)
        val dateToFill: TextView = root.findViewById(R.id.date)
        dateToFill.text = "$currentDate"

        val editDate: TextView = root.findViewById(R.id.date)
        val calendarBtn: AppCompatImageButton = root.findViewById(R.id.calBtn)
        calendarBtn.setOnClickListener {
            // Get current date for the date picker
            val c = Calendar.getInstance()
            val mYear = c[Calendar.YEAR]
            val mMonth = c[Calendar.MONTH]
            val mDay = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val dayWithLeadingZero = String.format("%02d", dayOfMonth)
                    val monthWithLeadingZero = String.format("%02d", monthOfYear + 1)
                    editDate.setText(
                        dayWithLeadingZero + "/" + monthWithLeadingZero + "/" + year
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }
        return root
    }
}