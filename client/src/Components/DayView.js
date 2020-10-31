import React, {Component} from "react";
import {SERVER_URL} from "../config";
import update from "immutability-helper";
import Consumption from "./Consumption";
import DaySelect from "./DaySelect";

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
            loadingItems: true,
            removingItem: false
        }
    }

    /**
     * initialize the view
     */
    componentDidMount() {
        document.title = "Calorie App";
        this.getConsumptions(this.state.selectedDay);
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
    getConsumptions(day) {
        const userId = 1;
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
        this.setState({
            selectedDay: newDay
        }, () => {
            this.getConsumptions(newDay);
        })
    }

    render() {
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
            </div>
        );
    }
}

export default DayView;