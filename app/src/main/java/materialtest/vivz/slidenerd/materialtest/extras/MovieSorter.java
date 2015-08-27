package materialtest.vivz.slidenerd.materialtest.extras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import materialtest.vivz.slidenerd.materialtest.pojo.Movie;

/**
 * Creado por soft12 el 26/08/2015.
 */
public class MovieSorter {

    public void sortMoviesByName(ArrayList<Movie> movies) {

        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });

    }

    public void sortMoviesByDate(ArrayList<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getReleaseDateTheater().compareTo(rhs.getReleaseDateTheater());
            }
        });
    }

    public void sortMoviesByRating(ArrayList<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                int ratingLhs = lhs.getAudienceScore();
                int ratingRhs = rhs.getAudienceScore();
                if (ratingLhs < ratingRhs) {
                    return 1;
                } else if (ratingLhs > ratingRhs) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });
    }

}
