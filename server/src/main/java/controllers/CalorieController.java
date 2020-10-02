package controllers;

import calorieapp.FoodItemsExtractor;
import calorieapp.FoodItemsExtractorFactory;
import calorieapp.HTTPSingleResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import models.FoodItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.USDAAPIClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * calorie controller
 */
@Controller("/api")
public class CalorieController {
    private static final Logger log = LoggerFactory.getLogger(CalorieController.class);

    /**
     * get food details by fdcId
     *
     * @param fdcId
     */
    // api/foods?fdcId=167782
    @Get("/foods")
    public HTTPSingleResponse getFoodDetails(int fdcId) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        try {
            FoodItem item = getFoodDetailsByFdcID(fdcId);
            Map<String, Object> map = new HashMap<>();
            if (item != null)
                map.put(String.valueOf(fdcId), item);
            response.success = true;
            response.data = map;
        } catch (Exception ex) {
            //provide a user friendly message
            response.success = false;
            response.errorMessage = "unable to read food details response for fdcId:" + fdcId;

            //log the error
            log.error("unable to read food details response for fdcId:" + fdcId);
            log.error(ex.getStackTrace().toString());
        }
        return response;
    }

    /**
     * search foods
     *
     * @param query
     */
    // api/foods/search?query=Cheddar cheese
    @Get("/foods/search")
    public HTTPSingleResponse searchFoods(String query) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        try {
            Set<FoodItem> set = new HashSet<>();
            JsonObject foodObject = USDAAPIClient.getFoodSearchResponse(query);
            JsonArray foods = foodObject.getAsJsonArray("foods");
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < foods.size(); i++) {
                JsonObject food = foods.get(i).getAsJsonObject();
                int fdcId = food.get("fdcId").getAsInt();
                FoodItem item = getFoodDetailsByFdcID(fdcId);
                if (item != null)
                    set.add(item);
            }
            response.success = true;
            map.put(query, set);
            response.data = map;
        } catch (Exception ex) {
            //provide a user friendly message
            response.success = false;
            response.errorMessage = "unable to search response for query:" + query;

            //log the error
            log.error("unable to search response for query:" + query);
            log.error(ex.getStackTrace().toString());
        }
        return response;
    }

    /**
     * helper function to get Food Details By FdcID
     *
     * @param fdcId
     * @return FoodItem
     * @throws IOException
     */
    private FoodItem getFoodDetailsByFdcID(int fdcId) throws Exception {
        JsonObject foodDetails = USDAAPIClient.getFoodDetailsResponse(fdcId);

        FoodItemsExtractorFactory foodDetailsExtractorFactory = new FoodItemsExtractorFactory();
        FoodItemsExtractor extractor = foodDetailsExtractorFactory.getFoodItemsExtractor(foodDetails.get("dataType").getAsString());
        FoodItem foodItem = extractor.extract(foodDetails);
        return foodItem;
    }

}
