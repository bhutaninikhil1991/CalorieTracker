package models;

/**
 * class stores serving size i.e. cup, slice, oz
 */
public class ServingSize {
    private String label;
    private double ratio;

    /**
     * constructor
     *
     * @param label
     * @param gramWeight
     * @param quantity
     */
    public ServingSize(String label, double gramWeight, int quantity) {
        this.label = label;
        this.ratio = calculateRatio(gramWeight, quantity);
    }

    /**
     * getter for serving label
     *
     * @return String
     */
    public String getLabel() {
        return label;
    }

    /**
     * getter for ratio
     *
     * @return
     */
    public double getRatio() {
        return ratio;
    }

    /**
     * Nutrient information is provided per 100gms
     *
     * @param gramWeight
     * @param quantity
     * @return double
     */
    private double calculateRatio(double gramWeight, int quantity) {
        double perUnitGramWeight = gramWeight / quantity;
        return perUnitGramWeight / 100.0;
    }

    /**
     * override toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "ServingSize{" +
                "label='" + label + '\'' +
                ", ratio=" + ratio +
                '}';
    }

}
