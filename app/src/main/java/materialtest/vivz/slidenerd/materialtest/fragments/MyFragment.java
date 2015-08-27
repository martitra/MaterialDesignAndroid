package materialtest.vivz.slidenerd.materialtest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.network.VolleySingleton;

/**
 * Creado por soft12 el 12/08/2015.
 */
public class MyFragment extends Fragment {
    private TextView textView;

    public static MyFragment getInstance(int position) {
        MyFragment myFragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my, container, false);
        textView = (TextView) layout.findViewById(R.id.position);
        Bundle bundle = getArguments();
        if (bundle != null) {
            textView.setText("The Page Number is " + bundle.getInt("position"));
        }
        /* ESTO NO VE VA PORQUE NO TENGO INTERNET EN EL ADB */

        /*
        * This Android Tutorial shows an example of android volley library and rotten tomatoes API.
        * Construct an object of RequestQueue using Volley.newRequestQueue() and add a StringRequest
        * to it with Response.Listener and Error.Listener callbacks. If the request is successful
        * the onResponse method is called in the main thread with the requested data, else onError
        * is invoked with the appropriate error message. Register at rottentomatoes.com to access
        * their JSON API which will be used in our app
        *
        * */
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, "http://php.net/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "RESPONSE ", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "ERROR ", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
        return layout;
    }
}