import React, {Component} from 'react'
import AddableFoodItem from "./AddableFoodItem";

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
            foods = this.props.foods.map(food => {
                return (
                    <AddableFoodItem
                        selectedServing={food.servingSizes[0]}
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