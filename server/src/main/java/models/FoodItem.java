package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * class stores food item details
 */
@Entity
@Table(name = "food_item")
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double carbohydrates;
    private double fat;
    private double protein;
    private double calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User creator;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "food_item_id", referencedColumnName = "ID")
    private List<ServingSize> servingSizes = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.EAGER
    )
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "food_item_id", referencedColumnName = "ID")
    @JsonIgnore
    private List<Consumption> consumptions = new ArrayList<>();

    /**
     * constructor
     *
     * @param id
     * @param name
     * @param carbohydrates
     * @param fat
     * @param protein
     */
    public FoodItem(int id, String name, double carbohydrates, double fat, double protein) {
        this.id = id;
        this.name = name;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
        this.calories = 4 * carbohydrates + 9 * fat + 4 * protein;
    }

    /**
     * default constructor
     */
    public FoodItem() {

    }

    /**
     * getter for fdcId
     *
     * @return String
     */
    public int getId() {
        return id;
    }

    /**
     * getter for food description
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * getter for carbohydrates
     *
     * @return double
     */
    public double getCarbohydrates() {
        return carbohydrates;
    }

    /**
     * getter for fat
     *
     * @return double
     */
    public double getFat() {
        return fat;
    }

    /**
     * getter for protein
     *
     * @return double
     */
    public double getProtein() {
        return protein;
    }

    /**
     * getter for calories
     *
     * @return double
     */
    public double getCalories() {
        return Math.round(calories * 100.0) / 100.0;
    }

    /**
     * setter for user
     *
     * @param creator
     */
    public void setUser(User creator) {
        this.creator = creator;
    }

    /**
     * getter for serving size
     *
     * @return List<ServingSize>
     */
    public List<ServingSize> getServingSizes() {
        return Collections.unmodifiableList(servingSizes);
    }

    /**
     * add serving size to the list
     *
     * @param servingSize
     */
    public void addServingSize(ServingSize servingSize) {
        ServingSize newServingSize = new ServingSize(servingSize.getServingLabel(), servingSize.getServingAmount());
        servingSizes.add(newServingSize);
        servingSize.setFoodItem(this);
    }

    /**
     * override default toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", name:'" + name + '\'' +
                ", carbohydrates:" + carbohydrates +
                ", fat:" + fat +
                ", protein:" + protein +
                ", calories:" + calories +
                ", servingSizes:" + servingSizes +
                '}';
    }
}
