package materialtest.vivz.slidenerd.materialtest.json;

import org.json.JSONObject;

/**
 * Creado por soft12 el 27/08/2015.
 */
public class Utils {

    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) & !jsonObject.isNull(key);
    }

}
