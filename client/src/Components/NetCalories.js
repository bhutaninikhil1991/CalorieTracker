import React, {Component} from "react";

class NetCalories extends Component {

    render() {
        return (
            <div className="NetCalories">
                <span className="NetCalories__equation">
                    <span className="NetCalories__number food">{this.props.caloriesEaten}</span>
                    <span className="NetCalories__label">calories from food</span>
                    <span className="NetCalories__minusSign">-</span>
                    <span
                        className="NetCalories__number exercise">{this.props.caloriesBurned ? this.props.caloriesBurned : 0}</span>
                    <span className="NetCalories__label">calories from exercise</span>
                    <span className="NetCalories__equals">=</span>
                    <span className="NetCalories__number net">{this.props.netCalories}</span>
                </span>
            </div>
        );
    }
}

export default NetCalories;