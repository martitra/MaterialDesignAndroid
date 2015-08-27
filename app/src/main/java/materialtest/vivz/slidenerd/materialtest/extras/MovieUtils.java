package materialtest.vivz.slidenerd.materialtest.extras;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

import materialtest.vivz.slidenerd.materialtest.json.Endpoints;
import materialtest.vivz.slidenerd.materialtest.json.Parser;
import materialtest.vivz.slidenerd.materialtest.json.Requestor;
import materialtest.vivz.slidenerd.materialtest.materialtest.MyApplication;
import materialtest.vivz.slidenerd.materialtest.pojo.Movie;

/**
 * Creado por soft12 el 27/08/2015.
 */
public class MovieUtils {

    public static ArrayList<Movie> loadBoxOfficeMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.sendRequestBoxOfficeMovies(requestQueue, Endpoints.getRequestUrl(30));
        ArrayList<Movie> listMovies = Parser.parseJSONResponse(response);
        MyApplication.getWritableDatabase().insertMoviesBoxOffice(listMovies, true);
        return listMovies;
    }
}
