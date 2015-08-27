package materialtest.vivz.slidenerd.materialtest.services;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import materialtest.vivz.slidenerd.materialtest.extras.Constants;
import materialtest.vivz.slidenerd.materialtest.logging.L;
import materialtest.vivz.slidenerd.materialtest.materialtest.MyApplication;
import materialtest.vivz.slidenerd.materialtest.network.VolleySingleton;
import materialtest.vivz.slidenerd.materialtest.pojo.Movie;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

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
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_BOX_OFFICE;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_CHAR_AMPERSAND;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_CHAR_QUESTION;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_PARAM_API_KEY;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_PARAM_LIMIT;

/**
 * Creado por soft12 el 27/08/2015.
 */
public class MyService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        L.t(this, "onStartJob");
        //para < lolipop
        //jobFinished(jobParameters, false);
        new MyTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        L.t(this, "onStopJob");
        return false;
    }

    private static class MyTask extends AsyncTask<JobParameters, Void, JobParameters> {

        MyService myService;
        private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        private VolleySingleton volleySingleton;
        private RequestQueue requestQueue;

        public MyTask(MyService myService) {
            this.myService = myService;
            volleySingleton = VolleySingleton.getsInstance();
            requestQueue = volleySingleton.getRequestQueue();
        }

        public static String getRequestUrl(int limit) {
            return URL_BOX_OFFICE
                    + URL_CHAR_QUESTION
                    + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                    + URL_CHAR_AMPERSAND
                    + URL_PARAM_LIMIT + limit;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            JSONObject response = senJsonRequest();
            ArrayList<Movie> listMovies = parseJSONResponse(response);
            MyApplication.getWritableDatabase().insertMoviesBoxOffice(listMovies, true);
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            myService.jobFinished(jobParameters, false);
        }

        private JSONObject senJsonRequest() {

            JSONObject response = null;
            RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    getRequestUrl(30), "",
                    requestFuture, requestFuture);
            requestQueue.add(request);
            try {
                response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                L.m(e + "");
            }
            return response;
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
                            String urlSelf = Constants.NA;
                            String urlCast = Constants.NA;
                            String urlReviews = Constants.NA;
                            String urlSimilar = Constants.NA;

                            JSONObject currentMovie = arrayMovies.getJSONObject(i);

                            if (contains(currentMovie, KEY_ID)) {
                                //get the id from the current movie
                                id = currentMovie.getLong(KEY_ID);
                            }
                            if (contains(currentMovie, KEY_TITLE)) {
                                //get the title from the current movie
                                title = currentMovie.getString(KEY_TITLE);
                            }
                            if (contains(currentMovie, KEY_RELEASE_DATES)) {
                                //get the date in theaters from the current movie
                                JSONObject objectReleaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);
                                if (contains(objectReleaseDates, KEY_THEATER)) {
                                    releaseDate = objectReleaseDates.getString(KEY_THEATER);
                                }
                            }
                            if (contains(currentMovie, KEY_RATINGS)) {
                                //get the audience score from the current movie
                                JSONObject objectRatings = currentMovie.getJSONObject(KEY_RATINGS);
                                if (contains(objectRatings, KEY_AUDIENCE_SCORE)) {
                                    audiendceScore = objectRatings.getInt(KEY_AUDIENCE_SCORE);
                                }
                            }
                            if (contains(currentMovie, KEY_SYNOPSIS)) {
                                //get the synopsis from the current movie
                                synopsis = currentMovie.getString(KEY_SYNOPSIS);
                            }
                            if (contains(currentMovie, KEY_POSTERS)) {
                                //get the thumbnail from the current movie
                                JSONObject objectPosters = currentMovie.getJSONObject(KEY_POSTERS);
                                if (contains(objectPosters, KEY_THUMBNAIL)) {
                                    urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                                }
                            }

                            if (contains(currentMovie, KEY_LINKS)) {
                                JSONObject objectsLinks = currentMovie.getJSONObject(KEY_LINKS);
                                if (contains(objectsLinks, KEY_SELF)) {
                                    urlSelf = objectsLinks.getString(KEY_SELF);
                                }
                                if (contains(objectsLinks, KEY_CAST)) {
                                    urlCast = objectsLinks.getString(KEY_CAST);
                                }
                                if (contains(objectsLinks, KEY_REVIEWS)) {
                                    urlReviews = objectsLinks.getString(KEY_REVIEWS);
                                }
                                if (contains(objectsLinks, KEY_SIMILAR)) {
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
                        //L.t(getActivity(), listMovies.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return listMovies;
        }

        private boolean contains(JSONObject jsonObject, String key) {
            return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key);
        }

    }
}
