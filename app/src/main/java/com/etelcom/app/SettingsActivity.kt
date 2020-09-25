package com.etelcom.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.etelcom.app.ui.settings.SettingsFragment

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportFragmentManager.findFragmentById(android.R.id.content) == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment()).commit()
        }
    }
}