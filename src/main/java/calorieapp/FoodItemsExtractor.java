package calorieapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import models.FoodItem;
import models.ServingSize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class to extract food items
 */
public abstract class FoodItemsExtractor {
    private Map<String, Double> nutrientMap = new HashMap<>();

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
        //extract food portions
        List<ServingSize> servingSizes = extractFoodPortions(foodDetails);

        //create food item object
        FoodItem foodItem = new FoodItem(fdcId, name, nutrientMap.get("carbohydrates"), nutrientMap.get("fat"), nutrientMap.get("proteins"), servingSizes);
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

    public abstract List<ServingSize> extractFoodPortions(JsonObject foodDetails);

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
