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
        return consumptionRepository.save(consumption);
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
        Integer foodItemId = consumptionObject.get("foodItemId").getAsInt();
        Integer servingSizeId = consumptionObject.get("servingSizeId").getAsInt();
        double servingQuantity = consumptionObject.get("servingQuantity").getAsDouble();
        String stringDate = consumptionObject.get("consumptionDate").getAsString();
        LocalDate consumptionDate = LocalDate.parse(stringDate);
        Consumption consumption = getConsumptionObject(userId, foodItemId, servingSizeId, servingQuantity, consumptionDate);
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
            Integer servingSizeId = consumptionObject.get("servingSizeId").getAsInt();
            double servingQuantity = consumptionObject.get("servingQuantity").getAsDouble();
            ServingSize servingSize = foodItemRepository.findByServingId(servingSizeId).orElse(null);
            if (servingSize != null && servingQuantity > 0) {
                consumption.setSelectedServingSize(servingSize);
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
     * @param servingSizeId
     * @param servingQuantity
     * @param date
     * @return Consumption
     */
    private Consumption getConsumptionObject(int userId, int foodItemId, int servingSizeId, double servingQuantity, LocalDate date) {
        Consumption consumption = new Consumption();
        User user = userRepository.findByUserId(userId).orElse(null);
        FoodItem foodItem = foodItemRepository.findById(foodItemId).orElse(null);
        ServingSize servingSize = foodItemRepository.findByServingId(servingSizeId).orElse(null);
        //set user
        if (user != null)
            consumption.setUser(user);
        //set food item
        if (foodItem != null)
            consumption.setFoodItem(foodItem);
        //set serving size
        if (servingSize != null)
            consumption.setSelectedServingSize(servingSize);
        // set serving quantity
        consumption.setServingQuantity(servingQuantity);
        //set consumption date
        consumption.setConsumptionDate(date);
        return consumption;
    }
}
