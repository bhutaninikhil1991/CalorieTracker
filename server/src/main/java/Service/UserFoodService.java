package Service;

import models.FoodItem;
import models.User;

import javax.inject.Singleton;

/**
 * service responsible for user and food items related operations
 */
@Singleton
public class UserFoodService {
    private FoodService foodService;
    private UserService userService;

    /**
     * constructor
     *
     * @param foodService
     * @param userService
     */
    public UserFoodService(FoodService foodService, UserService userService) {
        this.userService = userService;
        this.foodService = foodService;
    }

    /**
     * asave food items for the user
     *
     * @param userId
     * @param item
     * @return FoodItem
     * @throws Exception
     */
    public FoodItem saveUserFoodItems(int userId, FoodItem item) throws Exception {
        User user = userService.findUserById(userId);
        if (user == null)
            throw new Exception("User not found");
        item.setUser(user);
        return foodService.saveFoodItem(item);
    }
}
