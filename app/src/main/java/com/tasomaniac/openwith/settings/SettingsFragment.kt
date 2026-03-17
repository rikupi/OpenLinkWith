package com.tasomaniac.openwith.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.openwith.base.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject lateinit var settings: @JvmSuppressWildcards Set<Settings>

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateRecyclerView(
        inflater: LayoutInflater,
        parent: ViewGroup,
        savedInstanceState: Bundle?
    ): RecyclerView {
        val rv = super.onCreateRecyclerView(inflater, parent, savedInstanceState)
        rv.addItemDecoration(OneUiGroupDecoration(requireContext()))
        return rv
    }

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        settings.forEach { it.setup() }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.app_short_name)
        settings.forEach { it.resume() }
    }

    override fun onPause() {
        settings.forEach { it.pause() }
        super.onPause()
    }

    override fun onDestroy() {
        settings.forEach { it.release() }
        super.onDestroy()
    }
}
