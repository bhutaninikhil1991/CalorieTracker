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
     * @param ratio
     */
    public ServingSize(String label, double ratio) {
        this.label = label;
        this.ratio = ratio;
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
     * @return double
     */
    public double getRatio() {
        return ratio;
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
