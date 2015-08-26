package materialtest.vivz.slidenerd.materialtest.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
                        listMovies = parseJSONResponse(response);
                        adapterBoxOffice.setMovieList(listMovies);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private ArrayList<Movie> parseJSONResponse(JSONObject response) {

        ArrayList<Movie> listMovies = new ArrayList<>();
        if (response != null || response.length() > 0) {
            try {
                StringBuilder data = new StringBuilder();
                assert response != null;
                if (response.has(KEY_MOVIES)) {
                    JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);
                    for (int i = 0; i < arrayMovies.length(); i++) {
                        JSONObject currentMovie = arrayMovies.getJSONObject(i);
                        //get the id from the current movie
                        long id = currentMovie.getLong(KEY_ID);
                        //get the title from the current movie
                        String title = currentMovie.getString(KEY_TITLE);
                        //get the date in theaters from the current movie
                        JSONObject objectReleaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);
                        String releaseDate = null;
                        if (objectReleaseDates.has(KEY_THEATER)) {
                            releaseDate = objectReleaseDates.getString(KEY_THEATER);
                        } else {
                            releaseDate = "NA";
                        }
                        //get the audience score from the current movie
                        JSONObject objectRatings = currentMovie.getJSONObject(KEY_RATINGS);
                        int audiendceScore = -1;
                        if (objectRatings.has(KEY_AUDIENCE_SCORE)) {
                            audiendceScore = objectRatings.getInt(KEY_AUDIENCE_SCORE);
                        }

                        //get the synopsis from the current movie
                        String synopsis = currentMovie.getString(KEY_SYNOPSIS);

                        //get the thumbnail from the current movie
                        JSONObject objectPosters = currentMovie.getJSONObject(KEY_POSTERS);
                        String urlThumbnail = null;
                        if (objectPosters.has(KEY_THUMBNAIL)) {
                            urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                        }

                        //data.append(id + " " + title + " " + releaseDate + " " + audiendceScore + "\n");
                        Movie movie = new Movie();
                        movie.setId(id);
                        movie.setTitle(title);
                        Date date = dateFormat.parse(releaseDate);
                        movie.setReleaseDateTheater(date);
                        movie.setAudienceScore(audiendceScore);
                        movie.setSynopsis(synopsis);
                        movie.setUrlThumbnail(urlThumbnail);

                        listMovies.add(movie);

                    }
                    L.t(getActivity(), listMovies.toString());
                }

            } catch (JSONException | ParseException e) {
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
        listMovieHits = (RecyclerView) view.findViewById(R.id.listMovieHits);
        listMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        listMovieHits.setAdapter(adapterBoxOffice);
        senJsonRequest();
        return view;
    }

}
