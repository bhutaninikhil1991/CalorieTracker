package Repository;

import models.FoodItem;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface FoodItemRepository {
    Optional<FoodItem> findById(@NotNull Integer id);

    FoodItem save(@NotNull FoodItem item);
}
