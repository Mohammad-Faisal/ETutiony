package candor.example.com.etutiony;

import android.app.Application;

import timber.log.Timber;

public class ETutiony extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
