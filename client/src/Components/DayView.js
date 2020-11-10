import React, {Component} from "react";
import {SERVER_URL} from "../config";
import update from "immutability-helper";
import Consumption from "./Consumption";
import DaySelect from "./DaySelect";
import ActivityInput from "./ActivityInput";
import NetCalories from "./NetCalories";

/**
 * class displays a particular consumption date view
 */
class DayView extends Component {
    /**
     * constructor
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            selectedDay: this.getTodaysDate(),
            items: [],
            caloriesBurned: undefined,
            goals: {},
            loadingItems: true,
            removingItem: false
        }
    }

    /**
     * initialize the view
     */
    componentDidMount() {
        document.title = "Calorie App";
        const userId = 1;
        this.getConsumptions(userId, this.state.selectedDay);
        this.getActivity(userId, this.state.selectedDay);
        this.getGoals(userId);
    }

    /**
     * get today's date
     * @returns {Date}
     */
    getTodaysDate() {
        return new Date(Date.now() - (60000 * new Date().getTimezoneOffset()));
    }

    /**
     * get users consumptions
     * @param day
     */
    getConsumptions(userId, day) {
        let date = day.toISOString().split('T')[0];
        fetch(`${SERVER_URL}/api/consumptions?userId=${userId}&consumptionDate=${date}`)
            .then((response) => {
                if (response.ok) {
                    return response.json()
                        .then(results => {
                            this.setState({
                                items: results.success && results.data !== undefined ? results.data[userId] : [],
                                loadingItems: false
                            });
                        });
                } else {
                    alert("unable to load consumptions");
                }
            });
    }

    getActivity(userId, day) {
        let date = day.toISOString().split('T')[0];
        fetch(`${SERVER_URL}/api/exercise?userId=${userId}&exerciseDate=${date}`)
            .then((response) => {
                if (response.ok) {
                    return response.json()
                        .then(result => {
                            let activity = result.success && result.data !== undefined ? result.data[userId] : [];
                            if (activity.caloriesBurned === 0) {
                                this.setState({caloriesBurned: undefined});
                            } else {
                                this.setState({caloriesBurned: activity.caloriesBurned});
                            }
                        });
                } else {
                    alert("unable to load exercises");
                }
            });
    }

    getGoals(userId) {
        fetch(`${SERVER_URL}/api/goals?userId=${userId}`)
            .then((response) => {
                if (response.ok) {
                    return response.json()
                        .then(result => {
                            let goals = result.success && result.data !== undefined ? result.data[userId] : [];
                            this.setState({goals: goals});
                        });
                }
            });
    }

    /**
     * delete user consumption
     * @param consumptionId
     */
    removeItem(consumptionId) {
        fetch(`${SERVER_URL}/api/consumptions/delete/${consumptionId}`, {method: 'POST'})
            .then(response => {
                if (response.ok) {
                    let item = this.state.items.find(consumption => consumption.id === consumptionId);
                    let itemIndex = this.state.items.indexOf(item);
                    let newState = update(this.state, {
                        items: {$splice: [[itemIndex, 1]]}
                    });
                    this.setState(newState);
                } else {
                    alert("Consumption item cannot be deleted");
                }
            });
    }

    /**
     * update user consumption
     * @param updatedConsumption
     */
    handleServingUpdate(updatedConsumption) {
        const userId = 1;
        let consumption = this.state.items.find(consumption => consumption.id === updatedConsumption.id)
        let consumptionIndex = this.state.items.indexOf(consumption);

        let updatedConsumptionObj = JSON.parse(JSON.stringify(updatedConsumption));
        updatedConsumptionObj.userId = userId;
        updatedConsumptionObj.selectedServing = {
            "servingSize": updatedConsumption.selectedServing,
            "quantity": updatedConsumption.servingQuantity
        }

        const reqObj = {
            consumption: updatedConsumptionObj
        }

        fetch(`${SERVER_URL}/api/consumptions/update`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(reqObj)
        }).then(response => {
            if (response.ok) {
                let newState = update(this.state, {
                    items: {
                        [consumptionIndex]: {$set: updatedConsumption}
                    }
                });
                this.setState(newState);
            } else {
                alert("There is a problem updating record");
            }
        });
    }

    /**
     * handle selected day change event
     * @param newDay
     */
    changeSelectedDay(newDay) {
        const userId = 1;
        this.setState({
            selectedDay: newDay
        }, () => {
            this.getConsumptions(userId, newDay);
            this.getActivity(userId, newDay);
        })
    }

    handleActivityChange(calories) {
        let caloriesInt = parseInt(calories);
        this.setState({caloriesBurned: calories});
        const userId = 1;
        this.saveActivity(userId, caloriesInt);
    }

    saveActivity(userId, calories) {
        let calsBurned = calories ? calories : 0;
        const activityObj = {
            caloriesBurned: calsBurned,
            exerciseDate: this.state.selectedDay
        }

        const reqObj = {
            activity: activityObj
        }

        fetch(`${SERVER_URL}/api/exercise/${userId}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(reqObj)
        }).then(response => {
            if (response.ok) {

            } else {
                alert("There is a problem updating record");
            }
        });
    }

    /**
     * calculate total
     * @returns {{carbs: number, protein: number, fat: number, caloriesEaten: number, netCalories: number}}
     */
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

        // calculate net calories consumption - activity
        let netCalories = totalCals - (this.state.caloriesBurned ? this.state.caloriesBurned : 0);

        return {
            caloriesEaten: totalCals,
            netCalories: netCalories,
            carbs: totalCarbs,
            fat: totalFat,
            protein: totalProtein
        };
    }

    render() {
        let dayTotals = this.calculateItemTotals();
        return (
            <div className="DayView content-container">
                <DaySelect
                    selectedDay={this.state.selectedDay}
                    changeSelectedDay={this.changeSelectedDay.bind(this)}
                    todaysDate={this.getTodaysDate()}/>
                <div className="clearfix"/>
                <Consumption
                    items={this.state.items}
                    handleServingUpdate={this.handleServingUpdate.bind(this)}
                    handleItemRemove={this.removeItem.bind(this)}
                    day={this.state.selectedDay}
                    removingItem={this.state.removingItem}
                />
                <ActivityInput caloriesBurned={this.state.caloriesBurned}
                               handleActivityChange={this.handleActivityChange.bind(this)}/>
                <NetCalories
                    caloriesEaten={dayTotals.caloriesEaten}
                    caloriesBurned={this.state.caloriesBurned}
                    netCalories={dayTotals.netCalories}
                    caloriesGoal={this.state.goals.calories}
                />
            </div>
        );
    }
}

export default DayView;