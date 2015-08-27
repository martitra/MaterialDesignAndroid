package materialtest.vivz.slidenerd.materialtest.json;

import materialtest.vivz.slidenerd.materialtest.materialtest.MyApplication;

import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_BOX_OFFICE;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_CHAR_AMPERSAND;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_CHAR_QUESTION;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_PARAM_API_KEY;
import static materialtest.vivz.slidenerd.materialtest.extras.UrlEndpoints.URL_PARAM_LIMIT;

/**
 * Creado por soft12 el 27/08/2015.
 */
public class Endpoints {

    public static String getRequestUrl(int limit) {

        return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMPERSAND
                + URL_PARAM_LIMIT + limit;
    }
}
