package materialtest.vivz.slidenerd.materialtest.materialtest;

import android.app.Application;
import android.content.Context;

/**
 * Creado por soft12 el 13/08/2015.
 */
public class MyApplication extends Application {

    /*
    * This Android Volley Tutorial video talks about how to setup the Android volley singleton
    * class that contains an instance variable of RequestQueue and ImageLoader which can be used
    * throughout the app. Create a custom Application object and invoke getApplicationContext() to
    * get access to the context needed by Volley library. Pass this context to the
    * Volley.newRequestQueue method , Construct an object of LruCache to maintain a cache for the
    * ImageLoader, use the Runtime.getRuntime() and its method getMaxMemory() to determine
    * the size of the cache.
    */

    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}