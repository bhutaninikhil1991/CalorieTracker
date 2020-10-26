package Repository;

import io.micronaut.transaction.annotation.ReadOnly;
import models.Consumption;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Singleton
public class ConsumptionRepositoryImpl implements ConsumptionRepository {
    private EntityManager entityManager;

    public ConsumptionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    @ReadOnly
    public Optional<Consumption> findById(@NotNull Integer id) {
        return Optional.ofNullable(entityManager.find(Consumption.class, id));
    }

    @Override
    @Transactional
    public Consumption save(@NotNull Consumption consumption) {
        entityManager.persist(consumption);
        return consumption;
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Integer id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
