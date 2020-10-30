package Repository;

import io.micronaut.transaction.annotation.ReadOnly;
import models.FoodItem;
import models.ServingSize;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Singleton
public class FoodItemRepositoryImpl implements FoodItemRepository {
    private EntityManager entityManager;

    public FoodItemRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @ReadOnly
    public Optional<FoodItem> findById(@NotNull Integer id) {
        return Optional.ofNullable(entityManager.find(FoodItem.class, id));
    }

    @Override
    @ReadOnly
    public Optional<Integer> findServingSizeId(@NotNull FoodItem foodItem, @NotNull String servingLabel) {
        ServingSize servingSize = (ServingSize) entityManager.createQuery("SELECT s FROM ServingSize s WHERE s.foodItem.id =: foodItemId and s.servingLabel =: servingLabel")
                .setParameter("servingLabel", servingLabel)
                .setParameter("foodItemId", foodItem.getId())
                .getSingleResult();
        return Optional.ofNullable(servingSize.getId());
    }

    @Override
    @ReadOnly
    public Optional<ServingSize> findByServingId(@NotNull Integer servingSizeId) {
        return Optional.ofNullable(entityManager.find(ServingSize.class, servingSizeId));
    }

    @Override
    @Transactional
    public FoodItem save(@NotNull FoodItem item) {
        entityManager.persist(item);
        return item;
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Integer id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
