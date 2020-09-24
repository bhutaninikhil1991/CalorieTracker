package models;

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
        return calories;
    }

    /**
     * override toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "FoodItem{" +
                "fdcID='" + id + '\'' +
                ", description='" + name + '\'' +
                ", carbohydrates=" + carbohydrates +
                ", fat=" + fat +
                ", protein=" + protein +
                ", calories=" + calories +
                '}';
    }


}
