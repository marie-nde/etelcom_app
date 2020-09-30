package com.etelcom.app

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream


class Scribbler : Activity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signature)

        val validateBtn: Button = findViewById(R.id.validateSignBtn)
        validateBtn.setOnClickListener {
            var fos: FileOutputStream? = null
            val b: Bitmap = getBitmapFromView(it.rootView)

            val bun = intent.extras
            var ref: String? = ""
            var who: String? = ""
            if (bun != null) {
                ref = bun.getString("ref")
                who = bun.getString("who")
            }
            val extStorageDirectory = this.getExternalFilesDir(null).toString()
            val len = extStorageDirectory.length - 34
            val myDir = extStorageDirectory.substring(0, len)
            val dir = File("$myDir/Documents/Signatures_Etelcom/")
            val path = "$dir/$ref" + "_$who.png"

            fos = FileOutputStream(path)
            b.compress(Bitmap.CompressFormat.PNG, 95, fos)

            Toast.makeText(this, "Signature enregistrée", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return Bitmap.createBitmap(bitmap, 0, 350, bitmap.width, bitmap.height - 350)
    }
}