package calorieapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import controllers.CalorieController;
import models.FoodItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * class to extract food items
 */
public abstract class FoodItemsExtractor {
    private Map<String, Double> nutrientMap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(CalorieController.class);

    /**
     * extract food items from food details object
     *
     * @param foodDetails
     * @return FoodItem
     */
    public FoodItem extract(JsonObject foodDetails) {
        int fdcId = foodDetails.get("fdcId").getAsInt();
        String name = foodDetails.get("description").getAsString();
        JsonArray foodNutrients = foodDetails.getAsJsonArray("foodNutrients");

        //extract food nutrients
        for (int i = 0; i < foodNutrients.size(); i++) {
            JsonObject foodNutrient = foodNutrients.get(i).getAsJsonObject();
            extractNutrient(foodNutrient);
        }
        //create food item object
        FoodItem foodItem = new FoodItem(fdcId, name, nutrientMap.get("carbohydrates"), nutrientMap.get("fat"), nutrientMap.get("proteins"));
        try {
            //extract food portions and store it in food item object
            extractFoodPortions(foodItem, foodDetails);
        } catch (Exception ex) {
            //if there is an issue in extracting food portion then log exception and return null
            log.error("unable to extract food portion for fdcId:" + fdcId + " name:" + name);
            log.error(ex.getStackTrace().toString());
            return null;
        }

        return foodItem;
    }

    /**
     * extract nutrients from food Nutrient object
     *
     * @param foodNutrient
     */
    public void extractNutrient(JsonObject foodNutrient) {
        JsonObject nutrient = foodNutrient.get("nutrient").getAsJsonObject();
        int number = nutrient.get("number").getAsInt();
        if (number == 203)
            nutrientMap.put("proteins", foodNutrient.get("amount").getAsDouble());
        else if (number == 204)
            nutrientMap.put("fat", foodNutrient.get("amount").getAsDouble());
        else if (number == 205)
            nutrientMap.put("carbohydrates", foodNutrient.get("amount").getAsDouble());
    }

    public abstract void extractFoodPortions(FoodItem foodItem, JsonObject foodDetails);

    /**
     * Nutrient information is provided per 100 grams, so ratio is compared to this standard.
     *
     * @param gramWeight
     * @param quantity
     * @return double
     */
    public double calculateRatio(double gramWeight, double quantity) {
        double perUnitGramWeight = gramWeight / quantity;
        return perUnitGramWeight / 100.0;
    }

}
