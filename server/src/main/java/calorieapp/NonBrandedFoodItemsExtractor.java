package calorieapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
    public List<ServingSize> extractFoodPortions(JsonObject foodDetails) {
        JsonArray foodPortions = foodDetails.getAsJsonArray("foodPortions");
        for (int i = 0; i < foodPortions.size(); i++) {
            JsonObject foodPortion = foodPortions.get(i).getAsJsonObject();
            ServingSize servingSize = extractFoodPortion(foodPortion);
            if (servingSize != null)
                servingSizeList.add(servingSize);
        }
        return servingSizeList;
    }

    /**
     * extract weight and quantity
     *
     * @param foodPortion
     * @return ServingSize
     */
    public abstract ServingSize extractFoodPortion(JsonObject foodPortion);
}
