package nightroomcreation.id.realmdemo.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by iand on 26/09/17.
 */

public class GlobalFunction {
    /**
     * shared preference here
     * ======================
     */
    private static SharedPreferences sp;
    private static GlobalFunction instance;

    public GlobalFunction(Context context) {
        sp = context.getSharedPreferences(GlobalConstant.NAME_PREF, Context.MODE_PRIVATE);
    }

    public static GlobalFunction prefWith(Context context) {
        if (instance == null) {
            instance = new GlobalFunction(context);
        }
        return instance;
    }

    public void setPreloadPref(boolean totalTime) {
        sp.edit()
                .putBoolean(GlobalConstant.PRELOAD_PREF, totalTime)
                .apply();
    }

    public boolean getPreloadPref() {
        return sp.getBoolean(GlobalConstant.PRELOAD_PREF, false);
    }
}
