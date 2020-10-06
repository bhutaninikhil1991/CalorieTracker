package models;

import javax.persistence.*;

/**
 * class stores serving size i.e. cup, slice, oz
 */
@Entity
@Table(name = "serving_size")
public class ServingSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String label;
    private double ratio;
    private double quantity;

    @ManyToOne
    private FoodItem item;

    /**
     * constructor
     *
     * @param label
     * @param quantity
     */
    public ServingSize(String label, double quantity) {
        this.label = label;
        this.quantity = quantity;
        this.ratio = 1.0 / quantity;
    }

    public ServingSize() {
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
     * getter for Id
     *
     * @return int
     */
    public int getId() {
        return id;
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
     * getter for foodItem
     *
     * @return FoodItem
     */
    public FoodItem getFoodItem() {
        return item;
    }

    /**
     * setter for food item
     *
     * @param foodItem
     */
    public void setFoodItem(FoodItem foodItem) {
        this.item = foodItem;
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
