package controllers;

import Service.*;
import calorieapp.HTTPSingleResponse;
import com.google.gson.*;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import models.Consumption;
import models.Exercise;
import models.FoodItem;
import models.Goal;
import utils.HelperUtils;
import utils.USDAAPIClient;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * calorie controller
 */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api")
public class CalorieController {
    @Inject
    protected final FoodService foodService;
    @Inject
    protected final UserService userService;
    @Inject
    protected final UserFoodService userFoodService;
    @Inject
    protected final ConsumptionService consumptionService;
    @Inject
    protected final UserFoodConsumptionService userFoodConsumptionService;

    /**
     * constructor
     *
     * @param foodService
     * @param userService
     * @param userFoodService
     * @param consumptionService
     * @param userFoodConsumptionService
     */
    public CalorieController(FoodService foodService, UserService userService, UserFoodService userFoodService, ConsumptionService consumptionService, UserFoodConsumptionService userFoodConsumptionService) {
        this.foodService = foodService;
        this.userService = userService;
        this.userFoodService = userFoodService;
        this.consumptionService = consumptionService;
        this.userFoodConsumptionService = userFoodConsumptionService;
    }

    /**
     * get food details by fdcId
     *
     * @param fdcId
     * @return HTTPSingleResponse
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
     * @return HTTPSingleResponse
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
     * create consumption
     *
     * @param object
     * @return HTTPSingleResponse
     */
    @Post("/consumptions")
    public HTTPSingleResponse CreateConsumption(@Body String object) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            JsonObject consumptionObject = new JsonParser().parse(object).getAsJsonObject();
            Consumption consumption = userFoodConsumptionService.extractConsumptionData(consumptionObject.getAsJsonObject("consumption"));
            // error occurred show appropriate error message
            if (consumption == null) {
                response.success = false;
                response.errorMessage = "unable to parse json object";
            } else {
                Consumption savedItem = consumptionService.saveOrUpdateConsumption(consumption);
                map.put(String.valueOf(savedItem.getId()), savedItem);
                response.success = true;
                response.data = map;
            }
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to create new consumption";
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * delete consumption
     *
     * @param consumptionId
     * @return HTTPSingleResponse
     */
    @Post("/consumptions/delete/{consumptionId}")
    public HTTPSingleResponse deleteConsumption(int consumptionId) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        try {
            consumptionService.deleteConsumptionById(consumptionId);
            response.success = true;
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to delete record for consumptionId:" + consumptionId;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * get consumption
     *
     * @param userId
     * @param consumptionDate
     * @return HTTPSingleResponse
     */
    @Get("/consumptions")
    public HTTPSingleResponse getConsumptions(int userId, String consumptionDate) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(consumptionDate);
            List<Consumption> userConsumptions = userService.getUserConsumptions(userId, date);
            map.put(String.valueOf(userId), userConsumptions);
            response.success = true;
            response.data = map;
            if (userConsumptions.size() <= 0) {
                response.errorMessage = "No records found";
            }
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to fetch consumptions for userId:" + userId + " and consumptionDate:" + consumptionDate;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * update consumption
     *
     * @param object
     * @return HTTPSingleResponse
     */
    @Post("/consumptions/update")
    public HTTPSingleResponse updateConsumption(@Body String object) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            JsonObject consumptionObject = new JsonParser().parse(object).getAsJsonObject();
            Consumption consumption = userFoodConsumptionService.extractConsumptionData(consumptionObject.getAsJsonObject("consumption"));
            Consumption savedItem = consumptionService.saveOrUpdateConsumption(consumption);
            response.success = true;
            map.put(String.valueOf(consumption.getId()), savedItem);
            response.data = map;
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to update the consumption record";
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * set user goals
     *
     * @param userId
     * @param object
     * @return HTTPSingleResponse
     */
    @Post("/goals/{userId}")
    public HTTPSingleResponse setUserGoal(int userId, @Body String object) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            JsonObject goalObject = new JsonParser().parse(object).getAsJsonObject();
            List<Goal> goals = userFoodConsumptionService.saveOrUpdateUserGoals(userId, goalObject.getAsJsonObject("goals"));
            response.success = true;
            map.put(String.valueOf(userId), goals);
            response.data = map;
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to set goals for user " + userId;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * get user goals
     *
     * @param userId
     * @return HTTPSingleResponse
     */
    @Get("/goals")
    public HTTPSingleResponse getUserGoal(int userId) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            List<Goal> userGoals = userService.getUserGoals(userId);
            map.put(String.valueOf(userId), userGoals);
            response.success = true;
            response.data = map;
            if (userGoals.size() <= 0) {
                response.errorMessage = "No records found";
            }
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to fetch goals for userId:" + userId;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * add or update exercise
     *
     * @param userId
     * @param object
     * @return HTTPSingleResponse
     */
    @Post("/exercise/{userId}")
    public HTTPSingleResponse addOrUpdateUserExercises(int userId, @Body String object) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            JsonObject exerciseObject = new JsonParser().parse(object).getAsJsonObject();
            Exercise exercise = userFoodConsumptionService.saveOrUpdateExercise(userId, exerciseObject.getAsJsonObject("activity"));
            response.success = true;
            map.put(String.valueOf(userId), exercise);
            response.data = map;
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to save exercise record for user " + userId;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * get user exercise
     *
     * @param userId
     * @param exerciseDate
     * @return HTTPSingleResponse
     */
    @Get("/exercise")
    public HTTPSingleResponse getUserExercises(int userId, String exerciseDate) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(exerciseDate);
            Exercise exercise = userService.getUserExercise(userId, date);
            response.success = true;
            map.put(String.valueOf(userId), exercise);
            response.data = map;
            if (exercise == null) {
                response.errorMessage = "No records found";
            }
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to fetch exercise record for userId:" + userId + " and exerciseDate:" + exerciseDate;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

    /**
     * get user statistics from given range
     *
     * @param userId
     * @param dateFrom
     * @param dateTo
     * @return HTTPSingleResponse
     */
    @Get("/stats")
    public HTTPSingleResponse getStatistics(int userId, String dateFrom, String dateTo) {
        HTTPSingleResponse response = new HTTPSingleResponse();
        HashMap<String, Object> map = new HashMap<>();
        try {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom);
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateTo);
            JsonObject object = userFoodConsumptionService.getStatisticsForGivenRange(userId, fromDate, toDate);
            response.success = true;
            map.put(String.valueOf(userId), new Gson().toJson(object));
            response.data = map;
        } catch (Exception ex) {
            response.success = false;
            response.errorMessage = "unable to fetch statistics for userId:" + userId + " from date:" + dateFrom + " to date:" + dateTo;
            HelperUtils.logErrorMessage(response.errorMessage, ex);
        }
        return response;
    }

}
