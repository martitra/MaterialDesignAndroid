package materialtest.vivz.slidenerd.materialtest.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.adapters.AdapterBoxOffice;
import materialtest.vivz.slidenerd.materialtest.extras.Constants;
import materialtest.vivz.slidenerd.materialtest.logging.L;
import materialtest.vivz.slidenerd.materialtest.materialtest.MyApplication;
import materialtest.vivz.slidenerd.materialtest.network.VolleySingleton;
import materialtest.vivz.slidenerd.materialtest.pojo.Movie;

import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_AUDIENCE_SCORE;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_ID;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_MOVIES;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_POSTERS;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_RATINGS;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_RELEASE_DATES;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_SYNOPSIS;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_THEATER;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_THUMBNAIL;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_TITLE;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_BOX_OFFICE;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_CHAR_AMPERSAND;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_CHAR_QUESTION;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_PARAM_API_KEY;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_PARAM_LIMIT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBoxOffice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBoxOffice extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private AdapterBoxOffice adapterBoxOffice;
    private RecyclerView listMovieHits;
    private TextView textVolleyError;


    public FragmentBoxOffice() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBoxOffice.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBoxOffice newInstance(String param1, String param2) {
        FragmentBoxOffice fragment = new FragmentBoxOffice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static String getRequestUrl(int limit) {
        return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMPERSAND
                + URL_PARAM_LIMIT + limit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
        senJsonRequest();

    }

    private void senJsonRequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getRequestUrl(10),
                "",
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //L.t(getActivity(),response.toString());
                        textVolleyError.setVisibility(View.GONE);
                        listMovies = parseJSONResponse(response);
                        adapterBoxOffice.setMovieList(listMovies);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        });
        requestQueue.add(request);
    }

    private void handleVolleyError(VolleyError error) {
        textVolleyError.setVisibility(View.VISIBLE);

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            textVolleyError.setText(R.string.error_timeout);
        } else if (error instanceof AuthFailureError) {
            textVolleyError.setText(R.string.error_auth_failure);
        } else if (error instanceof ServerError) {
            textVolleyError.setText(R.string.error_auth_failure);
        } else if (error instanceof NetworkError) {
            textVolleyError.setText(R.string.error_network);
        } else if (error instanceof ParseError) {
            textVolleyError.setText(R.string.error_parser);
        }
    }

    private ArrayList<Movie> parseJSONResponse(JSONObject response) {

        ArrayList<Movie> listMovies = new ArrayList<>();
        if (response != null || response.length() > 0) {
            try {
                //StringBuilder data = new StringBuilder();
                if (response.has(KEY_MOVIES)) {
                    JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);
                    for (int i = 0; i < arrayMovies.length(); i++) {

                        long id = 0;
                        String title = Constants.NA;
                        String releaseDate = Constants.NA;
                        int audiendceScore = -1;
                        String synopsis = Constants.NA;
                        String urlThumbnail = Constants.NA;

                        JSONObject currentMovie = arrayMovies.getJSONObject(i);

                        if (currentMovie.has(KEY_ID) && !currentMovie.isNull(KEY_ID)) {
                            //get the id from the current movie
                            id = currentMovie.getLong(KEY_ID);
                        }
                        if (currentMovie.has(KEY_TITLE) && !currentMovie.isNull(KEY_TITLE)) {
                            //get the title from the current movie
                            title = currentMovie.getString(KEY_TITLE);
                        }
                        if (currentMovie.has(KEY_RELEASE_DATES) && !currentMovie.isNull(KEY_RELEASE_DATES)) {
                            //get the date in theaters from the current movie
                            JSONObject objectReleaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);
                            if (objectReleaseDates != null && objectReleaseDates.has(KEY_THEATER)
                                    && !objectReleaseDates.isNull(KEY_THEATER)) {
                                releaseDate = objectReleaseDates.getString(KEY_THEATER);
                            }
                        }
                        if (currentMovie.has(KEY_RATINGS) && !currentMovie.isNull(KEY_RATINGS)) {
                            //get the audience score from the current movie
                            JSONObject objectRatings = currentMovie.getJSONObject(KEY_RATINGS);
                            if (objectRatings != null && objectRatings.has(KEY_AUDIENCE_SCORE)
                                    && !objectRatings.isNull(KEY_AUDIENCE_SCORE)) {
                                audiendceScore = objectRatings.getInt(KEY_AUDIENCE_SCORE);
                            }
                        }
                        if (currentMovie.has(KEY_SYNOPSIS) && !currentMovie.isNull(KEY_SYNOPSIS)) {
                            //get the synopsis from the current movie
                            synopsis = currentMovie.getString(KEY_SYNOPSIS);
                        }
                        if (currentMovie.has(KEY_POSTERS) && !currentMovie.isNull(KEY_POSTERS)) {
                            //get the thumbnail from the current movie
                            JSONObject objectPosters = currentMovie.getJSONObject(KEY_POSTERS);
                            if (objectPosters != null && objectPosters.has(KEY_THUMBNAIL)
                                    && !objectPosters.isNull(KEY_THUMBNAIL)) {
                                urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                            }
                        }

                        //data.append(id + " " + title + " " + releaseDate + " " + audiendceScore + "\n");
                        Movie movie = new Movie();
                        movie.setId(id);
                        movie.setTitle(title);
                        Date date = null;
                        try {
                            date = dateFormat.parse(releaseDate);
                        } catch (ParseException ignored) {
                        }
                        movie.setReleaseDateTheater(date);
                        movie.setAudienceScore(audiendceScore);
                        movie.setSynopsis(synopsis);
                        movie.setUrlThumbnail(urlThumbnail);

                        if (id != -1 && !title.equals(Constants.NA)) {
                            listMovies.add(movie);
                        }

                    }
                    L.t(getActivity(), listMovies.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listMovies;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_back_office, container, false);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        listMovieHits = (RecyclerView) view.findViewById(R.id.listMovieHits);
        listMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        listMovieHits.setAdapter(adapterBoxOffice);
        senJsonRequest();
        return view;
    }

}
