package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Date;
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
    @JoinColumn(referencedColumnName = "ID")
    private FoodItem foodItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "ID")
    private ServingSize selectedServing;

    private double servingQuantity;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date consumptionDate;

    /**
     * default constructor
     */
    public Consumption() {

    }

    /**
     * parameterized constructor
     *
     * @param user
     * @param foodItem
     * @param selectedServing
     * @param servingQuantity
     * @param consumptionDate
     */
    public Consumption(User user, FoodItem foodItem, ServingSize selectedServing, double servingQuantity, Date consumptionDate) {
        this.creator = user;
        this.foodItem = foodItem;
        this.selectedServing = selectedServing;
        this.servingQuantity = servingQuantity;
        this.consumptionDate = consumptionDate;
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
    public void setSelectedServing(ServingSize servingSize) {
        this.selectedServing = servingSize;
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
    public void setConsumptionDate(Date consumptionDate) {
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
    public ServingSize getSelectedServing() {
        return selectedServing;
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
     * @return Date
     */
    public Date getConsumptionDate() {
        return consumptionDate;
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
                ", servingSize=" + selectedServing +
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

    /**
     * get category value
     *
     * @param category
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public long calculateCategoryValue(Goal.GoalCategory category) throws NoSuchFieldException, IllegalAccessException {
        Field field = FoodItem.class.getDeclaredField(category.toString().toLowerCase());
        field.setAccessible(true);
        double value = field.getDouble(this.foodItem);
        return Math.round(value * this.servingQuantity * this.selectedServing.getRatio());
    }

}
