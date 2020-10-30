package Repository;

import models.FoodItem;
import models.ServingSize;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface FoodItemRepository {
    Optional<FoodItem> findById(@NotNull Integer id);

    Optional<Integer> findServingSizeId(@NotNull FoodItem foodItem, @NotNull String servingLabel);

    Optional<ServingSize> findByServingId(@NotNull Integer servingSizeId);

    FoodItem save(@NotNull FoodItem item);

    void deleteById(@NotNull Integer id);
}
