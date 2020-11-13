import React, {Component} from "react";

class NetCalories extends Component {

    render() {
        let goalDifference = this.props.netCalories - this.props.caloriesGoal;
        let calsDifference;
        if (goalDifference > 0) {
            calsDifference = (<span
                className="NetCalories__goalDifference over">(+{new Intl.NumberFormat().format(goalDifference)})</span>);
        } else {
            calsDifference = (<span
                className="NetCalories__goalDifference under">({new Intl.NumberFormat().format(goalDifference)})</span>);
        }
        return (
            <div className="NetCalories">
                <span className="NetCalories__equation">
                    <span
                        className="NetCalories__number food">{new Intl.NumberFormat().format(this.props.caloriesEaten)}</span>
                    <span className="NetCalories__label">calories from food</span>
                    <span className="NetCalories__minusSign">-</span>
                    <span
                        className="NetCalories__number exercise">{this.props.caloriesBurned ? new Intl.NumberFormat().format(this.props.caloriesBurned) : 0}</span>
                    <span className="NetCalories__label">calories from exercise</span>
                    <span className="NetCalories__equals">=</span>
                    <span
                        className="NetCalories__number net">{new Intl.NumberFormat().format(this.props.netCalories)}</span>
                </span>
                <div className="clearfix"/>
                {this.props.caloriesGoal > -1 && calsDifference}
                <div className="clearfix"/>
            </div>
        );
    }
}

export default NetCalories;