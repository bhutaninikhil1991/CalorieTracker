package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User creator;

    private int caloriesBurned;

    private LocalDate exerciseDate;

    /**
     * constructor
     */
    public Exercise() {

    }

    /**
     * constructor
     *
     * @param user
     * @param caloriesBurned
     * @param exerciseDate
     */
    public Exercise(User user, int caloriesBurned, LocalDate exerciseDate) {
        this.creator = user;
        this.caloriesBurned = caloriesBurned;
        this.exerciseDate = exerciseDate;
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
     * getter for calories burned
     *
     * @return int
     */
    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    /**
     * getter for exercise date
     *
     * @return String
     */
    public String getExerciseDate() {
        return exerciseDate.toString();
    }

    /**
     * setter for calories burned
     *
     * @param caloriesBurned
     */
    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    /**
     * override default toString method
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", creator=" + creator +
                ", caloriesBurned=" + caloriesBurned +
                ", exerciseDate=" + exerciseDate +
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
        Exercise exercise = (Exercise) o;
        return id == exercise.id;
    }

    /**
     * override default hash code
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
