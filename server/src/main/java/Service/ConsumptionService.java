package Service;

import Repository.ConsumptionRepository;
import models.Consumption;
import models.Exercise;
import models.Goal;

import javax.inject.Singleton;

/**
 * consumption service
 */
@Singleton
public class ConsumptionService {
    private ConsumptionRepository consumptionRepository;

    /**
     * constructor
     *
     * @param consumptionRepository
     */
    public ConsumptionService(ConsumptionRepository consumptionRepository) {
        this.consumptionRepository = consumptionRepository;
    }

    /**
     * delete consumption by Id
     *
     * @param consumptionId
     */
    public void deleteConsumptionById(int consumptionId) {
        consumptionRepository.deleteById(consumptionId);
    }

    /**
     * get consumption by Id
     *
     * @param consumptionId
     * @return Consumption
     */
    public Consumption getConsumptionById(int consumptionId) {
        return consumptionRepository.findById(consumptionId).orElse(null);
    }

    /**
     * save consumption
     *
     * @param consumption
     * @return Consumption
     */
    public Consumption saveOrUpdateConsumption(Consumption consumption) {
        return consumptionRepository.saveOrUpdate(consumption);
    }

    /**
     * save or update goals
     *
     * @param goal
     * @return List<Goal>
     */
    public Goal saveOrUpdateGoal(Goal goal) {
        return consumptionRepository.saveOrUpdate(goal);
    }

    /**
     * save or update exercises
     *
     * @param exercise
     * @return Exercise
     */
    public Exercise saveOrUpdateExercise(Exercise exercise) {
        return consumptionRepository.saveOrUpdate(exercise);
    }

    /**
     * get exercise by id
     *
     * @param exerciseId
     * @return Exercise
     */
    public Exercise getExerciseById(int exerciseId) {
        return consumptionRepository.findByExerciseId(exerciseId).orElse(null);
    }
}
