package Repository;

import models.Consumption;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface ConsumptionRepository {
    Optional<Consumption> findById(@NotNull Integer id);

    Consumption save(@NotNull Consumption consumption);

    void deleteById(@NotNull Integer id);
}
