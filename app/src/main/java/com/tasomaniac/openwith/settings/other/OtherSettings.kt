package com.tasomaniac.openwith.settings.other

import com.tasomaniac.openwith.BuildConfig
import com.tasomaniac.openwith.R
import com.tasomaniac.openwith.settings.Settings
import com.tasomaniac.openwith.settings.SettingsFragment
import javax.inject.Inject

class OtherSettings @Inject constructor(
    fragment: SettingsFragment
) : Settings(fragment) {

    override fun setup() {
        addPreferencesFromResource(R.xml.pref_others)

        findPreference(R.string.pref_key_open_source).setOnPreferenceClickListener {
            displayLicensesDialogFragment()
            true
        }

        findPreference(R.string.pref_key_version).apply {
            summary = BuildConfig.VERSION_NAME
        }
    }

    private fun displayLicensesDialogFragment() {
        LicensesDialogFragment.newInstance()
            .show(activity.supportFragmentManager, "LicensesDialog")
    }
}
