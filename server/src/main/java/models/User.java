package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * class stores user data
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String emailAddress;
    @JsonIgnore
    private String password;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "creator",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    private List<FoodItem> userCreatedFoods;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "creator",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    private List<Consumption> userConsumptions;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "creator",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    private List<Goal> userGoals;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "creator",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    private List<Exercise> exercises;


    /**
     * get user Id
     *
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * getter for email Address
     *
     * @return String
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * getter for user created food items
     *
     * @return List<FoodItem>
     */
    public List<FoodItem> getUserCreatedFoods() {
        return Collections.unmodifiableList(userCreatedFoods);
    }

    /**
     * getter for user consumptions
     *
     * @return List<Consumption>
     */
    public List<Consumption> getUserConsumptions() {
        return Collections.unmodifiableList(userConsumptions);
    }

    /**
     * getter for user goals
     *
     * @return List<Goal>
     */
    public List<Goal> getUserGoals() {
        return Collections.unmodifiableList(userGoals);
    }

    /**
     * getter for user exercises
     *
     * @return List<Exercise>
     */
    public List<Exercise> getUserExercises() {
        return Collections.unmodifiableList(exercises);
    }

    /**
     * parameterized constructor
     *
     * @param id
     * @param emailAddress
     * @param password
     */
    public User(int id, String emailAddress, String password) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.password = password;
        userCreatedFoods = new ArrayList<>();
        userConsumptions = new ArrayList<>();
        userGoals = new ArrayList<>();
        exercises = new ArrayList<>();
    }

    /**
     * get user password
     *
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * default constructor
     */
    public User() {

    }

    /**
     * override default toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
