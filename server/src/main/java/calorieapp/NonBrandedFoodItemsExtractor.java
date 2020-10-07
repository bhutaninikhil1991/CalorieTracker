package calorieapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import models.FoodItem;
import models.ServingSize;

import java.util.ArrayList;
import java.util.List;

/**
 * class to abstract food portions
 */
public abstract class NonBrandedFoodItemsExtractor extends FoodItemsExtractor {
    private List<ServingSize> servingSizeList = new ArrayList<>();

    /**
     * extract food portions
     *
     * @param foodDetails
     * @return List<ServingSize>
     */
    public void extractFoodPortions(FoodItem foodItem, JsonObject foodDetails) {
        JsonArray foodPortions = foodDetails.getAsJsonArray("foodPortions");
        for (int i = 0; i < foodPortions.size(); i++) {
            JsonObject foodPortion = foodPortions.get(i).getAsJsonObject();
            try {
                ServingSize servingSize = extractFoodPortion(foodPortion);
                if (servingSize != null)
                    foodItem.addServingSize(servingSize);
            } catch (Exception ex) {
                continue;
            }
        }
    }

    /**
     * extract weight and quantity
     *
     * @param foodPortion
     * @return ServingSize
     */
    public abstract ServingSize extractFoodPortion(JsonObject foodPortion);
}
