import React, {Component} from "react";
import AddableFoodItem from "./AddableFoodItem";

/**
 * class responsible for showing the search results-
 */
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
            if (this.props.searchResults.length > 0) {
                searchResults = this.props.searchResults.map(food => {
                    return (
                        <AddableFoodItem
                            selectedServing={food.servingSizes[0]}
                            key={food.id}
                            item={food}
                            day={this.props.day}
                        />
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