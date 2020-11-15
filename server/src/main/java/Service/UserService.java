package Service;

import Repository.UserRepository;
import models.*;

import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

/**
 * service responsible for user related operations
 */
@Singleton
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;

    /**
     * constructor
     *
     * @param userRepository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoderService();
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
     * find user by email address
     *
     * @param emailAddress
     * @return User
     */
    public User findUserByEmailAddress(String emailAddress) {
        return userRepository.findUserByEmailAddress(emailAddress);
    }

    /**
     * validate user credentials
     *
     * @param emailAddress
     * @param password
     * @return boolean
     */
    public boolean validateUserCredentials(String emailAddress, String password) {
        User user = userRepository.findUser(emailAddress, password).orElse(null);
        if (user != null)
            return true;

        return false;
    }

    /**
     * save user
     *
     * @param emailAddress
     * @param password
     * @return User
     */
    public User registerUser(String emailAddress, String password) {
        User user = new User(0, emailAddress, encoder.encode(password));
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

    /**
     * get user consumptions
     *
     * @param userId
     * @param consumptionDate
     * @return List<Consumption>
     */
    public List<Consumption> getUserConsumptions(int userId, Date consumptionDate) {
        return userRepository.getUserConsumptions(userId, consumptionDate);
    }

    /**
     * get user goals
     *
     * @param userId
     * @return List<Goal>
     */
    public List<Goal> getUserGoals(int userId) {
        return userRepository.getUserGoals(userId);
    }

    /**
     * get user exercise
     *
     * @param userId
     * @param exerciseDate
     * @return Exercise
     */
    public Exercise getUserExercise(int userId, Date exerciseDate) {
        return userRepository.getUserExercise(userId, exerciseDate);
    }
}
