import React, {Component} from "react";
import Highcharts from "react-highcharts";
import update from "immutability-helper";
import moment from "moment";

/**
 * line chart class
 */
class LineChart extends Component {

    /**
     * constructors
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            graphConfig: {
                title: {
                    text: "Exercise And Food Consumption"
                },
                xAxis: {
                    type: "datetime",
                    dateTimeLabelFormats: {
                        day: "%b %e"
                    }
                },
                yAxis: {
                    title: {
                        text: "Calories Burned/Consumed"
                    }
                },
                legend: {
                    layout: "vertical",
                    align: "right",
                    verticalAlign: "middle"
                },
                plotOptions: {
                    series: {
                        pointStart: Date.UTC(2010, 0, 1),
                        pointInterval: 24 * 3600 * 1000 //1 day
                    }
                },
                series: [{
                    name: "Calories Consumed",
                    data: [],
                    color: "#c22317"
                }, {
                    name: "Calories Burned",
                    data: [],
                    color: "#08ae23"
                }]
            }
        }
    }

    /**
     * initialize
     * @param nextProps
     * @param prevState
     * @returns {*}
     */
    static getDerivedStateFromProps(nextProps, prevState) {

        let {from, to, stats} = nextProps;
        let {caloriesConsumed, caloriesBurned} = LineChart.buildGraph(from, to, stats);

        return update(prevState, {
            graphConfig: {
                plotOptions: {
                    series: {
                        pointStart: {$set: LineChart.convertDate(from)}
                    }
                },
                series: {
                    0: {
                        data: {$set: caloriesConsumed}
                    },
                    1: {
                        data: {$set: caloriesBurned}
                    }
                }
            }
        });
    }

    /**
     * build graph data
     * @param dayFrom
     * @param dayTo
     * @param stats
     * @returns {{caloriesBurned: [number], caloriesConsumed: [number]}}
     */
    static buildGraph(dayFrom, dayTo, stats) {
        let days = moment(LineChart.convertDate(dayTo)).diff(moment(LineChart.convertDate(dayFrom)), 'days');

        let totalCaloriesConsumed = [0];
        let totalCaloriesBurned = [0];

        for (let i = 0; i <= days; i++) {
            let currentDay = moment(dayFrom).add(i, 'days').format('YYYY-MM-DD');
            let presentDayStat = stats.find(item => item.date === currentDay);

            totalCaloriesConsumed.push(presentDayStat ? presentDayStat.netCalories : 0);
            totalCaloriesBurned.push(presentDayStat ? presentDayStat.caloriesBurned : 0);
        }

        return {
            caloriesConsumed: totalCaloriesConsumed,
            caloriesBurned: totalCaloriesBurned
        };
    }

    /**
     * helper function to convert date to utc
     * @param date
     * @returns {moment.Moment}
     */
    static convertDate(date) {
        let processedDate = new Date(date.getTime());

        processedDate = moment(processedDate).hour(0).minute(0).second(0).millisecond(0).toDate();

        processedDate = new Date(processedDate - (60000 * new Date().getTimezoneOffset()));

        processedDate = moment(processedDate).subtract(1, 'days').toDate();

        return moment(processedDate).utc();
    }

    render() {
        return (
            <div>
                <Highcharts config={this.state.graphConfig}/>
            </div>);
    }
}

export default LineChart