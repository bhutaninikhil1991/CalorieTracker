import React, {Component} from 'react'
import update from "immutability-helper"
import {SERVER_URL} from "../config";
import ServingSize from "./ServingSize";
import ItemDeleteButton from "./ItemDeleteButton";
import plusIcon from "../resources/plus-icon.png";
import trashIcon from "../resources/trash-icon.png";

/**
 * food item class
 */
class AddableFoodItem extends Component {
    /**
     * constructor
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            adding: false,
            deleting: false,
            loading: false,
            item: this.props.completedItem ? this.props.completedItem : undefined,
            selectedServing: {
                servingSize: this.props.selectedServing,
                quantity: 1
            }
        }
    }

    /**
     * handle serving size change
     * @param newServingSizeId
     */
    handleServingSizeChange(newServingSizeId) {
        let newServingSize = this.state.item.servingSizes.find(servingSize => servingSize.id === newServingSizeId);
        let newState = update(this.state, {
            selectedServing: {
                servingSize: {$set: newServingSize}
            }
        });
        this.setState(newState);
    }

    /**
     * handle serving quantity change
     * @param newServingQuantity
     */
    handleQuantityChange(newServingQuantity) {
        let newState = update(this.state, {
            selectedServing: {
                quantity: {$set: newServingQuantity}
            }
        });
        this.setState(newState);
    }

    /**
     * get food Items by there fdcId
     */
    handleItemClick() {
        if (!this.props.editMode) {
            if (!this.state.adding && !this.state.item) {
                this.setState({loading: true});
                fetch(`${SERVER_URL}/api/foods?fdcId=${this.props.item.id}`)
                    .then((response) => {
                        if (response.ok) {
                            response.json()
                                .then(item => {
                                    let processedItem = item.success ? this.setDefaultServing(item.data[this.props.item.id]) : null;
                                    this.setState({
                                        adding: true,
                                        item: processedItem,
                                        loading: false
                                    });
                                });
                        }
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
     * set default values for initial serving
     * @param item
     * @returns {*}
     */
    setDefaultServing(item) {
        //update selected serving
        let newState = update(this.state, {
            selectedServing: {
                servingSize: {$set: item.servingSizes[0]},
                quantity: {$set: 1}
            }
        });
        this.setState(newState);
        return item;
    }

    /**
     * calculate total
     * @returns {{carbohydrates: number, protein: number, fat: number, calories: number}}
     */
    calculateItemTotals() {
        let item = this.state.item;
        let totalCalories, totalCarbohydrates, totalFat, totalProtein;
        totalCalories = totalCarbohydrates = totalFat = totalProtein = 0;
        let servingSizeMultiplier = this.state.selectedServing.quantity * this.state.selectedServing.servingSize.ratio;

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

    /**
     * delete user created food item
     */
    deleteUserFoodItem() {
        this.props.deleteUserFoodItem(this.state.item.id);
    }

    /**
     * add consumption
     */
    addConsumption() {
        const userId = 1;
        let consumption = {
            foodItem: JSON.parse(JSON.stringify(this.state.item)),
            selectedServing: JSON.parse(JSON.stringify(this.state.selectedServing)),
            userId: userId,
            consumptionDate: this.props.day
        };

        const reqObj = {
            consumption: consumption
        };

        fetch(`${SERVER_URL}/api/consumptions`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(reqObj)
        }).then(response => {
            if (response.ok) {
                window.location = "/?day=" + this.props.day;
            } else {
                alert("there was problem adding food to your log");
            }
        })
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
                    selectedServing={this.state.selectedServing}
                    servingSizes={this.state.item.servingSizes}
                    itemId={this.state.item.id}
                    handleAddClick={this.addConsumption.bind(this)}
                    handleQuantityChange={this.handleQuantityChange.bind(this)}
                    handleSizeChange={this.handleServingSizeChange.bind(this)}
                    showAddRemoveButtons
                />}

                {this.state.deleting && <ItemDeleteButton deleteItem={this.deleteUserFoodItem.bind(this)}/>}
            </div>);
    }
}

export default AddableFoodItem;