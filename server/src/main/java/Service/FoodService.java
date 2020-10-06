package Service;

import Repository.FoodItemRepository;
import calorieapp.FoodItemsExtractor;
import calorieapp.FoodItemsExtractorFactory;
import com.google.gson.JsonObject;
import models.FoodItem;
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
}
