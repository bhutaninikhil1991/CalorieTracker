import React, {Component} from "react";

/**
 * holds statistics records
 */
class StatsDayItem extends Component {
    render() {
        return (
            <div className="StatsDayItem">
                <span className="StatsDayItem__day">{this.props.date}</span>
                <span className={"StatsDayItem__macros"}>
                    <span>{this.props.carbohydrates}</span>
                    <span>{this.props.fat}</span>
                    <span>{this.props.protein}</span>
                </span>
                <span className="StatsDayItem__calories">{this.props.calories}</span>
                <span className="StatsDayItem__caloriesBurned">{this.props.caloriesBurned}</span>
                <div className="clearfix"/>
            </div>
        );
    }
}

export default StatsDayItem;