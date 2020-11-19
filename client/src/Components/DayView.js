import React, {Component} from "react";
import {SERVER_URL} from "../config";
import update from "immutability-helper";
import Consumption from "./Consumption";
import DaySelect from "./DaySelect";
import ActivityInput from "./ActivityInput";
import NetCalories from "./NetCalories";
import {Link} from "react-router-dom";
import {getUserId} from "./Helpers";
import qs from "qs";

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
            goals: {
                calories: -1,
                carbohydrates: -1,
                fat: -1,
                protein: -1
            },
            loadingItems: true,
            removingItem: false
        }
    }

    /**
     * initialize the view
     */
    componentDidMount() {
        document.title = "Calorie App";
        const userId = getUserId();
        const qsParsed = qs.parse(this.props.location.search.slice(1));
        let day = qsParsed.day;
        if (day) {
            this.setState({
                selectedDay: this.convertStringToDate(day)
            });
        }
        let selectedDay = day ? this.convertStringToDate(day) : this.state.selectedDay
        this.getConsumptions(userId, selectedDay);
        this.getActivity(userId, selectedDay);
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
     * convert string to date
     * @param date
     * @returns {Date}
     */
    convertStringToDate(date) {
        let arr = date.split('-');
        let year = parseInt(arr[0]);
        let month = parseInt(arr[1]) - 1;
        let day = parseInt(arr[2]);
        return new Date(year, month, day);
    }

    /**
     * get users consumptions
     * @param day
     */
    getConsumptions(userId, day) {
        let date = day.toISOString().split('T')[0];
        fetch(`${SERVER_URL}/api/consumptions?userId=${userId}&consumptionDate=${date}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            }
        }).then((response) => {
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

    /**
     * get activity details
     * @param userId
     * @param day
     */
    getActivity(userId, day) {
        let date = day.toISOString().split('T')[0];
        fetch(`${SERVER_URL}/api/exercise?userId=${userId}&exerciseDate=${date}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            }
        }).then((response) => {
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

    /**
     * get goals
     * @param userId
     */
    getGoals(userId) {
        fetch(`${SERVER_URL}/api/goals?userId=${userId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            }
        }).then((response) => {
            if (response.ok) {
                return response.json()
                    .then(result => {
                        let items = result.success && result.data !== undefined ? result.data[userId] : [];
                        if (items.length <= 0)
                            return;
                        let goals = {};
                        items.forEach((item) => {
                            goals[item.goalCategory.toLowerCase()] = item.goalValue;
                        });
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
        fetch(`${SERVER_URL}/api/consumptions/delete/${consumptionId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            }
        })
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
        const userId = getUserId();
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
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            },
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
        const userId = getUserId();
        this.setState({
            selectedDay: newDay
        }, () => {
            this.getConsumptions(userId, newDay);
            this.getActivity(userId, newDay);
        })
    }

    /**
     * handle activity change
     * @param calories
     */
    handleActivityChange(calories) {
        let caloriesInt = parseInt(calories);
        this.setState({caloriesBurned: calories});
        const userId = getUserId();
        //to fix issue with multiple post request
        if (this.apiPostTimeout) {
            clearTimeout(this.apiPostTimeout);
        }
        this.apiPostTimeout = setTimeout(() => this.saveActivity(userId, caloriesInt), 500);
    }

    /**
     * save activity
     * @param userId
     * @param calories
     */
    saveActivity(userId, calories) {
        let calsBurned = calories ? calories : 0;
        const activityObj = {
            caloriesBurned: calsBurned,
            exerciseDate: this.state.selectedDay.toISOString().split('T')[0]
        }

        const reqObj = {
            activity: activityObj
        }

        fetch(`${SERVER_URL}/api/exercise/${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            },
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
        let caloriesGoalText;
        if (this.state.goals.calories > -1) {
            caloriesGoalText = (<span className="DayView__caloriesGoal">GOAL: {this.state.goals.calories}</span>);
        } else {
            caloriesGoalText = (
                <span className="DayView__caloriesGoal">You haven't <Link
                    to={"/goal"}>set your goals</Link> yet.</span>);
        }
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

                {caloriesGoalText}
            </div>
        );
    }
}

export default DayView;