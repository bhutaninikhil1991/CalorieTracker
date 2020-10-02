package models;

/**
 * class stores serving size i.e. cup, slice, oz
 */
public class ServingSize {
    private String label;
    private double ratio;
    private double quantity;

    /**
     * constructor
     *
     * @param label
     * @param quantity
     */
    public ServingSize(String label, double quantity) {
        this.label = label;
        this.quantity = quantity;
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
     * getter for quantity
     *
     * @return double
     */
    public double getQuantity() {
        return this.quantity;
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
     * setter for ratio
     *
     * @param ratio
     */
    public void setRatio(double ratio) {
        this.ratio = ratio;
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
