import React, {Component} from "react";

/**
 * class responsible for searching the food item
 */
class SearchFood extends Component {

    /**
     * handle search change event
     * @param e
     */
    handleSearchChange(e) {
        this.props.handleSearchChange(e.target.value);
    }

    render() {
        return (
            <div className="SearchFood">
                <h1 className="page-title">Search Food</h1>
                <input type="text" placeholder="search" value={this.props.searchFoodItem}
                       onChange={this.handleSearchChange.bind(this)}/>
            </div>
        );
    }
}

export default SearchFood;