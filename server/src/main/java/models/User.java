package models;

import javax.persistence.*;
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
    private String password;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creator", cascade = CascadeType.ALL)
    private List<FoodItem> userCreatedFoods;


    /**
     * get user Id
     *
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * getter for user created food items
     *
     * @return List<FoodItem>
     */
    public List<FoodItem> getUserCreatedFoods() {
        return userCreatedFoods;
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
                ", password='" + password + '\'' +
                ", userCreatedFoods=" + userCreatedFoods +
                '}';
    }
}