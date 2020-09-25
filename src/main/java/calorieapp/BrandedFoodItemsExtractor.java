package calorieapp;

import com.google.gson.JsonObject;
import models.ServingSize;

import java.util.ArrayList;
import java.util.List;


/**
 * class to extract Branded food items
 */
public class BrandedFoodItemsExtractor extends FoodItemsExtractor {

    /**
     * extract food portion for Branded food
     *
     * @param foodDetails
     * @return ServingSize
     */
    @Override
    public List<ServingSize> extractFoodPortions(JsonObject foodDetails) {
        List<ServingSize> servingSizeList = new ArrayList<>();
        String householdServingText = foodDetails.get("householdServingFullText").getAsString();
        String baseServingSizeLabel = foodDetails.get("servingSizeUnit").getAsString();
        // The base serving size is provided with a common measuring unit such as grams or milliliters. All nutrient
        // amounts are normalized to 1 of this measuring unit (i.e. how much protein is in 1 gram of applesauce).
        double ratio = 1.0;
        ServingSize baseServingSize = new ServingSize(baseServingSizeLabel, ratio);
        servingSizeList.add(baseServingSize);
        if (!householdServingText.isEmpty()) {
            double baseServingSizeQuantity = foodDetails.get("servingSize").getAsDouble();
            //if the serving size label is equal to 'Quantity not specified' then skip
            if (householdServingText.equals("Quantity not specified"))
                return null;
            int index = householdServingText.indexOf(' ');
            double householdServingQuantity;
            try {
                householdServingQuantity = Integer.valueOf(householdServingText.substring(0, index));
            } catch (Exception e) {
                //if the serving text is not in proper format then skip
                return null;
            }
            ratio = calculateRatio(baseServingSizeQuantity, householdServingQuantity);
            String label = householdServingText.substring(index + 1);
            ServingSize householdServingSize = new ServingSize(label, ratio);
            servingSizeList.add(0, householdServingSize);
        }
        return servingSizeList;
    }

    /**
     * Calculate serving size ratio as {base serving size quantity} * (1 / {household serving size quantity}). Its ratio
     * indicates how many of the base units are in one household units. As an example, say we have a pizza
     * whose base serving size is 126 grams. The household label indicates that this is equivalent to .2 of a pizza.
     * So to figure out how many grams are in an entire pizza, we multiply by 5 (which is 1 / .2).
     *
     * @param baseServingSizeQuantity
     * @param quantity
     * @return double
     */
    @Override
    public double calculateRatio(double baseServingSizeQuantity, double quantity) {
        return baseServingSizeQuantity * (1 / quantity);
    }
}
