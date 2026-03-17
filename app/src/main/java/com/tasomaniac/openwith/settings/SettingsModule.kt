package com.tasomaniac.openwith.settings

import com.tasomaniac.openwith.settings.advanced.DisableFeaturesSettings
import com.tasomaniac.openwith.settings.other.OtherSettings
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet

@Module
class SettingsModule {

    @Provides
    @ElementsIntoSet
    fun settings(
        general: GeneralSettings,
        display: DisplaySettings,
        other: OtherSettings
    ): Set<Settings> = setOf(general, display, other)

    @Provides
    @ElementsIntoSet
    fun disableFeaturesSettings(settings: DisableFeaturesSettings): Set<Settings> =
        setOf(settings)
}
