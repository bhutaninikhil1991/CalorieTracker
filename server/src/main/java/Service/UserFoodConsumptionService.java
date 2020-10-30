package Service;

import com.google.gson.JsonObject;
import models.Consumption;
import models.FoodItem;
import models.ServingSize;
import models.User;

import javax.inject.Singleton;
import java.time.LocalDate;

@Singleton
public class UserFoodConsumptionService {
    private FoodService foodService;
    private ConsumptionService consumptionService;
    private UserService userService;

    public UserFoodConsumptionService(FoodService foodService, ConsumptionService consumptionService, UserService userService) {
        this.foodService = foodService;
        this.consumptionService = consumptionService;
        this.userService = userService;
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

        Consumption consumption = null;

        Integer consumptionId = -1;
        if (consumptionObject.has("id"))
            consumptionId = consumptionObject.get("id").getAsInt();

        //extract user
        Integer userId = consumptionObject.get("userId").getAsInt();
        User user = userService.findUserById(userId);

        //extract food item
        JsonObject foodItemObj = consumptionObject.get("foodItem").getAsJsonObject();
        Integer foodItemId = foodItemObj.get("id").getAsInt();
        FoodItem foodItem = foodService.getFoodItemById(foodItemId);

        //extract selected serving object
        JsonObject selectedServingObject = consumptionObject.get("selectedServing").getAsJsonObject();

        //extract serving object
        JsonObject servingObject = selectedServingObject.get("servingSize").getAsJsonObject();
        String servingLabel = servingObject.get("servingLabel").getAsString();

        //extract serving quantity
        double servingQuantity = selectedServingObject.get("quantity").getAsDouble();

        //if food item is null then create new food item
        if (foodItem == null) {
            foodItem = foodService.extractFoodData(foodItemObj);
            foodService.saveFoodItem(foodItem);
        }

        Integer selectedServingId = foodService.findServingSizeId(foodItem, servingLabel);
        ServingSize servingSize = foodService.getServingSizeById(selectedServingId);

        //extract consumption date
        String stringDate = consumptionObject.get("consumptionDate").getAsString();
        LocalDate consumptionDate = LocalDate.parse(stringDate);

        //create consumption object with new serving and quantity or
        // select existing one if it exists and update the selected serving and quantity
        if (consumptionId > 0) {
            consumption = consumptionService.getConsumptionById(consumptionId);
            consumption.setSelectedServing(servingSize);
            consumption.setServingQuantity(servingQuantity);
        } else {
            consumption = new Consumption(user, foodItem, servingSize, servingQuantity, consumptionDate);
        }

        return consumption;
    }
}
