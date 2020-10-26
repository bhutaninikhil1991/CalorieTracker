package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "consumption")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User creator;

    @ManyToOne(fetch = FetchType.EAGER)
    private FoodItem foodItem;

    @ManyToOne(fetch = FetchType.EAGER)
    private ServingSize selectedServingSize;

    private double servingQuantity;

    private LocalDate consumptionDate;

    /**
     * default constructor
     */
    public Consumption() {

    }

    /**
     * set user
     *
     * @param user
     */
    public void setUser(User user) {
        this.creator = user;
    }

    /**
     * set foodItem
     *
     * @param foodItem
     */
    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    /**
     * set serving size
     *
     * @param servingSize
     */
    public void setSelectedServingSize(ServingSize servingSize) {
        this.selectedServingSize = servingSize;
    }

    /**
     * set serving quantity
     *
     * @param servingQuantity
     */
    public void setServingQuantity(double servingQuantity) {
        this.servingQuantity = servingQuantity;
    }

    /**
     * set consumption date
     *
     * @param consumptionDate
     */
    public void setConsumptionDate(LocalDate consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    /**
     * getter for id
     *
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * getter for creator
     *
     * @return User
     */
    public User getCreator() {
        return creator;
    }

    /**
     * getter for foodItem
     *
     * @return FoodItem
     */
    public FoodItem getFoodItem() {
        return foodItem;
    }

    /**
     * getter for selected serving size
     *
     * @return Serving Size
     */
    public ServingSize getSelectedServingSize() {
        return selectedServingSize;
    }

    /**
     * getter for quantity
     *
     * @return double
     */
    public double getServingQuantity() {
        return servingQuantity;
    }

    /**
     * getter for consumption date
     *
     * @return LocalDate
     */
    public String getConsumptionDate() {
        return consumptionDate.toString();
    }

    /**
     * override default toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Consumption{" +
                "id=" + id +
                ", user=" + creator +
                ", foodItem=" + foodItem +
                ", servingSize=" + selectedServingSize +
                ", servingQuantity=" + servingQuantity +
                ", consumptionDate=" + consumptionDate +
                '}';
    }

    /**
     * override equals method
     *
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consumption that = (Consumption) o;
        return id == that.id;
    }

    /**
     * override hash code
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}