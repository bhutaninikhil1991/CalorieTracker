package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "goal")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User creator;

    @Enumerated(EnumType.ORDINAL)
    private GoalCategory goalCategory;

    /**
     * GoalCategory enum
     */
    public enum GoalCategory {
        CALORIES,
        CARBOHYDRATES,
        FAT,
        PROTEIN
    }

    private int goalValue;

    /**
     * default constructor
     */
    public Goal() {
    }

    /**
     * constructor
     *
     * @param user
     * @param category
     * @param goalValue
     */
    public Goal(User user, GoalCategory category, int goalValue) {
        this.creator = user;
        this.goalCategory = category;
        this.goalValue = goalValue;
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
     * getter for category
     *
     * @return GoalCategory
     */
    public GoalCategory getGoalCategory() {
        return goalCategory;
    }

    /**
     * getter for goal value
     *
     * @return int
     */
    public int getGoalValue() {
        return goalValue;
    }

    /**
     * setter for goal value
     *
     * @param value
     */
    public void setGoalValue(int value) {
        this.goalValue = value;
    }

    /**
     * override default toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", creator=" + creator +
                ", goalCategory=" + goalCategory +
                ", goalValue=" + goalValue +
                '}';
    }

    /**
     * override default equals method
     *
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return id == goal.id;
    }

    /**
     * override hashcode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
