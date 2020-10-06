package Repository;

import models.FoodItem;
import models.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserId(@NotNull Integer userId);

    User save(@NotNull User user);

    void deleteByUserId(@NotNull Integer userId);

    List<FoodItem> getUserFoodItems(@NotNull Integer userId);
}
