package com.example.etelcom.ui.new_file

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.example.etelcom.R
import org.w3c.dom.Text
import java.sql.Time
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

        // Display the current time in "beginning of the intervention"
        val interventionBtnBeg: Button = root.findViewById(R.id.interventionBtnBeg)
        val beginHour: TextView = root.findViewById(R.id.beginHour)
        interventionBtnBeg.setOnClickListener {
            val dateFormat: DateFormat = SimpleDateFormat("HH:mm")
            val date = Date()
            val currentHour = dateFormat.format(date)
            val hourFormat: String = currentHour.replace(":", "h")
            beginHour.text = "$hourFormat"
        }

        // Display the current time in "end of the intervention"
        val interventionBtnEnd: Button = root.findViewById(R.id.interventionBtnEnd)
        val endHour: TextView = root.findViewById(R.id.endHour)
        interventionBtnEnd.setOnClickListener {
            val dateFormat: DateFormat = SimpleDateFormat("HH:mm")
            val date = Date()
            val currentHour = dateFormat.format(date)
            val hourFormat: String = currentHour.replace(":", "h")
            endHour.text = "$hourFormat"
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

        // Fill duration time
        val durationAction: AppCompatImageButton = root.findViewById(R.id.durationAction)
        durationAction.setOnClickListener {
            val editBegin: TextView = root.findViewById(R.id.beginHour)
            val editEnd: TextView = root.findViewById(R.id.endHour)
            if (editBegin.text.toString().isEmpty() || editEnd.text.toString().isEmpty()) {
                val errorToast = Toast.makeText(requireActivity(),"Calcul impossible", Toast.LENGTH_LONG)
                errorToast.show();
            }
            else if (editBegin.text.toString().length != 5 || editEnd.text.toString().length != 5) {
                val errorToast = Toast.makeText(requireActivity(),"Le format de l'heure doit" +
                        " être celui-ci : 00h00", Toast.LENGTH_LONG)
                errorToast.show();
            }
            else {
                val beginHour: String = editBegin.text.substring(0, 2)
                val beginMinute: String = editBegin.text.substring(3, 5)
                val endHour: String = editEnd.text.substring(0, 2)
                val endMinute: String = editEnd.text.substring(3, 5)

                val stop = Time(beginHour.toInt(), beginMinute.toInt(), 0)
                val start = Time(endHour.toInt(), endMinute.toInt(), 0)
                val diff: Time

                diff = difference(start, stop)
                val diffHours = String.format("%02d", diff.hours)
                val diffMinutes = String.format("%02d", diff.minutes)
                val durationTime: String = diffHours + "h" + diffMinutes
                val editDuration: TextView = root.findViewById(R.id.durationTime)
                editDuration.text = durationTime
            }
        }
        // Access the items of the tech list
        val technicians = resources.getStringArray(R.array.technicians)
        // Access the spinner
        val spinner: Spinner = root.findViewById(R.id.tech_spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_dropdown_item, technicians
            )
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    return
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    return
                }
            }
        }
        return root
    }
}

fun difference(start: Time, stop: Time): Time {
    val diff = Time(0, 0, 0)

    if (stop.seconds > start.seconds) {
        --start.minutes
        start.seconds += 60
    }

    diff.seconds = start.seconds - stop.seconds
    if (stop.minutes > start.minutes) {
        --start.hours
        start.minutes += 60
    }

    diff.minutes = start.minutes - stop.minutes
    diff.hours = start.hours - stop.hours

    return diff
}