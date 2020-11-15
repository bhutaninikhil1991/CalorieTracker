package Repository;

import models.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserId(@NotNull Integer userId);

    User findUserByEmailAddress(String emailAddress);

    User save(@NotNull User user);

    void deleteByUserId(@NotNull Integer userId);

    Optional<User> findUser(String emailAddress, String password);

    List<FoodItem> getUserFoodItems(@NotNull Integer userId);

    List<Consumption> getUserConsumptions(@NotNull Integer userId, Date consumptionDate);

    List<Goal> getUserGoals(@NotNull Integer userId);

    Exercise getUserExercise(@NotNull Integer userId, Date exerciseDate);
}
