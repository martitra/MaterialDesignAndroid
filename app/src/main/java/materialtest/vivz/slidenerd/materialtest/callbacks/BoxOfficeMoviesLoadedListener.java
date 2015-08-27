package materialtest.vivz.slidenerd.materialtest.callbacks;

import java.util.ArrayList;

import materialtest.vivz.slidenerd.materialtest.pojo.Movie;

/**
 * Creado por soft12 el 27/08/2015.
 */
public interface BoxOfficeMoviesLoadedListener {
    void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies);
}
