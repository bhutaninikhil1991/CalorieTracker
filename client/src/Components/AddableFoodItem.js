import React, {Component} from 'react'
import update from "immutability-helper"
import {SERVER_URL} from "../config";
import ServingSize from "./ServingSize";
import ItemDeleteButton from "./ItemDeleteButton";
import plusIcon from "../resources/plus-icon.png";
import trashIcon from "../resources/trash-icon.png";

class AddableFoodItem extends Component {
    constructor(props) {
        super(props);
        this.state = {
            adding: false,
            deleting: false,
            loading: false,
            item: this.props.completedItem ? this.props.completedItem : undefined
        }
    }

    handleServingSizeChange(newServingSizeId) {
        let newServingSize = this.state.item.servingSizes.find(servingSize => servingSize.id === newServingSizeId);
        let newState = update(this.state, {
            item: {
                selectedServing: {
                    servingSize: {$set: newServingSize}
                }
            }
        });
        this.setState(newState);
    }

    handleQuantityChange(newServingQuantity) {
        let newState = update(this.state, {
            item: {
                selectedServing: {
                    quantity: {$set: newServingQuantity}
                }
            }
        });
        this.setState(newState);
    }

    handleItemClick() {
        if (!this.props.editMode) {
            if (!this.state.adding && !this.state.item) {
                this.setState({loading: true})
                fetch(`${SERVER_URL}` + "/api/foods/" + this.props.item.id)
                    .then((response) => response.json)
                    .then(item => {
                        let processedItem = this.setDefaultServingQuantity(item);
                        this.setState({
                            adding: true,
                            item: processedItem,
                            loading: false
                        })
                    });
            } else {
                this.setState(prevState => ({
                    adding: !prevState.adding
                }));
            }
        } else {
            this.setState(prevState => ({
                deleting: !prevState.deleting
            }));
        }
    }

    /**
     * set initial serving quantity to 100 gram for foods
     * whose only serving size is 1 gram
     * @param item
     * @returns {*}
     */
    setDefaultServingQuantity(item) {
        if (item.selectedServing.servingSize.label === 'g' && item.selectedServing.quantity === 1) {
            item.selectedServing.quantity = 100;
        }
        return item;
    }

    calculateItemTotals() {
        let item = this.state.item;
        let totalCalories, totalCarbohydrates, totalFat, totalProtein;
        totalCalories = totalCarbohydrates = totalFat = totalProtein = 0;
        let selectedServing = item.servingSizes[0];
        let servingSizeMultiplier = 1 * selectedServing.ratio;

        totalCalories += Math.round(item.calories * servingSizeMultiplier);
        totalCarbohydrates += Math.round(item.carbohydrates * servingSizeMultiplier);
        totalFat += Math.round(item.fat * servingSizeMultiplier);
        totalProtein += Math.round(item.protein * servingSizeMultiplier);

        return {
            calories: totalCalories,
            carbohydrates: totalCarbohydrates,
            fat: totalFat,
            protein: totalProtein
        };
    }

    deleteUserFoodItem() {
        this.props.deleteUserFoodItem(this.state.item.id);
    }

    render() {
        let foodName;
        if (this.props.completedItem) {
            foodName = this.state.item.name;
        } else if (this.props.item) {
            foodName = this.props.item.name;
        } else {
            foodName = 'Name'
        }

        let itemTotals;
        if (this.state.item) {
            itemTotals = this.calculateItemTotals();
        }

        let plusButtonImg;
        if (this.props.editMode)
            plusButtonImg = trashIcon;
        else
            plusButtonImg = plusIcon;

        return (
            <div
                className={'AddableFoodItem' + (this.state.adding ? ' adding' : '') + (this.state.deleting ? ' deleting' : '')}
                onClick={this.handleItemClick.bind(this)}>
                <img src={plusButtonImg} alt="Add" className="addable-item__plus"/>
                <span className="FoodItem__food">
                    <span className="FoodItem__food--name">{foodName}</span>
                </span>
                {this.state.item &&
                <span>
                    <span className="FoodItem__macros">
                        <span className="FoodItem__macros--carbs">{itemTotals.carbohydrates}</span>
                        <span className="FoodItem__macros--fat">{itemTotals.fat}</span>
                        <span className="FoodItem__macros--protein">{itemTotals.protein}</span>
                    </span>
                    <span className="FoodItem__calories">{itemTotals.calories}</span>
                </span>}

                <div className="clearfix"></div>

                {this.state.adding && this.state.item &&
                <ServingSize
                    selectedServing={this.state.item.selectedServing}
                    servingSizes={this.state.item.servingSizes}
                    itemId={this.state.item.id}
                    handleQuantityChange={this.handleQuantityChange.bind(this)}
                    handleSizeChange={this.handleServingSizeChange.bind(this)}
                />}

                {this.state.deleting && <ItemDeleteButton deleteItem={this.deleteUserFoodItem.bind(this)}/>}
            </div>);
    }
}

export default AddableFoodItem;