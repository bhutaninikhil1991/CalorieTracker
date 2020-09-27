package calorieapp;

import com.google.gson.JsonObject;
import models.ServingSize;

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
        //if the serving size label is equal to 'Quantity not specified' then skip
        if (portionDescription.equals("Quantity not specified"))
            return null;
        int index = portionDescription.indexOf(' ');
        double quantity;
        try {
            quantity = Integer.valueOf(portionDescription.substring(0, index));
        } catch (Exception e) {
            //if the serving text is not in proper format then skip
            return null;
        }
        double gramWeight = foodPortion.get("gramWeight").getAsDouble();
        String label = portionDescription.substring(index + 1);
        double ratio = calculateRatio(gramWeight, quantity);
        return new ServingSize(label, ratio);
    }
}
