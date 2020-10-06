package Repository;

import io.micronaut.transaction.annotation.ReadOnly;
import models.FoodItem;
import models.User;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserRepositoryImpl implements UserRepository {
    private EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @ReadOnly
    public Optional<User> findByUserId(@NotNull Integer userId) {
        return Optional.ofNullable(entityManager.find(User.class, userId));
    }

    @Override
    @Transactional
    public User save(@NotNull User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    @Transactional
    public void deleteByUserId(@NotNull Integer userId) {
        findByUserId(userId).ifPresent(entityManager::remove);
    }

    @Override
    @ReadOnly
    public List<FoodItem> getUserFoodItems(@NotNull Integer userId) {
        User user = entityManager.find(User.class, userId);
        return user.getUserCreatedFoods();
    }
}