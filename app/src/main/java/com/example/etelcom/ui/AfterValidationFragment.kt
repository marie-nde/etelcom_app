package com.example.etelcom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.etelcom.R
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class AfterValidationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_after_validation, container, false)
        val pdfName = "Fiche_etelcom.pdf"
        val btn: Button = root.findViewById(R.id.button)
        val document: FileInputStream = FileInputStream("$pdfName")
        btn.setOnClickListener {
            PDDocument.load(document);
        }
        return root
    }
}