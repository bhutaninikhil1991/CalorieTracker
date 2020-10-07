package calorieapp;

import com.google.gson.JsonObject;
import models.FoodItem;
import models.ServingSize;
import utils.HelperUtils;


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
    public void extractFoodPortions(FoodItem foodItem, JsonObject foodDetails) {
        String householdServingText = foodDetails.get("householdServingFullText").getAsString();
        String baseServingSizeLabel = foodDetails.get("servingSizeUnit").getAsString();
        // The base serving size is provided with a common measuring unit such as grams or milliliters. All nutrient
        // amounts are normalized to 1 of this measuring unit (i.e. how much protein is in 1 gram of applesauce).
        double ratio = 1.0;
        ServingSize baseServingSize = new ServingSize(baseServingSizeLabel, ratio);
        if (!householdServingText.isEmpty()) {
            double baseServingSizeQuantity = foodDetails.get("servingSize").getAsDouble();
            //if the serving size label is equal to 'Quantity not specified' then skip
            ServingSize householdServingSize = HelperUtils.parseServingText(householdServingText);
            double householdServingQuantity = householdServingSize.getQuantity();
            ratio = calculateRatio(baseServingSizeQuantity, householdServingQuantity);
            householdServingSize.setRatio(ratio);
            foodItem.addServingSize(householdServingSize);
        } else {
            foodItem.addServingSize(baseServingSize);
        }
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
