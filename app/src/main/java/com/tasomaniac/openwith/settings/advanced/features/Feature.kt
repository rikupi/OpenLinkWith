package com.tasomaniac.openwith.settings.advanced.features

import androidx.annotation.StringRes
import com.tasomaniac.openwith.translations.R.string

enum class Feature(
    @StringRes val titleRes: Int,
    @StringRes val detailsRes: Int,
    val className: String? = null,
    val prefKey: String,
    val defaultValue: Boolean = true
) {

    TEXT_SELECTION(
        string.pref_title_feature_text_selection,
        string.pref_details_feature_text_selection,
        "com.tasomaniac.openwith.TextSelectionActivity",
        "pref_feature_text_selection"
    ),
    DIRECT_SHARE(
        string.pref_title_feature_direct_share,
        string.pref_details_feature_direct_share,
        "androidx.sharetarget.ChooserTargetServiceCompat",
        "pref_feature_direct_share"
    ),
    BROWSER(
        string.pref_title_feature_browser,
        string.pref_details_feature_browser,
        className = "com.tasomaniac.openwith.BrowserActivity",
        prefKey = "pref_feature_browser",
        defaultValue = false
    )
}

fun String.toFeature() = Feature.values().find { it.prefKey == this }!!
