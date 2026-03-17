package com.tasomaniac.openwith.redirect

import com.tasomaniac.openwith.test.testScheduling
import org.junit.Test

class RedirectFixerTest {

    private val redirectFixer = RedirectFixer(testScheduling())

    @Test
    fun givenUrlShouldReturnSameUrl() {
        redirectFixer
            .followRedirects("https://example.com/path")
            .test()
            .assertValue("https://example.com/path")
            .assertNoErrors()
    }

    @Test
    fun givenEmptyUrlShouldReturnEmptyUrl() {
        redirectFixer
            .followRedirects("")
            .test()
            .assertValue("")
            .assertNoErrors()
    }
}
