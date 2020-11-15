import React, {Component} from "react";
import FoodView from "./FoodView";
import qs from "qs";

/**
 * food view container class
 */
class FoodViewContainer extends Component {
    /**
     * constructor
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            tab: undefined
        };
    }

    render() {
        const qsParsed = qs.parse(this.props.location.search.slice(1));
        const day = qsParsed.day;
        const tab = parseInt(qsParsed.tab);
        if (tab && this.state.tab !== tab) {
            this.setState({tab});
        }

        return (
            <div>
                <FoodView
                    tab={this.state.tab}
                    day={day}
                />
            </div>
        );
    }
}

export default FoodViewContainer