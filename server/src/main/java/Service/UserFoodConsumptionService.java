package Service;

import com.google.gson.JsonObject;
import models.*;

import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public Consumption extractConsumptionData(JsonObject consumptionObject) throws ParseException {
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
        Date consumptionDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);

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
    public Exercise saveOrUpdateExercise(int userId, JsonObject exerciseObject) throws ParseException {
        User user = userService.findUserById(userId);
        Integer exerciseId = -1;
        Exercise exercise;
        int caloriesBurned = exerciseObject.get("caloriesBurned").getAsInt();
        String stringDate = exerciseObject.get("exerciseDate").getAsString();
        Date exerciseDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
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

    /**
     * get consumptions in giver range
     *
     * @param userId
     * @param dateFrom
     * @param dateTo
     * @return Map<Date, Map < String, Long>>
     * @throws Exception
     */
    public Map<Date, Map<String, Long>> getConsumptionInGivenRange(int userId, Date dateFrom, Date dateTo) throws Exception {
        Map<Date, Map<String, Long>> nutrientTotals = new HashMap<>();
        List<Consumption> consumptions = consumptionService.getConsumptionList(userId, dateFrom, dateTo);
        if (consumptions.size() > 0) {
            for (Consumption consumption : consumptions) {
                nutrientTotals.putIfAbsent(consumption.getConsumptionDate(), new HashMap<>());
                updateNutrientMap(nutrientTotals.get(consumption.getConsumptionDate()), consumption);
            }
        }
        return nutrientTotals;
    }

    /**
     * method to count nutrient totals
     *
     * @param nestedMap
     * @param consumption
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void updateNutrientMap(Map<String, Long> nestedMap, Consumption consumption) throws NoSuchFieldException, IllegalAccessException {
        for (Goal.GoalCategory category :
                Goal.GoalCategory.values()) {
            nestedMap.putIfAbsent(category.toString().toLowerCase(), new Long(0));
            Long count = nestedMap.get(category.toString().toLowerCase()) + consumption.calculateCategoryValue(category);
            nestedMap.put(category.toString().toLowerCase(), count);
        }
    }

    /**
     * get exercise in given range
     *
     * @param userId
     * @param dateFrom
     * @param dateTo
     * @return Map<Date, Exercise>
     */
    public Map<Date, Exercise> getExerciseInGivenRange(int userId, Date dateFrom, Date dateTo) {
        Map<Date, Exercise> map = new HashMap<>();
        List<Exercise> exercises = consumptionService.getExerciseList(userId, dateFrom, dateTo);
        if (exercises.size() > 0) {
            for (Exercise exercise : exercises) {
                map.put(exercise.getExerciseDate(), exercise);
            }
        }
        return map;
    }
}
