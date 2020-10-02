import React, {Component} from "react";

class SearchFood extends Component {

    handleSearchChange(e) {
        this.props.handleSearchChange(e.target.value);
    }

    render() {
        return (
            <div>
                <input type="text" placeholder="search" value={this.props.searchFoodItem}
                       onChange={this.handleSearchChange.bind(this)}/>
            </div>
        );
    }
}

export default SearchFood;