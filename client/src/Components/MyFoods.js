import React, {Component} from 'react'
import FoodItem from "./FoodItem";

class MyFoods extends Component {

    componentDidMount() {
        this.props.getFoods();
    }

    deleteUserFoodItem(foodItemId) {
        this.props.deleteUserFoodItem(foodItemId);
    }

    render() {
        let foods;
        if (this.props.foods.length > 0) {
            console.log("hey")
            console.log(this.props.foods);
            foods = this.props.foods.map(food => {
                return (
                    <FoodItem
                        key={food.id}
                        completedItem={food}
                        editMode={this.props.editMode}
                        deleteUserFoodItem={this.deleteUserFoodItem.bind(this)}
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