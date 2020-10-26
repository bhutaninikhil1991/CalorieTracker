package Service;

import Repository.UserRepository;
import models.Consumption;
import models.FoodItem;
import models.User;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;

@Singleton
/**
 * service responsible for user related operations
 */
public class UserService {
    private UserRepository userRepository;

    /**
     * constructor
     *
     * @param userRepository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * find user by userId
     *
     * @param userId
     * @return User
     */
    public User findUserById(int userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    /**
     * delete user by UserId
     *
     * @param userId
     */
    public void deleteUserById(int userId) {
        userRepository.deleteByUserId(userId);
    }

    /**
     * save user
     *
     * @param user
     * @return User
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * get foodItems related to user
     *
     * @param userId
     * @return List<FoodItems>
     */
    public List<FoodItem> getUserFoodItems(int userId) {
        return userRepository.getUserFoodItems(userId);
    }

    public List<Consumption> getUserConsumptions(int userId, LocalDate consumptionDate) {
        return userRepository.getUserConsumptions(userId, consumptionDate);
    }
}
