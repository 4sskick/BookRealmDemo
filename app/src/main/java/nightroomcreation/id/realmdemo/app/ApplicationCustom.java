package nightroomcreation.id.realmdemo.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import nightroomcreation.id.realmdemo.BuildConfig;

/**
 * Created by iand on 25/09/17.
 */

public class ApplicationCustom extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
