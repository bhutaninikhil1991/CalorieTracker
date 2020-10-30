import React, {Component} from "react";
import update from "immutability-helper";
import carbsIcon from "../resources/bread-emoji.png";
import fatIcon from "../resources/bacon-strip-emoji.png";
import proteinIcon from "../resources/steak-emoji.png";
import ConsumptionItem from "./ConsumptionItem";
import AddFoodItem from "./AddFoodItem";

class Consumption extends Component {
    constructor(props) {
        super(props);
        this.state = {
            items: this.props.items,
            consumptionId: undefined
        }
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({items: nextProps.items});
    }

    handleSizeChange(newServingSizeId, consumptionId) {
        let consumption = this.state.items.find(item => item.id === consumptionId);
        let consumptionIndex = this.state.items.indexOf(consumption);
        let newServingSize = consumption.foodItem.servingSizes.find(servingSize => servingSize.id === newServingSizeId);

        let newState = update(this.state, {
            items: {
                [consumptionIndex]: {
                    selectedServing: {$set: newServingSize}
                }
            },
            consumptionId: {$set: consumptionId}
        });
        this.setState(newState);
    }

    handleQuantityChange(newServingQuantity, consumptionId) {
        let consumption = this.state.items.find(item => item.id === consumptionId);
        let consumptionIndex = this.state.items.indexOf(consumption);
        let newState = update(this.state, {
            items: {
                [consumptionIndex]: {
                    servingQuantity: {$set: newServingQuantity}
                }
            },
            consumptionId: {$set: consumptionId}
        });
        this.setState(newState);
    }

    handleNewServingSave() {
        if (this.state.items.length > 0 && this.state.consumptionId !== undefined) {
            let updatedConsumption = this.state.items.find(consumption => consumption.id === this.state.consumptionId)
            this.props.handleServingUpdate(updatedConsumption);
        }
    }

    handleItemRemove(consumptionId) {
        this.props.handleItemRemove(consumptionId);
    }

    calculateItemTotals() {
        let totalCals, totalCarbs, totalFat, totalProtein;
        totalCals = totalCarbs = totalFat = totalProtein = 0;
        this.state.items.forEach(item => {
            let servingSizeMultiplier = item.selectedServing.ratio * item.servingQuantity;

            totalCals += Math.round(item.foodItem.calories * servingSizeMultiplier);
            totalCarbs += Math.round(item.foodItem.carbohydrates * servingSizeMultiplier);
            totalFat += Math.round(item.foodItem.fat * servingSizeMultiplier);
            totalProtein += Math.round(item.foodItem.protein * servingSizeMultiplier);
        });

        return {
            calories: totalCals,
            carbs: totalCarbs,
            fat: totalFat,
            protein: totalProtein
        };
    }

    render() {
        let itemTotals = this.calculateItemTotals();

        let consumptions;
        if (this.state.items.length > 0) {
            consumptions = this.state.items.map(item => {
                let servingSizeMultiplier = item.selectedServing.ratio * item.servingQuantity;
                let nutritionValues = {
                    carbs: Math.round(item.foodItem.carbohydrates * servingSizeMultiplier),
                    fat: Math.round(item.foodItem.fat * servingSizeMultiplier),
                    protein: Math.round(item.foodItem.protein * servingSizeMultiplier),
                    calories: Math.round(item.foodItem.calories * servingSizeMultiplier)
                };

                return (
                    <ConsumptionItem
                        consumptionId={item.id}
                        foodItemId={item.foodItem.id}
                        name={item.foodItem.name}
                        nutritionValues={nutritionValues}
                        servingSizes={item.foodItem.servingSizes}
                        selectedServing={item.selectedServing}
                        servingQuantity={item.servingQuantity}
                        handleQuantityChange={this.handleQuantityChange.bind(this)}
                        handleSizeChange={this.handleSizeChange.bind(this)}
                        handleNewServingSave={this.handleNewServingSave.bind(this)}
                        handleItemRemove={this.handleItemRemove.bind(this)}
                        removingItem={this.props.removingItem}
                    />
                );
            });
        }

        return (
            <div className="Consumption">
                <div className="Consumption__header">
                    <span className="Consumption__header--name">Food Item</span>
                    <span className="Consumption__header--macros">
                        <img src={carbsIcon} alt="carbohydrates" title="carbohydrates"/>
                        <img src={fatIcon} alt="fat" title="fat"/>
                        <img src={proteinIcon} alt="protein" title="protein"/>
                    </span>
                    <span className="Consumption__header--caloriesTotal">
                        {itemTotals.calories ? itemTotals.calories : '--'}
                    </span>
                </div>
                {consumptions}
                <AddFoodItem day={this.props.day}/>
            </div>
        );
    }
}

export default Consumption;