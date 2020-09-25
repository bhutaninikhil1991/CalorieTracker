package models;

import java.util.List;

/**
 * class stores food item details
 */
public class FoodItem {
    private int id;
    private String name;
    private double carbohydrates;
    private double fat;
    private double protein;
    private double calories;
    private List<ServingSize> servingSizes;

    /**
     * constructor
     *
     * @param id
     * @param name
     * @param carbohydrates
     * @param fat
     * @param protein
     * @param servingSizes
     */
    public FoodItem(int id, String name, double carbohydrates, double fat, double protein, List<ServingSize> servingSizes) {
        this.id = id;
        this.name = name;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
        this.calories = 4 * carbohydrates + 9 * fat + 4 * protein;
        this.servingSizes = servingSizes;
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
     * getter for serving size
     *
     * @return List<ServingSize>
     */
    public List<ServingSize> getServingSizes() {
        return servingSizes;
    }

    /**
     * override default toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "FoodItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", carbohydrates=" + carbohydrates +
                ", fat=" + fat +
                ", protein=" + protein +
                ", calories=" + calories +
                ", servingSizes=" + servingSizes +
                '}';
    }

}
