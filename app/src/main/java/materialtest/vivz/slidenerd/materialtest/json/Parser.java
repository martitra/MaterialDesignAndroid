package materialtest.vivz.slidenerd.materialtest.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import materialtest.vivz.slidenerd.materialtest.extras.Constants;
import materialtest.vivz.slidenerd.materialtest.pojo.Movie;

import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_AUDIENCE_SCORE;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_CAST;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_ID;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_LINKS;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_MOVIES;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_POSTERS;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_RATINGS;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_RELEASE_DATES;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_REVIEWS;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_SELF;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_SIMILAR;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_SYNOPSIS;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_THEATER;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_THUMBNAIL;
import static materialtest.vivz.slidenerd.materialtest.extras.Keys.EndpointBoxOffice.KEY_TITLE;

/**
 * Creado por soft12 el 27/08/2015.
 */
public class Parser {

    public static ArrayList<Movie> parseJSONResponse(JSONObject response) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Movie> listMovies = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);
                for (int i = 0; i < arrayMovies.length(); i++) {
                    long id = 0;
                    String title = Constants.NA;
                    String releaseDate = Constants.NA;
                    int audiendceScore = -1;
                    String synopsis = Constants.NA;
                    String urlThumbnail = Constants.NA;
                    String urlSelf = Constants.NA;
                    String urlCast = Constants.NA;
                    String urlReviews = Constants.NA;
                    String urlSimilar = Constants.NA;

                    JSONObject currentMovie = arrayMovies.getJSONObject(i);

                    if (Utils.contains(currentMovie, KEY_ID)) {
                        //get the id from the current movie
                        id = currentMovie.getLong(KEY_ID);
                    }
                    if (Utils.contains(currentMovie, KEY_TITLE)) {
                        //get the title from the current movie
                        title = currentMovie.getString(KEY_TITLE);
                    }
                    if (Utils.contains(currentMovie, KEY_RELEASE_DATES)) {
                        //get the date in theaters from the current movie
                        JSONObject objectReleaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);
                        if (Utils.contains(objectReleaseDates, KEY_THEATER)) {
                            releaseDate = objectReleaseDates.getString(KEY_THEATER);
                        }
                    }
                    if (Utils.contains(currentMovie, KEY_RATINGS)) {
                        //get the audience score from the current movie
                        JSONObject objectRatings = currentMovie.getJSONObject(KEY_RATINGS);
                        if (Utils.contains(objectRatings, KEY_AUDIENCE_SCORE)) {
                            audiendceScore = objectRatings.getInt(KEY_AUDIENCE_SCORE);
                        }
                    }
                    if (Utils.contains(currentMovie, KEY_SYNOPSIS)) {
                        //get the synopsis from the current movie
                        synopsis = currentMovie.getString(KEY_SYNOPSIS);
                    }
                    if (Utils.contains(currentMovie, KEY_POSTERS)) {
                        //get the thumbnail from the current movie
                        JSONObject objectPosters = currentMovie.getJSONObject(KEY_POSTERS);
                        if (Utils.contains(objectPosters, KEY_THUMBNAIL)) {
                            urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                        }
                    }

                    if (Utils.contains(currentMovie, KEY_LINKS)) {
                        JSONObject objectsLinks = currentMovie.getJSONObject(KEY_LINKS);
                        if (Utils.contains(objectsLinks, KEY_SELF)) {
                            urlSelf = objectsLinks.getString(KEY_SELF);
                        }
                        if (Utils.contains(objectsLinks, KEY_CAST)) {
                            urlCast = objectsLinks.getString(KEY_CAST);
                        }
                        if (Utils.contains(objectsLinks, KEY_REVIEWS)) {
                            urlReviews = objectsLinks.getString(KEY_REVIEWS);
                        }
                        if (Utils.contains(objectsLinks, KEY_SIMILAR)) {
                            urlSimilar = objectsLinks.getString(KEY_SIMILAR);
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
                    movie.setUrlSelf(urlSelf);
                    movie.setUrlCast(urlCast);
                    movie.setUrlReviews(urlReviews);
                    movie.setUrlSimilar(urlSimilar);

                    if (id != -1 && !title.equals(Constants.NA)) {
                        listMovies.add(movie);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listMovies;
    }

}
