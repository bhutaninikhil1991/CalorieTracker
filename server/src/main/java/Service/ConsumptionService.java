package Service;

import Repository.ConsumptionRepository;
import models.Consumption;
import models.Exercise;
import models.Goal;

import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

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
     * get exercise details
     *
     * @param userId
     * @param exerciseDate
     * @return Exercise
     */
    public Exercise getExercise(int userId, Date exerciseDate) {
        return consumptionRepository.getExercise(userId, exerciseDate);
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

    /**
     * get consumption list
     *
     * @param userId
     * @param dateFrom
     * @param dateTo
     * @return List<Consumption>
     */
    public List<Consumption> getConsumptionList(int userId, Date dateFrom, Date dateTo) {
        return consumptionRepository.getConsumptionList(userId, dateFrom, dateTo);
    }

    /**
     * get exercise list
     *
     * @param userId
     * @param dateFrom
     * @param dateTo
     * @return List<Exercise>
     */
    public List<Exercise> getExerciseList(int userId, Date dateFrom, Date dateTo) {
        return consumptionRepository.getExerciseList(userId, dateFrom, dateTo);
    }
}
