package com.tasomaniac.openwith.util

import dagger.Provides

sealed class CallerPackageExtractor {

    abstract fun extract(): String?

    @dagger.Module
    class Module {

        @Provides
        fun callerPackageExtractor(): CallerPackageExtractor = EmptyExtractor
    }
}

private object EmptyExtractor : CallerPackageExtractor() {
    override fun extract(): String? = null
}
