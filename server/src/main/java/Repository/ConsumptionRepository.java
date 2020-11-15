package Repository;

import models.Consumption;
import models.Exercise;
import models.Goal;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ConsumptionRepository {
    Optional<Consumption> findById(@NotNull Integer id);

    Optional<Exercise> findByExerciseId(@NotNull Integer id);

    Consumption saveOrUpdate(@NotNull Consumption consumption);

    void deleteById(@NotNull Integer id);

    Goal saveOrUpdate(@NotNull Goal goal);

    Exercise saveOrUpdate(@NotNull Exercise exercise);

    List<Consumption> getConsumptionList(@NotNull Integer userId, Date dateFrom, Date dateTo);

    List<Exercise> getExerciseList(@NotNull Integer userId, Date dateFrom, Date dateTo);
}
