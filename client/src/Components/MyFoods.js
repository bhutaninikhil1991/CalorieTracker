import React, {Component} from 'react';
import AddableFoodItem from "./AddableFoodItem";

/**
 * class responsible for viewing user created food items
 */
class MyFoods extends Component {

    /**
     * initialization of view
     */
    componentDidMount() {
        this.props.getFoods();
    }

    /**
     * handle delete user food item event
     * @param foodItemId
     */
    deleteUserFoodItem(foodItemId) {
        this.props.deleteUserFoodItem(foodItemId);
    }

    render() {
        let foods;
        if (this.props.foods.length > 0) {
            foods = this.props.foods.map(food => {
                return (
                    <AddableFoodItem
                        selectedServing={food.servingSizes[0]}
                        key={food.id}
                        completedItem={food}
                        editMode={this.props.editMode}
                        deleteUserFoodItem={this.deleteUserFoodItem.bind(this)}
                        day={this.props.day}
                    />
                );
            });
        } else {
            foods = (
                <span className="MyFoods__no-foods">Looks like you haven't created any food</span>
            );
        }
        return (
            <div className="MyFoods">
                {foods}
            </div>
        );
    }
}

export default MyFoods