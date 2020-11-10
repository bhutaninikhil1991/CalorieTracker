import React, {Component} from "react";
import fireEmoji from "../resources/fire-emoji.png";

class ActivityInput extends Component {

    handleActivityChange(e) {
        this.props.handleActivityChange(e.target.value);
    }

    render() {
        return (
            <div className="ActivityInput">
                <img src={fireEmoji} alt={"Activity icon"}/>
                <span>Calories burned from activity</span>
                <input type="text" placeholder="0" className={this.props.caloriesBurned ? 'active' : ''}
                       value={this.props.caloriesBurned ? this.props.caloriesBurned : ''}
                       onChange={this.handleActivityChange.bind(this)}/>
                <span
                    className={"ActivityInput__calsLabel" + (this.props.caloriesBurned ? 'active' : '')}>cals</span>
            </div>
        );
    }

}

export default ActivityInput;