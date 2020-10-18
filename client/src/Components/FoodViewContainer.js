import React, {Component} from "react";
import FoodView from "./FoodView";

class FoodViewContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tab: undefined
        };
    }

    render() {
        const tab = 0;
        if (tab && this.state.tab !== tab) {
            this.setState({tab});
        }

        return (
            <div>
                <FoodView
                    tab={this.state.tab}
                />
            </div>
        );
    }
}

export default FoodViewContainer