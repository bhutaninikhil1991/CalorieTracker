import React, {Component} from "react";
import moment from "moment";
import DayPickerInput from "react-day-picker/DayPickerInput";
import refreshIcon from "../resources/refresh-icon.png";
import refreshIconInactive from "../resources/refresh-icon-inactive.png";
import {SERVER_URL} from "../config";
import {getUserId} from "./Helpers";
import StatsTable from "./StatsTable";
import LineChart from "./LineChart";

/**
 * statistics class
 */
class StatisticsView extends Component {

    /**
     * constructor
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            dayFrom: moment(new Date(Date.now() - (60000 * new Date().getTimezoneOffset()))).subtract(1, 'weeks').toDate(),
            dayTo: new Date(Date.now() - (60000 * new Date().getTimezoneOffset())),
            refreshButtonActive: false,
            stats: undefined
        }
    }

    /**
     * handle from day change event
     * @param from
     */
    handleFromDayChange(from) {
        this.setState({
            dayFrom: from,
            refreshButtonActive: true
        });
    }

    /**
     * handle to day change event
     * @param to
     */
    handleToDayChange(to) {
        this.setState({
            dayTo: to,
            refreshButtonActive: true
        });
    }

    /**
     * load statistics
     */
    refreshStats() {
        const userId = getUserId();
        fetch(`${SERVER_URL}/api/stats?userId=${userId}&dateFrom=${this.state.dayFrom.toISOString().split('T')[0]}&dateTo=${this.state.dayTo.toISOString().split('T')[0]}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            }
        }).then((response) => {
            if (response.ok) {
                return response.json().then(result => {
                    let item = result.success && result.data !== undefined ? result.data[userId] : []
                    let obj = JSON.parse(item)
                    this.setState({
                        stats: obj["statistics"]
                    });
                });
            }
        })
    }

    render() {
        let refreshButton;
        if (this.state.refreshButtonActive) {
            refreshButton = (
                <img className="StatisticsView__refresh-button" src={refreshIcon} onClick={this.refreshStats.bind(this)}
                     alt="Refresh"/>);
        } else {
            refreshButton = (
                <img className="StatisticsView__refresh-button inactive" src={refreshIconInactive} alt=""/>);
        }

        let statsComponent;
        if (this.state.stats) {
            if (this.state.stats.length > 0) {
                statsComponent = (
                    <div>
                        <StatsTable stats={this.state.stats}/>
                        <LineChart from={this.state.dayFrom} to={this.state.dayTo} stats={this.state.stats}/>
                    </div>
                );
            } else {
                statsComponent = (<span className="StatisticsView__no-consumption">
                    Looks like you haven't consumed anything during this time period.
                </span>)
            }
        }

        return (
            <div className="StatisticsView content-container">
                <h1 className="page-title">Nutrition Details:</h1>
                <div className="StatisticsView__week-select">
                    From <DayPickerInput value={this.state.dayFrom}
                                         onDayChange={this.handleFromDayChange.bind(this)}/>
                    To <DayPickerInput value={this.state.dayTo} onDayChange={this.handleToDayChange.bind(this)}/>
                    {refreshButton}
                </div>
                <div className="clearfix"/>

                {this.state.stats && statsComponent}
            </div>
        );
    }
}

export default StatisticsView;