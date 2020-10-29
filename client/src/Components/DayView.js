import React, {Component} from "react";
import {SERVER_URL} from "../config";
import update from "immutability-helper";
import Consumption from "./Consumption";

class DayView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedDay: this.getTodaysDate(),
            items: [],
            loadingItems: true,
            removingItem: false
        }
    }

    componentDidMount() {
        document.title = "Calorie App";
        this.getConsumptions(this.state.selectedDay);
    }

    getTodaysDate() {
        return new Date("2020-10-26").toISOString().split('T')[0];
    }

    getConsumptions(day) {
        const userId = 1;

        fetch(`${SERVER_URL}` + "/api/consumptions?userId=" + userId + "&consumptionDate=" + day)
            .then((response) => {
                if (response.ok) {
                    return response.json()
                        .then(results => {
                            this.setState({
                                items: results.success ? results.data[userId] : [],
                                loadingItems: false
                            });
                        });
                } else {
                    alert("unable to load consumptions");
                }
            });
    }

    removeItem(consumptionId) {
        fetch(`${SERVER_URL}` + "/api/consumptions/delete" + consumptionId, {method: 'POST'})
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

    handleServingUpdate(updatedConsumption) {
        let consumption = this.state.items.find(consumption => consumption.id === updatedConsumption.id)
        let consumptionIndex = this.state.items.indexOf(consumption);
        fetch(`${SERVER_URL}` + "/api/consumptions/update" + updatedConsumption.id, {
            method: 'POST',
            body: JSON.stringify(updatedConsumption)
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


    changeSelectedDay(newDay) {
        this.setState({
            selectedDay: newDay,
        }, () => {
            this.getConsumptions(newDay);
        });
    }


    render() {
        return (
            <div className="DayView content-container">
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