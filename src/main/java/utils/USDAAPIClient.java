package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.micronaut.context.annotation.Value;

import java.io.IOException;
import java.net.URL;

/**
 * class facilitates request made to the the USDA food database API
 */
public class USDAAPIClient {

    @Value("${USDA.secretkeyid}")
    private static String keyId;
    //https://fdc.nal.usda.gov/faq.html
    //Branded, SR Legacy and Survey(FNDDS) foods are available for general public except for Foundation and Experimental Foods
    private static final String dataTypeList = "Branded,SR%20Legacy,Survey%20(FNDDS)";

    /**
     * Returns a list of foods that matched search (query) keywords
     *
     * @param query
     * @return JsonObject
     * @throws IOException
     */
    public static JsonObject getFoodSearchResponse(String query) throws IOException {
        URL endpoint = new URL(String.format("https://api.nal.usda.gov/fdc/v1/foods/search?api_key=%s&dataType=%s&query=%s",
                keyId, dataTypeList, query));
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(URLReader.getUrlContent(endpoint));
        return jsonObject;
    }

    /**
     * Fetches details for one food item by FDC ID
     *
     * @param fdcId FoodData Central ID
     * @return JsonObject
     * @throws IOException
     */
    public static JsonObject getFoodDetailsResponse(int fdcId) throws IOException {
        URL endpoint = new URL(String.format("https://api.nal.usda.gov/fdc/v1/food/%d?api_key=%s&dataType=%s", fdcId, keyId, dataTypeList));
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(URLReader.getUrlContent(endpoint));
        return jsonObject;
    }

}
