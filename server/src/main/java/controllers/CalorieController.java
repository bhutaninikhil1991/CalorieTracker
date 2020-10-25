package controllers;

import Service.FoodService;
import Service.UserFoodService;
import Service.UserService;
import calorieapp.HTTPSingleResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import models.FoodItem;
import models.User;
import utils.HelperUtils;
import utils.USDAAPIClient;

import javax.inject.Inject;
import java.util.*;

/**
 * calorie controller
 */
@ExecuteOn(TaskExecutors.IO)
@Controller("/api")
public class CalorieController {
    @Inject
    protected final FoodService foodService;
    @Inject
    protected final UserService userService;
    @Inject
    protected final UserFoodService userFoodService;

    public CalorieController(FoodService foodService, UserService userService, UserFoodService userFoodService) {
        this.foodService = foodService;
        this.userService = userService;
        this.userFoodService = userFoodService;
    }

    /**
     * get food details by fdcId
     *
     * @param fdcId
     */
    // api/foods?fdcId=167782
    @Get("/foods")
    public HTTPSingleResponse getFoodDetails(int fdcId) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        try {
            FoodItem item = foodService.getFoodDetailsByFdcID(fdcId);
            Map<String, Object> map = new HashMap<>();
            if (item != null)
                map.put(String.valueOf(fdcId), item);
            response.success = true;
            response.data = map;
        } catch (Exception ex) {
            //provide a user friendly message
            response.success = false;
            response.errorMessage = "unable to read food details response for fdcId:" + fdcId;

            //log the error
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * search foods
     *
     * @param query
     */
    // api/foods/search?query=Cheddar cheese
    @Get("/foods/search")
    public HTTPSingleResponse searchFoods(String query) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        try {
            Set<FoodItem> set = new HashSet<>();
            JsonObject foodObject = USDAAPIClient.getFoodSearchResponse(query);
            JsonArray foods = foodObject.getAsJsonArray("foods");
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < foods.size(); i++) {
                JsonObject food = foods.get(i).getAsJsonObject();
                int fdcId = food.get("fdcId").getAsInt();
                FoodItem item = foodService.getFoodDetailsByFdcID(fdcId);
                if (item != null)
                    set.add(item);
            }
            response.success = true;
            map.put(query, set);
            response.data = map;
        } catch (Exception ex) {
            //provide a user friendly message
            response.success = false;
            response.errorMessage = "unable to search response for query:" + query;

            //log the error
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * create food Item
     *
     * @param userId
     * @param object
     * @return HTTPSingleResponse
     */
    @Post("/foods/{userId}")
    public HTTPSingleResponse createFoodItemForUser(int userId, @Body String object) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            JsonObject foodObject = new JsonParser().parse(object).getAsJsonObject();
            FoodItem foodItem = foodService.extractFoodData(foodObject.getAsJsonObject("foodItem"));
            // error occurred show appropriate error message
            if (foodItem == null) {
                response.success = false;
                response.errorMessage = "unable to parse json object";
            } else {
                FoodItem savedItem = userFoodService.saveUserFoodItems(userId, foodItem);
                map.put(String.valueOf(savedItem.getId()), savedItem);
                response.success = true;
                response.data = map;
            }
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to create food Item for userId:" + userId;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * delete user created food Item
     *
     * @param foodId
     * @return HTTPSingleResponse
     */
    @Post("/foods/remove/{foodId}")
    public HTTPSingleResponse deleteUserCreatedFoodItem(int foodId) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            foodService.deleteFoodById(foodId);
            response.success = true;
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to delete record for foodItemId:" + foodId;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * get user created food Items
     *
     * @param userId
     * @return HTTPSingleResponse
     */
    @Get("/foods/user")
    public HTTPSingleResponse getUserCreatedFoodItems(int userId) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            List<FoodItem> userFoods = userService.getUserFoodItems(userId);
            map.put(String.valueOf(userId), userFoods);
            response.success = true;
            response.data = map;
            if (userFoods.size() <= 0) {
                response.errorMessage = "No records found";
            }
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to fetch food Items for userId:" + userId;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * register user
     *
     * @param user
     * @return HTTPSingleResponse
     */
    @Post("users/register")
    public HTTPSingleResponse registerUser(@Body User user) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            User usr = userService.createUser(user);
            map.put(String.valueOf(usr.getId()), usr);
            response.success = true;
            response.data = map;
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to register user";
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

}
