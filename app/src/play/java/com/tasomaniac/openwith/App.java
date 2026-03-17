package com.tasomaniac.openwith;

import com.tasomaniac.openwith.settings.NightModePreferences;

import javax.inject.Inject;

import dagger.android.support.DaggerApplication;
import timber.log.Timber;

public class App extends DaggerApplication {

    private AppComponent component;

    @Inject NightModePreferences nightModePreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        nightModePreferences.updateDefaultNightMode();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    @Override
    protected AppComponent applicationInjector() {
        component = DaggerAppComponent.factory().create(this);
        return component;
    }

    public AppComponent component() {
        return component;
    }
}
