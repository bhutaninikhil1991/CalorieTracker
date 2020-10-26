package Repository;

import models.FoodItem;
import models.ServingSize;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface FoodItemRepository {
    Optional<FoodItem> findById(@NotNull Integer id);

    Optional<ServingSize> findByServingId(@NotNull Integer foodItemId, @NotNull Integer servingSizeId);

    Optional<ServingSize> findByServingId(@NotNull Integer servingSizeId);

    FoodItem save(@NotNull FoodItem item);

    void deleteById(@NotNull Integer id);
}
