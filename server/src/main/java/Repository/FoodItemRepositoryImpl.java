package Repository;

import io.micronaut.transaction.annotation.ReadOnly;
import models.FoodItem;

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
