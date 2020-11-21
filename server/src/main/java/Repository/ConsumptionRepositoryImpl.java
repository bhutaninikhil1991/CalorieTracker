package Repository;

import io.micronaut.transaction.annotation.ReadOnly;
import models.Consumption;
import models.Exercise;
import models.Goal;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
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
    @ReadOnly
    public Optional<Exercise> findByExerciseId(@NotNull Integer id) {
        return Optional.ofNullable(entityManager.find(Exercise.class, id));
    }

    @Override
    @Transactional
    public Consumption saveOrUpdate(@NotNull Consumption consumption) {
        if (consumption.getId() > 0) {
            entityManager.merge(consumption);
        } else {
            entityManager.persist(consumption);
        }
        return consumption;
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Integer id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Override
    @Transactional
    public Goal saveOrUpdate(@NotNull Goal goal) {
        if (goal.getId() > 0) {
            entityManager.merge(goal);
        } else {
            entityManager.persist(goal);
        }
        return goal;
    }

    @Override
    @Transactional
    public Exercise saveOrUpdate(@NotNull Exercise exercise) {
        if (exercise.getId() > 0) {
            entityManager.merge(exercise);
        } else {
            entityManager.persist(exercise);
        }
        return exercise;
    }

    @Override
    @ReadOnly
    public List<Consumption> getConsumptionList(@NotNull Integer userId, Date dateFrom, Date dateTo) {
        return entityManager.createQuery("SELECT c FROM Consumption AS c WHERE c.creator.id =: creator_id and c.consumptionDate BETWEEN :from_date and :to_date ORDER BY c.consumptionDate DESC ", Consumption.class)
                .setParameter("creator_id", userId)
                .setParameter("from_date", dateFrom)
                .setParameter("to_date", dateTo)
                .getResultList();
    }

    @Override
    @ReadOnly
    public List<Exercise> getExerciseList(@NotNull Integer userId, Date dateFrom, Date dateTo) {
        return entityManager.createQuery("SELECT e FROM Exercise AS e WHERE e.creator.id =: creator_id and e.exerciseDate BETWEEN :from_date and :to_date ORDER BY e.exerciseDate DESC ", Exercise.class)
                .setParameter("creator_id", userId)
                .setParameter("from_date", dateFrom)
                .setParameter("to_date", dateTo)
                .getResultList();
    }

    @Override
    @ReadOnly
    public Exercise getExercise(@NotNull Integer userId, Date exerciseDate) {
        try {
            Exercise exercise = entityManager.createQuery("SELECT e FROM Exercise AS e WHERE e.creator.id =: creator_id and e.exerciseDate =: exerciseDate", Exercise.class)
                    .setParameter("creator_id", userId)
                    .setParameter("exerciseDate", exerciseDate)
                    .getSingleResult();
            return exercise;
        } catch (Exception ex) {
            return null;
        }
    }
}
