package com.tasomaniac.openwith.settings

import android.app.backup.BackupManager
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tasomaniac.openwith.R
import com.tasomaniac.openwith.data.Analytics
import com.tasomaniac.openwith.databinding.ActivitySettingsBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SettingsActivity :
    DaggerAppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    @Inject lateinit var analytics: Analytics
    @Inject lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySettingsBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fragment_container, SettingsFragment())
            }

            analytics.sendScreenView("Settings")
        }
    }

    override fun setTitle(titleId: Int) {
        super.setTitle(titleId)
        binding.toolbarLayout.setTitle(getString(titleId))
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        supportFragmentManager.commit {
            setCustomAnimations(
                androidx.fragment.R.anim.sesl_fragment_open_enter,
                androidx.fragment.R.anim.sesl_fragment_open_exit,
                androidx.fragment.R.anim.sesl_fragment_close_enter,
                androidx.fragment.R.anim.sesl_fragment_close_exit
            )
            val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, pref.fragment ?: return false)
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
        }
        return true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        BackupManager(this).dataChanged()
    }
}
