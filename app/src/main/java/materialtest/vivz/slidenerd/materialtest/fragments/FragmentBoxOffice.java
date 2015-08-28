package materialtest.vivz.slidenerd.materialtest.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.adapters.AdapterBoxOffice;
import materialtest.vivz.slidenerd.materialtest.callbacks.BoxOfficeMoviesLoadedListener;
import materialtest.vivz.slidenerd.materialtest.extras.MovieSorter;
import materialtest.vivz.slidenerd.materialtest.extras.SortListener;
import materialtest.vivz.slidenerd.materialtest.logging.L;
import materialtest.vivz.slidenerd.materialtest.materialtest.MyApplication;
import materialtest.vivz.slidenerd.materialtest.pojo.Movie;
import materialtest.vivz.slidenerd.materialtest.task.TaskLoadMoviesBoxOffice;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBoxOffice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBoxOffice extends Fragment implements SortListener,
        BoxOfficeMoviesLoadedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String STATE_MOVIES = "state_movies";
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private AdapterBoxOffice adapterBoxOffice;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerMovies;
    private TextView textVolleyError;
    private MovieSorter movieSorter;

    public FragmentBoxOffice() {
        // Required empty public constructor
        movieSorter = new MovieSorter();
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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, listMovies);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_office, container, false);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeMoviesHits);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerMovies = (RecyclerView) view.findViewById(R.id.listMovieHits);
        mRecyclerMovies.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        mRecyclerMovies.setAdapter(adapterBoxOffice);

        if (savedInstanceState != null) {
            listMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIES);

        } else {
            //senJsonRequest();
            listMovies = MyApplication.getWritableDatabase().getAllMoviesBoxOffice();
            if (listMovies.isEmpty()) {
                L.t(getActivity(), "executing task from fragment");
                new TaskLoadMoviesBoxOffice(this).execute();
            }
        }

        adapterBoxOffice.setMovieList(listMovies);

        return view;
    }

    @Override
    public void onSortByName() {
        L.t(getActivity(), "sort name BoxOffice");
        movieSorter.sortMoviesByName(listMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onSortByDate() {
        movieSorter.sortMoviesByDate(listMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onSortByRating() {
        movieSorter.sortMoviesByRating(listMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies) {
        L.t(getActivity(), "onBoxOfficeMoviesLoaded Fragment");
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        adapterBoxOffice.setMovieList(listMovies);
    }

    @Override
    public void onRefresh() {
        L.t(getActivity(), "onRefresh");
        new TaskLoadMoviesBoxOffice(this).execute();
    }
}
