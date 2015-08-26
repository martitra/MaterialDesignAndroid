package materialtest.vivz.slidenerd.materialtest.logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Creado por soft12 el 25/08/2015.
 */
public class L {

    public static void m(String message) {
        Log.d("VIVZ", "" + message);
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }
}
