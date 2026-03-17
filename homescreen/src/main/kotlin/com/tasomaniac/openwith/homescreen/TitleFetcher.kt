package com.tasomaniac.openwith.homescreen

import com.tasomaniac.openwith.PerActivity
import javax.inject.Inject

@PerActivity
class TitleFetcher @Inject constructor() {

    fun cancel() {}

    /** Network access removed. Title auto-fetch is disabled. */
    fun fetch(url: String, onSuccess: (title: String?) -> Unit, onFailure: () -> Unit) {
        onFailure()
    }
}
