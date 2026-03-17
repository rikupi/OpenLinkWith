package com.tasomaniac.openwith.redirect

import com.tasomaniac.openwith.rx.SchedulingStrategy
import io.reactivex.Single
import javax.inject.Inject

class RedirectFixer @Inject constructor(
    private val scheduling: SchedulingStrategy
) {
    /** Network access removed. Returns the URL as-is. */
    fun followRedirects(url: String): Single<String> =
        Single.just(url).compose(scheduling.forSingle())
}
