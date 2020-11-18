import React, {Component} from "react";
import carbsIcon from "../resources/bread-emoji.png";
import fatIcon from "../resources/bacon-strip-emoji.png";
import proteinIcon from "../resources/steak-emoji.png";
import caloriesIcon from "../resources/calories.png";
import caloriesBurnedIcon from "../resources/fire-emoji.png";
import StatsDayItem from "./StatsDayItem";

class StatsTable extends Component {
    render() {
        let statsDayItem;
        if (this.props.stats) {
            statsDayItem = this.props.stats.map(item => {
                return <StatsDayItem
                    date={item.date}
                    calories={item.netCalories}
                    caloriesBurned={item.caloriesBurned}
                    carbohydrates={item.carbohydrates}
                    fat={item.fat}
                    protein={item.protein}
                />
            });
        }
        return (
            <div className="StatsTable">
                <div className="StatsTable_header">
                    <span className="StatsTable_header--macros">
                        <img src={carbsIcon} alt="carbohydrates" title="carbohydrates"/>
                        <img src={fatIcon} alt="fat" title="fat"/>
                        <img src={proteinIcon} alt="protein" title="protein"/>
                    </span>
                    <span className="StatsTable_header--calories"><img src={caloriesIcon} alt="calories"
                                                                       title="calories"/></span>
                    <span className="StatsTable_header--caloriesBurned"><img src={caloriesBurnedIcon}
                                                                             alt="caloriesBurned"
                                                                             title="caloriesBurned"/></span>
                </div>
                {statsDayItem}
            </div>
        );
    }

}

export default StatsTable;