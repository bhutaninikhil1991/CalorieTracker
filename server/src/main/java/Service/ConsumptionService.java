package Service;

import Repository.ConsumptionRepository;
import Repository.FoodItemRepository;
import Repository.UserRepository;
import com.google.gson.JsonObject;
import models.Consumption;
import models.FoodItem;
import models.ServingSize;
import models.User;

import javax.inject.Singleton;
import java.time.LocalDate;

@Singleton
public class ConsumptionService {
    private ConsumptionRepository consumptionRepository;
    private FoodItemRepository foodItemRepository;
    private UserRepository userRepository;

    public ConsumptionService(ConsumptionRepository consumptionRepository, FoodItemRepository foodItemRepository, UserRepository userRepository) {
        this.consumptionRepository = consumptionRepository;
        this.foodItemRepository = foodItemRepository;
        this.userRepository = userRepository;
    }

    /**
     * delete consumption by Id
     *
     * @param consumptionId
     */
    public void deleteConsumptionById(int consumptionId) {
        consumptionRepository.deleteById(consumptionId);
    }

    /**
     * get consumption by Id
     *
     * @param consumptionId
     * @return Consumption
     */
    public Consumption getConsumptionById(int consumptionId) {
        return consumptionRepository.findById(consumptionId).orElse(null);
    }

    /**
     * save consumption
     *
     * @param consumption
     * @return Consumption
     */
    public Consumption saveConsumption(Consumption consumption) {
        return consumptionRepository.saveOrUpdate(consumption);
    }

    /**
     * extract consumption data
     *
     * @param consumptionObject
     * @return Consumption
     */
    public Consumption extractConsumptionData(JsonObject consumptionObject) {
        if (consumptionObject == null)
            return null;
        Integer userId = consumptionObject.get("userId").getAsInt();
        JsonObject item = consumptionObject.get("foodItem").getAsJsonObject();
        Integer foodItemId = item.get("id").getAsInt();
        JsonObject selectedServing = consumptionObject.get("selectedServing").getAsJsonObject();
        JsonObject selectedServingSize = selectedServing.get("servingSize").getAsJsonObject();
        Integer selectedServingSizeId = selectedServingSize.get("id").getAsInt();
        double servingQuantity = selectedServing.get("quantity").getAsDouble();
        String stringDate = consumptionObject.get("consumptionDate").getAsString();
        LocalDate consumptionDate = LocalDate.parse(stringDate);
        Consumption consumption = getConsumptionObject(userId, foodItemId, selectedServingSizeId, servingQuantity, consumptionDate);
        return consumption;
    }

    /**
     * update consumption
     *
     * @param consumptionId
     * @param consumptionObject
     * @return Consumption
     */
    public Consumption updateConsumption(int consumptionId, JsonObject consumptionObject) {
        if (consumptionObject == null)
            return null;
        Consumption consumption = getConsumptionById(consumptionId);
        if (consumption != null) {
            Integer selectedServingSizeId = consumptionObject.get("selectedServingSizeId").getAsInt();
            double servingQuantity = consumptionObject.get("servingQuantity").getAsDouble();
            ServingSize servingSize = foodItemRepository.findByServingId(consumption.getFoodItem().getId(), selectedServingSizeId).orElse(null);
            if (servingSize != null && servingQuantity > 0) {
                consumption.setSelectedServing(servingSize);
                consumption.setServingQuantity(servingQuantity);
                consumption = saveConsumption(consumption);
            }
        }
        return consumption;
    }

    /**
     * get consumption object
     *
     * @param userId
     * @param foodItemId
     * @param selectedServingSizeId
     * @param servingQuantity
     * @param date
     * @return Consumption
     */
    private Consumption getConsumptionObject(int userId, int foodItemId, int selectedServingSizeId, double servingQuantity, LocalDate date) {
        Consumption consumption = new Consumption();
        User user = userRepository.findByUserId(userId).orElse(null);
        FoodItem foodItem = foodItemRepository.findById(foodItemId).orElse(null);
        ServingSize servingSize = foodItemRepository.findByServingId(foodItem.getId(), selectedServingSizeId).orElse(null);
        //set user
        if (user != null)
            consumption.setUser(user);
        //set food item
        if (foodItem != null)
            consumption.setFoodItem(foodItem);
        //set serving size
        if (servingSize != null)
            consumption.setSelectedServing(servingSize);
        // set serving quantity
        consumption.setServingQuantity(servingQuantity);
        //set consumption date
        consumption.setConsumptionDate(date);
        return consumption;
    }
}
