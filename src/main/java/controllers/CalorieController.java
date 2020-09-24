package controllers;

import com.google.gson.JsonObject;
import io.micronaut.http.annotation.Controller;
import utils.USDAAPIClient;

import java.io.IOException;

/**
 * calorie controller
 */
@Controller("/api")
public class CalorieController {

    /**
     * get food details by fdcId
     *
     * @param fdcID
     * @throws IOException
     */
    public void getFoodDetailsByFdcID(int fdcID) throws IOException {
        JsonObject foodDetails = USDAAPIClient.getFoodDetailsResponse(fdcID);
    }

    /**
     * search foods
     *
     * @param query
     * @throws IOException
     */
    public void searchFoods(String query) throws IOException {
        JsonObject foods = USDAAPIClient.getFoodSearchResponse(query);
    }

}
