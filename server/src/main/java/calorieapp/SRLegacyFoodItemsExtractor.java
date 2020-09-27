package calorieapp;

import com.google.gson.JsonObject;
import models.ServingSize;

/**
 * class to extract SR legacy food items
 */
public class SRLegacyFoodItemsExtractor extends NonBrandedFoodItemsExtractor {
    /**
     * extract weight and quantity for SR Legacy food
     *
     * @param foodPortion
     */
    @Override
    public ServingSize extractFoodPortion(JsonObject foodPortion) {
        String label = foodPortion.get("modifier").getAsString();
        double gramWeight = foodPortion.get("gramWeight").getAsDouble();
        double quantity = foodPortion.get("amount").getAsDouble();
        double ratio = calculateRatio(gramWeight, quantity);
        return new ServingSize(label, ratio);
    }
}
