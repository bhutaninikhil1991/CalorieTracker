package Service;

import com.google.gson.JsonObject;
import models.*;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * user food consumption service
 */
@Singleton
public class UserFoodConsumptionService {
    private FoodService foodService;
    private ConsumptionService consumptionService;
    private UserService userService;

    /**
     * constructor
     *
     * @param foodService
     * @param consumptionService
     * @param userService
     */
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

    /**
     * save or update user goals
     *
     * @param userId
     * @param goalsObject
     * @return List<Goal>
     */
    public List<Goal> saveOrUpdateUserGoals(int userId, JsonObject goalsObject) {
        List<Goal> newGoals = new ArrayList<>();
        User user = userService.findUserById(userId);
        List<Goal> userGoals = user.getUserGoals();
        Map<Goal.GoalCategory, Goal> map = new HashMap<>();
        for (Goal goal : userGoals) {
            map.put(goal.getGoalCategory(), goal);
        }
        for (Goal.GoalCategory goalCategory : Goal.GoalCategory.values()) {
            String category = goalCategory.toString().toLowerCase();
            if (goalsObject.has(category)) {
                Goal goal;
                int goalValue = goalsObject.get(category).getAsInt();
                if (map.containsKey(goalCategory)) {
                    goal = map.get(goalCategory);
                    if (goal.getGoalValue() != goalValue)
                        goal.setGoalValue(goalValue);
                } else {
                    goal = new Goal(user, goalCategory, goalValue);
                }
                newGoals.add(consumptionService.saveOrUpdateGoal(goal));
            }
        }
        return newGoals;
    }

    /**
     * save or update Exercise
     *
     * @param userId
     * @param exerciseObject
     * @return Exercise
     */
    public Exercise saveOrUpdateExercise(int userId, JsonObject exerciseObject) {
        User user = userService.findUserById(userId);
        Integer exerciseId = -1;
        Exercise exercise;
        int caloriesBurned = exerciseObject.get("caloriesBurned").getAsInt();
        String stringDate = exerciseObject.get("exerciseDate").getAsString();
        LocalDate exerciseDate = LocalDate.parse(stringDate);
        if (exerciseObject.has("id"))
            exerciseId = exerciseObject.get("id").getAsInt();

        if (exerciseId > 0) {
            exercise = consumptionService.getExerciseById(exerciseId);
            exercise.setCaloriesBurned(caloriesBurned);
        } else {
            exercise = new Exercise(user, caloriesBurned, exerciseDate);
        }
        Exercise newExercise = consumptionService.saveOrUpdateExercise(exercise);
        return newExercise;
    }
}
