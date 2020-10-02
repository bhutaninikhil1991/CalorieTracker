package calorieapp;

import com.google.gson.JsonObject;
import models.ServingSize;
import utils.HelperUtils;

/**
 * class to extract Survey food items
 */
public class SurveyFoodItemsExtractor extends NonBrandedFoodItemsExtractor {
    /**
     * extract weight and quantity for Survey food
     *
     * @param foodPortion
     * @return ServingSize
     */
    @Override
    public ServingSize extractFoodPortion(JsonObject foodPortion) {
        String portionDescription = foodPortion.get("portionDescription").getAsString();
        ServingSize servingSize = HelperUtils.parseServingText(portionDescription);
        double quantity = servingSize.getQuantity();
        double gramWeight = foodPortion.get("gramWeight").getAsDouble();
        double ratio = calculateRatio(gramWeight, quantity);
        servingSize.setRatio(ratio);
        return servingSize;
    }
}
