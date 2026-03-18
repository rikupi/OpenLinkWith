package com.tasomaniac.openwith.settings

import android.content.SharedPreferences
import android.os.Build
import androidx.preference.SwitchPreferenceCompat
import com.tasomaniac.openwith.R
import com.tasomaniac.openwith.data.Analytics
import com.tasomaniac.openwith.translations.R as translationsR
import dev.oneuiproject.oneui.utils.supports3DTransitionFlag
import javax.inject.Inject

class DisplaySettings @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val nightModePreferences: NightModePreferences,
    private val analytics: Analytics,
    fragment: SettingsFragment
) : Settings(fragment),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun setup() {
        addPreferencesFromResource(R.xml.pref_display)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val selectedEntry = nightModePreferences.selectedEntry
        findPreference(R.string.pref_key_night_mode).setSummary(selectedEntry)

        setupAcrylicBlur()
    }

    private fun setupAcrylicBlur() {
        val pref = findPreference(R.string.pref_key_acrylic_blur) as SwitchPreferenceCompat
        val supported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM
                && supports3DTransitionFlag

        if (supported) {
            updateAcrylicSummary(pref, pref.isChecked)
            pref.setOnPreferenceChangeListener { _, newValue ->
                updateAcrylicSummary(pref, newValue as Boolean)
                true
            }
        } else {
            pref.isChecked = false
            pref.isEnabled = false
            pref.setSummary(translationsR.string.pref_summary_acrylic_blur_unavailable)
        }
    }

    private fun updateAcrylicSummary(pref: SwitchPreferenceCompat, enabled: Boolean) {
        pref.setSummary(
            if (enabled) translationsR.string.pref_summary_acrylic_blur_on
            else translationsR.string.pref_summary_acrylic_blur_off
        )
    }

    override fun release() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key != null && key.isKeyEquals(R.string.pref_key_night_mode)) {
            nightModePreferences.updateDefaultNightMode()
            activity.recreate()

            val selectedValue = nightModePreferences.mode.stringValue(context.resources)
            analytics.sendEvent("Preference", "Night Mode", selectedValue)
        }
    }
}
