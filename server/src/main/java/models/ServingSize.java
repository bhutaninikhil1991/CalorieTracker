package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class stores serving size i.e. cup, slice, oz
 */
@Entity
@Table(name = "serving_size")
public class ServingSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String servingLabel;
    private double ratio;
    private double servingAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private FoodItem foodItem;

    @OneToMany(
            fetch = FetchType.EAGER
    )
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "selected_serving_id", referencedColumnName = "ID")
    @JsonIgnore
    private List<Consumption> consumptions = new ArrayList<>();

    /**
     * constructor
     *
     * @param servingLabel
     * @param servingAmount
     */
    public ServingSize(String servingLabel, double servingAmount) {
        this.servingLabel = servingLabel;
        this.servingAmount = servingAmount;
        this.ratio = 1.0 / servingAmount;
    }

    /**
     * parameterized constructor
     *
     * @param id
     * @param servingLabel
     * @param servingAmount
     */
    public ServingSize(Integer id, String servingLabel, double servingAmount) {
        this.id = id;
        this.servingLabel = servingLabel;
        this.servingAmount = servingAmount;
        this.ratio = 1.0 / servingAmount;
    }

    /**
     * default constructor
     */
    public ServingSize() {
    }

    /**
     * getter for serving label
     *
     * @return String
     */
    public String getServingLabel() {
        return servingLabel;
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
    public double getServingAmount() {
        return this.servingAmount;
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
     * getter for food Item
     *
     * @return Food Item
     */
    public FoodItem getFoodItem() {
        return foodItem;
    }

    /**
     * setter for Food Item
     *
     * @param item
     */
    public void setFoodItem(FoodItem item) {
        this.foodItem = item;
    }

    /**
     * override toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                "label='" + servingLabel + '\'' +
                ", amount='" + servingAmount + '\'' +
                ", ratio=" + ratio +
                '}';
    }

}
