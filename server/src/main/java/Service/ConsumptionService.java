package Service;

import Repository.ConsumptionRepository;
import models.Consumption;

import javax.inject.Singleton;

@Singleton
public class ConsumptionService {
    private ConsumptionRepository consumptionRepository;

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
}
