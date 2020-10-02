import React, {Component} from "react";
import SearchFood from "./SearchFood";
import {SERVER_URL} from "../config";
import SearchResults from "./SearchResults";

class FoodView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            searchFoodItem: '',
            searchResults: [],
            searchError: false
        }
    }

    // to handle search change
    handleSearchChange(foodItem) {
        this.setState({
            searchFoodItem: foodItem
        });

        this.getSearchResults(foodItem)
    }

    // to get search results
    getSearchResults(searchFoodItem) {
        fetch(`${SERVER_URL}` + "/api/foods/search?query=" + searchFoodItem)
            .then((response) => {
                if (response.ok) {
                    response.json()
                        .then(results => {
                            this.setState({
                                searchResults: results.success ? results.data[searchFoodItem] : [],
                                searchError: false
                            });
                        });
                } else {
                    this.setState({
                        searchError: true
                    });
                }
            });
    }

    render() {
        return (
            <div>
                <SearchFood searchFoodItem={this.state.searchFoodItem}
                            handleSearchChange={this.handleSearchChange.bind(this)}/>
                <SearchResults searchResults={this.state.searchResults}/>
            </div>
        )
    }
}

export default FoodView;