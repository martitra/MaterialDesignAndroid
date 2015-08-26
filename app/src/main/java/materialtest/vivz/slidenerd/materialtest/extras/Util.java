package materialtest.vivz.slidenerd.materialtest.extras;

import android.os.Build;

/**
 * Creado por soft12 el 25/08/2015.
 */
public class Util {

    public static boolean isLollipopOrGreater() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean isJellyBeanOrGreater() {
        return Build.VERSION.SDK_INT >= 16;
    }

}
