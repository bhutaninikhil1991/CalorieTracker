import React, {Component} from "react";
import FoodItem from "./FoodItem";

class SearchResults extends Component {
    render() {
        let searchResults;
        if (this.props.error) {
            searchResults = (
                <p className="SearchResults__error">
                    Sorry, I can't find anything that matches your query. May be try something different?
                </p>
            );
        } else {
            if (this.props.searchResults) {
                searchResults = this.props.searchResults.map(result => {
                    return (
                        <FoodItem key={result.foodItemId}
                                  item={result}/>
                    );
                });
            }
        }
        return (
            <div className="searchResults">
                {searchResults}
            </div>
        );
    }
}


export default SearchResults;