package com.tasomaniac.openwith;

import android.app.ActivityManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.core.content.ContextCompat;
import com.tasomaniac.openwith.resolver.IconLoader;
import com.tasomaniac.openwith.rx.SchedulingStrategy;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Singleton;

@Module
abstract class AppModule {

    @Binds
    abstract Application application(App app);

    @Provides
    static PackageManager packageManager(Application app) {
        return app.getPackageManager();
    }

    @Provides
    static SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @SuppressWarnings("ConstantConditions")
    @Provides
    static ActivityManager provideActivityManager(Application app) {
        return ContextCompat.getSystemService(app, ActivityManager.class);
    }

    @Provides
    static Resources resources(Application app) {
        return app.getResources();
    }

    @Provides
    static SchedulingStrategy schedulingStrategy() {
        return new SchedulingStrategy(Schedulers.io(), AndroidSchedulers.from(Looper.getMainLooper(), true));
    }

    @Provides
    static IconLoader provideIconLoader(PackageManager pm, ActivityManager am) {
        int iconDpi = am.getLauncherLargeIconDensity();
        return new IconLoader(pm, iconDpi);
    }

}
