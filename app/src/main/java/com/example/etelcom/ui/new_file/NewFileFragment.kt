package com.example.etelcom.ui.new_file

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.example.etelcom.R
import com.itextpdf.forms.PdfAcroForm
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import kotlinx.android.synthetic.main.fragment_new_file.*
import java.io.File
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NewFileFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
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
        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item, technicians
        )
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View?, position: Int, id: Long) {
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // Click on the validation button
        val validateBtn: Button = root.findViewById(R.id.validateBtn)
        validateBtn.setOnClickListener {
            // Save the data entered
            saveData()

            // Load the data saved
            loadData()

            // Create a directory "Fiches" if it doesn't exist
            var extStorageDirectory = requireActivity().getExternalFilesDir(null).toString()
            val dir = File("$extStorageDirectory/Fiches/")
            if (!dir.isDirectory) {
                dir.mkdir()
            }

            val intent = Intent(Intent.ACTION_VIEW)
            val dirFiles: Uri = Uri.parse("content://$dir")
            intent.setDataAndType(dirFiles, "*/*")
            startActivity(intent)
        }

        return root
    }

    private fun saveData() {
        val clientName: String = clientName.text.toString()
        val siteName: String = siteName.text.toString()
        val date: String = date.text.toString()
        val beginHour: String = beginHour.text.toString()
        val endHour: String = endHour.text.toString()
        val durationTime: String = durationTime.text.toString()
        val ref: String = ref.text.toString()
        val objectIntervention: String = objectIntervention.text.toString()
        val detailIntervention: String = detailIntervention.text.toString()
        val tech: String = tech_spinner.selectedItem.toString();

        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("CLIENT", clientName)
            putString("SITE", siteName)
            putString("DATE", date)
            putString("BEGIN", beginHour)
            putString("END", endHour)
            putString("DURATION", durationTime)
            putString("REF", ref)
            putString("OBJECT", objectIntervention)
            putString("DETAIL", detailIntervention)
            putString("TECH", tech)
            putBoolean("SITE1", checkBoxInter1.isChecked)
            putBoolean("PRISE", checkBoxInter2.isChecked)
            putBoolean("ATELIER", checkBoxInter3.isChecked)
            putBoolean("PC", checkBoxMaint1.isChecked)
            putBoolean("SERVER", checkBoxMaint2.isChecked)
            putBoolean("NETWORK", checkBoxMaint3.isChecked)
            putBoolean("PHONE", checkBoxMaint4.isChecked)
            putBoolean("DONE", checkBoxStatus1.isChecked)
            putBoolean("INPROGRESS", checkBoxStatus2.isChecked)
            putBoolean("DEVIS", checkBoxStatus3.isChecked)
            putBoolean("MAINTENANCE", checkBoxType1.isChecked)
            putBoolean("FACTURABLE", checkBoxType2.isChecked)
        }.apply()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun loadData() {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedClientName = sharedPreferences.getString("CLIENT", "")
        val savedSiteName = sharedPreferences.getString("SITE", "")
        val savedDate = sharedPreferences.getString("DATE", "")
        val savedBeginHour = sharedPreferences.getString("BEGIN", "")
        val savedEndHour = sharedPreferences.getString("END", "")
        val savedDuration = sharedPreferences.getString("DURATION", "")
        val savedRef = sharedPreferences.getString("REF", "")
        val savedObject = sharedPreferences.getString("OBJECT", "")
        val savedDetail = sharedPreferences.getString("DETAIL", "")
        val savedTech = sharedPreferences.getString("TECH", "")
        val savedCheckBoxInter1 = sharedPreferences.getBoolean("SITE1", false)
        val savedCheckBoxInter2 = sharedPreferences.getBoolean("PRISE", false)
        val savedCheckBoxInter3 = sharedPreferences.getBoolean("ATELIER", false)
        val savedCheckBoxMaint1 = sharedPreferences.getBoolean("PC", false)
        val savedCheckBoxMaint2 = sharedPreferences.getBoolean("SERVER", false)
        val savedCheckBoxMaint3 = sharedPreferences.getBoolean("NETWORK", false)
        val savedCheckBoxMaint4 = sharedPreferences.getBoolean("PHONE", false)
        val savedCheckBoxStatus1 = sharedPreferences.getBoolean("DONE", false)
        val savedCheckBoxStatus2 = sharedPreferences.getBoolean("INPROGRESS", false)
        val savedCheckBoxStatus3 = sharedPreferences.getBoolean("DEVIS", false)
        val savedCheckBoxType1 = sharedPreferences.getBoolean("MAINTENANCE", false)
        val savedCheckBoxType2 = sharedPreferences.getBoolean("FACTURABLE", false)

        // Load empty pdf document
        val src = "/data/data/com.example.etelcom/fiche_intervention_modif.pdf"
        var extStorageDirectory = requireActivity().getExternalFilesDir(null).toString()
        val dest = "$extStorageDirectory/Fiches/$savedRef" + "_$savedClientName.pdf"

        val pdfDoc = PdfDocument(PdfReader(src), PdfWriter(dest))
        val form: PdfAcroForm = PdfAcroForm.getAcroForm(pdfDoc, true)
        form.getField("client").setValue("$savedClientName")
        form.getField("site").setValue("$savedSiteName")
        pdfDoc.close()
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