package com.etelcom.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.appbar.AppBarLayout
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
            val resources: Resources = this.resources
            val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            val lengthNavBar = if (resourceId > 0) {
                resources.getDimensionPixelSize(resourceId)
            } else 0
            val lengthButton: Int = validateBtn.height
            val lengthBottom: Int = lengthButton + lengthNavBar + 10
            val b: Bitmap = getBitmapFromView(it.rootView, lengthBottom)

            val bun = intent.extras
            var who: String? = ""
            if (bun != null) {
                who = bun.getString("who")
            }
            val extStorageDirectory = this.getExternalFilesDir(null).toString()
            val len = extStorageDirectory.length - 34
            val myDir = extStorageDirectory.substring(0, len)
            val dir = File("$myDir/Documents/Signatures_Etelcom/")
            val path = "$dir/$who.png"

            fos = FileOutputStream(path)
            b.compress(Bitmap.CompressFormat.PNG, 95, fos)

            Toast.makeText(this, "Signature enregistrée", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getBitmapFromView(view: View, length: Int): Bitmap {
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var bottomBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height - length)
        var canvas = Canvas(bottomBitmap)
        view.draw(canvas)
        return bottomBitmap
    }
}