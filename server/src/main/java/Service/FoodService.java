package Service;

import Repository.FoodItemRepository;
import calorieapp.FoodItemsExtractor;
import calorieapp.FoodItemsExtractorFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import models.FoodItem;
import models.ServingSize;
import utils.HelperUtils;
import utils.USDAAPIClient;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * service responsible for food item operations
 */
@Singleton
public class FoodService {
    private FoodItemRepository foodItemRepository;

    /**
     * constructor
     *
     * @param foodItemRepository
     */
    public FoodService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    /**
     * delete food Item by Id
     *
     * @param foodId
     */
    public void deleteFoodById(int foodId) {
        foodItemRepository.deleteById(foodId);
    }

    /**
     * get Food Item by Id
     *
     * @param foodId
     * @return FoodItem
     */
    public FoodItem getFoodById(int foodId) {
        return foodItemRepository.findById(foodId).orElse(null);
    }

    /**
     * save food Item
     *
     * @param foodItem
     * @return FoodItem
     */
    public FoodItem saveFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    /**
     * get Food Details By FdcID
     *
     * @param fdcId
     * @return FoodItem
     * @throws IOException
     */
    public FoodItem getFoodDetailsByFdcID(int fdcId) throws Exception {
        JsonObject foodDetails = USDAAPIClient.getFoodDetailsResponse(fdcId);

        FoodItemsExtractorFactory foodDetailsExtractorFactory = new FoodItemsExtractorFactory();
        FoodItemsExtractor extractor = foodDetailsExtractorFactory.getFoodItemsExtractor(foodDetails.get("dataType").getAsString());
        FoodItem foodItem = extractor.extract(foodDetails);
        return foodItem;
    }

    /**
     * extract food data from json object
     *
     * @param object
     * @return FoodItem
     */
    public FoodItem extractFoodData(JsonObject object) {
        try {
            String name = object.get("name").getAsString();
            double carbohydrates = object.get("carbohydrates").getAsDouble();
            double fat = object.get("fat").getAsDouble();
            double protein = object.get("protein").getAsDouble();
            FoodItem foodItem = new FoodItem(0, name, carbohydrates, fat, protein);
            JsonArray jsonArray = object.get("servingSizes").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject servingJson = jsonArray.get(i).getAsJsonObject();
                String label = servingJson.get("servingLabel").getAsString();
                double quantity = servingJson.get("servingAmount").getAsDouble();
                ServingSize servingSize = new ServingSize(label, quantity);
                foodItem.addServingSize(servingSize);
            }
            return foodItem;
        } catch (Exception ex) {
            String errorDescription = "unable to parse json object";
            HelperUtils.logErrorMessage(errorDescription, ex);
            return null;
        }
    }
}
