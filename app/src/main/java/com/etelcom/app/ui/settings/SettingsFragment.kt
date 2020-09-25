package com.etelcom.app.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.etelcom.app.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        addPreferencesFromResource(R.xml.preferences_main)
    }
}