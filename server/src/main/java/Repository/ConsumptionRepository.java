package Repository;

import models.Consumption;
import models.Exercise;
import models.Goal;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface ConsumptionRepository {
    Optional<Consumption> findById(@NotNull Integer id);

    Optional<Exercise> findByExerciseId(@NotNull Integer id);

    Consumption saveOrUpdate(@NotNull Consumption consumption);

    void deleteById(@NotNull Integer id);

    Goal saveOrUpdate(@NotNull Goal goal);

    Exercise saveOrUpdate(@NotNull Exercise exercise);
}
